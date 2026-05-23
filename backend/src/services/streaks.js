/**
 * Streak service. Pure date math at the top, DB application below.
 */

/** Returns 'increment' | 'same' | 'reset' | 'first'. */
export function classifyStreakTransition(lastActivityDate, today) {
  if (!lastActivityDate) return 'first';
  const last = new Date(lastActivityDate);
  const cur = new Date(today);
  // normalize to date-only UTC
  const lastDay = Date.UTC(last.getUTCFullYear(), last.getUTCMonth(), last.getUTCDate());
  const curDay = Date.UTC(cur.getUTCFullYear(), cur.getUTCMonth(), cur.getUTCDate());
  const diffDays = Math.round((curDay - lastDay) / 86_400_000);
  if (diffDays === 0) return 'same';
  if (diffDays === 1) return 'increment';
  return 'reset';
}

export function nextStreakDays(currentStreak, transition) {
  switch (transition) {
    case 'first':
    case 'reset':
      return 1;
    case 'increment':
      return currentStreak + 1;
    case 'same':
    default:
      return currentStreak;
  }
}

/**
 * Update the character's streak based on today's activity.
 * Must run inside an existing transaction. Returns the new streak.
 */
export async function updateStreak(client, userId) {
  const { rows } = await client.query(
    `SELECT streak_days, last_activity_date
       FROM characters
      WHERE user_id = $1
      FOR UPDATE`,
    [userId],
  );
  if (rows.length === 0) {
    throw new Error(`character not found for user ${userId}`);
  }
  const { streak_days: currentStreak, last_activity_date } = rows[0];
  const today = new Date();
  const transition = classifyStreakTransition(last_activity_date, today);
  const newStreak = nextStreakDays(currentStreak, transition);

  await client.query(
    `UPDATE characters
        SET streak_days = $2,
            last_activity_date = CURRENT_DATE,
            updated_at = NOW()
      WHERE user_id = $1`,
    [userId, newStreak],
  );

  return { streak_days: newStreak, transition };
}
