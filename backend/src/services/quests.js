import { sendFcmNotification, getFcmToken } from './fcm.js';

/** ISO date string for "today" in UTC (YYYY-MM-DD). */
function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

/** Monday of the current ISO week, as YYYY-MM-DD. */
function weekStartIso() {
  const d = new Date();
  const day = d.getUTCDay() || 7;
  if (day !== 1) d.setUTCDate(d.getUTCDate() - (day - 1));
  return d.toISOString().slice(0, 10);
}

/**
 * Ensure today's daily quests + this week's weekly quests are assigned.
 * Idempotent — uses ON CONFLICT DO NOTHING via the unique constraint
 * (user_id, quest_id, assigned_date).
 */
export async function assignDailyQuests(client, userId) {
  const today = todayIso();
  const weekStart = weekStartIso();

  const { rows: defs } = await client.query(
    `SELECT id, quest_type, target FROM quest_definitions
      WHERE quest_type IN ('daily','weekly')`,
  );
  for (const def of defs) {
    const assignedDate = def.quest_type === 'daily' ? today : weekStart;
    await client.query(
      `INSERT INTO user_quests (user_id, quest_id, status, progress, target, assigned_date)
       VALUES ($1, $2, 'active', 0, $3, $4)
       ON CONFLICT (user_id, quest_id, assigned_date) DO NOTHING`,
      [userId, def.id, def.target, assignedDate],
    );
  }
}

/**
 * Increment a quest's progress and mark it completed if target reached.
 * Returns true if newly completed.
 */
async function bumpQuest(client, userId, questId, assignedDate, increment = 1) {
  const { rows } = await client.query(
    `UPDATE user_quests
        SET progress = LEAST(progress + $4, target),
            status = CASE
              WHEN status = 'active' AND progress + $4 >= target THEN 'completed'
              ELSE status
            END,
            completed_at = CASE
              WHEN status = 'active' AND progress + $4 >= target THEN NOW()
              ELSE completed_at
            END
      WHERE user_id = $1 AND quest_id = $2 AND assigned_date = $3
      RETURNING quest_id, status, progress, target,
                (status = 'completed' AND completed_at >= NOW() - INTERVAL '1 second') AS just_completed`,
    [userId, questId, assignedDate, increment],
  );
  return rows[0]?.just_completed === true;
}

async function notifyComplete(client, userId, questId) {
  const [{ rows: defRows }, fcmToken] = await Promise.all([
    client.query('SELECT title, xp_reward FROM quest_definitions WHERE id = $1', [questId]),
    getFcmToken(client, userId),
  ]);
  const def = defRows[0];
  if (!def || !fcmToken) return;
  await sendFcmNotification(
    fcmToken,
    'Quest Complete!',
    `${def.title} — Tap to claim ${def.xp_reward} XP`,
    { type: 'quest_complete', quest_id: questId },
  );
}

/**
 * Called after every workout log. Increments the relevant daily and weekly
 * workout quests.
 */
export async function evaluateOnWorkout(client, userId) {
  const today = todayIso();
  const weekStart = weekStartIso();
  const completed = [];
  if (await bumpQuest(client, userId, 'daily_log_workout', today, 1)) {
    completed.push('daily_log_workout');
  }
  if (await bumpQuest(client, userId, 'weekly_5_workouts', weekStart, 1)) {
    completed.push('weekly_5_workouts');
  }
  for (const q of completed) await notifyComplete(client, userId, q);
  return completed;
}

/**
 * Called after every food log. Increments meal-count quest. Optionally bumps
 * the protein-goal quest if the day's protein target is now hit.
 */
export async function evaluateOnFood(client, userId, { hitProteinToday, isNewDayInWeek }) {
  const today = todayIso();
  const weekStart = weekStartIso();
  const completed = [];

  if (await bumpQuest(client, userId, 'daily_log_all_meals', today, 1)) {
    completed.push('daily_log_all_meals');
  }
  if (hitProteinToday) {
    // mark the daily protein quest as completed (binary, target=1)
    if (await bumpQuest(client, userId, 'daily_hit_protein', today, 1)) {
      completed.push('daily_hit_protein');
    }
  }
  if (isNewDayInWeek) {
    if (await bumpQuest(client, userId, 'weekly_7_day_log', weekStart, 1)) {
      completed.push('weekly_7_day_log');
    }
  }

  for (const q of completed) await notifyComplete(client, userId, q);
  return completed;
}

/** Claim XP for a completed quest. Returns the XP awarded or throws if invalid. */
export async function claimQuestReward(client, userId, questId) {
  const today = todayIso();
  const weekStart = weekStartIso();
  const { rows } = await client.query(
    `UPDATE user_quests uq
        SET status = 'claimed'
       FROM quest_definitions qd
      WHERE uq.quest_id = qd.id
        AND uq.user_id = $1
        AND uq.quest_id = $2
        AND uq.status = 'completed'
        AND uq.assigned_date IN ($3::date, $4::date)
      RETURNING qd.xp_reward`,
    [userId, questId, today, weekStart],
  );
  if (rows.length === 0) {
    return { ok: false, xp: 0 };
  }
  return { ok: true, xp: rows[0].xp_reward };
}

/** Return today's daily + this week's weekly quests for a user. */
export async function listUserQuests(client, userId) {
  const today = todayIso();
  const weekStart = weekStartIso();
  const { rows } = await client.query(
    `SELECT uq.id, uq.quest_id, uq.status, uq.progress, uq.target,
            uq.assigned_date, uq.completed_at,
            qd.title, qd.description, qd.xp_reward, qd.quest_type
       FROM user_quests uq
       JOIN quest_definitions qd ON qd.id = uq.quest_id
      WHERE uq.user_id = $1
        AND ((qd.quest_type = 'daily'  AND uq.assigned_date = $2::date)
          OR (qd.quest_type = 'weekly' AND uq.assigned_date = $3::date))
      ORDER BY qd.quest_type, qd.id`,
    [userId, today, weekStart],
  );
  return rows;
}
