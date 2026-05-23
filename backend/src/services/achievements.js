import { sendFcmNotification, getFcmToken } from './fcm.js';

/**
 * Check every achievement against current state and unlock newly qualifying
 * ones. Returns an array of { id, title, xp_reward } unlocked this call.
 *
 * Idempotent — INSERT ... ON CONFLICT DO NOTHING ensures we never award twice.
 */
export async function checkAchievements(client, userId) {
  const { rows: charRows } = await client.query(
    `SELECT level, streak_days, total_volume_kg, total_workouts
       FROM characters WHERE user_id = $1`,
    [userId],
  );
  if (charRows.length === 0) return [];
  const c = charRows[0];

  const candidates = [];
  if (c.total_workouts >= 1) candidates.push('first_workout');
  if (c.streak_days >= 7) candidates.push('streak_7');
  if (c.streak_days >= 30) candidates.push('streak_30');
  if (c.level >= 5) candidates.push('level_5');
  if (c.level >= 15) candidates.push('level_15');
  if (Number(c.total_volume_kg) >= 100_000) candidates.push('total_volume_100k');

  if (candidates.length === 0) return [];

  const placeholders = candidates.map((_, i) => `$${i + 2}`).join(',');
  const { rows: inserted } = await client.query(
    `INSERT INTO user_achievements (user_id, achievement_id)
     SELECT $1, ad.id FROM achievement_definitions ad
      WHERE ad.id IN (${placeholders})
        AND NOT EXISTS (
          SELECT 1 FROM user_achievements ua
           WHERE ua.user_id = $1 AND ua.achievement_id = ad.id
        )
     RETURNING achievement_id`,
    [userId, ...candidates],
  );
  if (inserted.length === 0) return [];

  const ids = inserted.map((r) => r.achievement_id);
  const { rows: defs } = await client.query(
    `SELECT id, title, description, xp_reward FROM achievement_definitions
      WHERE id = ANY($1::text[])`,
    [ids],
  );

  // fire-and-forget notifications
  const fcmToken = await getFcmToken(client, userId);
  for (const def of defs) {
    if (fcmToken) {
      sendFcmNotification(
        fcmToken,
        'Achievement Unlocked!',
        `${def.title} — ${def.description}`,
        { type: 'achievement', achievement_id: def.id },
      );
    }
  }
  return defs;
}

/** List all achievements with unlock state for a user. */
export async function listAchievements(client, userId) {
  const { rows } = await client.query(
    `SELECT ad.id, ad.title, ad.description, ad.xp_reward, ad.icon,
            ua.unlocked_at
       FROM achievement_definitions ad
       LEFT JOIN user_achievements ua
         ON ua.achievement_id = ad.id AND ua.user_id = $1
      ORDER BY ad.id`,
    [userId],
  );
  return rows;
}
