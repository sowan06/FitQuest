/**
 * Stat mapping service.
 *
 *   STR — +1 every 10,000 kg total volume lifted
 *   VIT — +1 every 7-day streak milestone
 *   END — +1 every 10 completed workout sessions
 *   WIS — +1 every 14 days of hitting protein goal
 *   CON — +1 every 14 days of hitting calorie goal
 *
 * Stats only ever increase. We compute the new value and only emit a gain row
 * when it crosses a threshold.
 */

const STR_PER_KG = 10_000;
const END_PER_WORKOUTS = 10;
const VIT_PER_STREAK = 7;
const WIS_PER_PROTEIN_DAYS = 14;
const CON_PER_CALORIE_DAYS = 14;

export function computeStr(totalVolumeKg) {
  return 1 + Math.floor(Math.max(0, totalVolumeKg) / STR_PER_KG);
}
export function computeEnd(totalWorkouts) {
  return 1 + Math.floor(Math.max(0, totalWorkouts) / END_PER_WORKOUTS);
}
export function computeVit(streakDays) {
  return 1 + Math.floor(Math.max(0, streakDays) / VIT_PER_STREAK);
}
export function computeWis(proteinGoalDays) {
  return 1 + Math.floor(Math.max(0, proteinGoalDays) / WIS_PER_PROTEIN_DAYS);
}
export function computeCon(calorieGoalDays) {
  return 1 + Math.floor(Math.max(0, calorieGoalDays) / CON_PER_CALORIE_DAYS);
}

/**
 * After a workout: bump total_volume_kg and total_workouts, recompute STR/END,
 * emit any gain rows. Runs inside a transaction.
 */
export async function applyWorkoutStats(client, userId, addedVolumeKg) {
  const { rows } = await client.query(
    `SELECT stat_str, stat_end, stat_vit, total_volume_kg, total_workouts, streak_days
       FROM characters WHERE user_id = $1 FOR UPDATE`,
    [userId],
  );
  if (rows.length === 0) throw new Error(`character not found for user ${userId}`);
  const c = rows[0];

  const newVolume = Number(c.total_volume_kg) + Number(addedVolumeKg || 0);
  const newWorkouts = c.total_workouts + 1;
  const newStr = computeStr(newVolume);
  const newEnd = computeEnd(newWorkouts);
  const newVit = computeVit(c.streak_days);

  const gains = [];
  if (newStr > c.stat_str) gains.push({ stat: 'STR', old_value: c.stat_str, new_value: newStr });
  if (newEnd > c.stat_end) gains.push({ stat: 'END', old_value: c.stat_end, new_value: newEnd });
  if (newVit > c.stat_vit) gains.push({ stat: 'VIT', old_value: c.stat_vit, new_value: newVit });

  await client.query(
    `UPDATE characters
        SET total_volume_kg = $2,
            total_workouts = $3,
            stat_str = $4,
            stat_end = $5,
            stat_vit = $6,
            updated_at = NOW()
      WHERE user_id = $1`,
    [userId, newVolume, newWorkouts, newStr, newEnd, newVit],
  );

  return gains;
}

/**
 * After a food entry that hit (or didn't) calorie/protein goals, advance the
 * cumulative day counters and recompute WIS/CON.
 *
 * `flags` { hitProtein: bool, hitCalorie: bool } — true means we should
 * increment the cumulative day count (caller dedupes per day).
 */
export async function applyFoodStats(client, userId, flags) {
  const { rows } = await client.query(
    `SELECT stat_wis, stat_con, protein_goal_days, calorie_goal_days
       FROM characters WHERE user_id = $1 FOR UPDATE`,
    [userId],
  );
  if (rows.length === 0) throw new Error(`character not found for user ${userId}`);
  const c = rows[0];

  const newProteinDays = c.protein_goal_days + (flags.hitProtein ? 1 : 0);
  const newCalorieDays = c.calorie_goal_days + (flags.hitCalorie ? 1 : 0);
  const newWis = computeWis(newProteinDays);
  const newCon = computeCon(newCalorieDays);

  const gains = [];
  if (newWis > c.stat_wis) gains.push({ stat: 'WIS', old_value: c.stat_wis, new_value: newWis });
  if (newCon > c.stat_con) gains.push({ stat: 'CON', old_value: c.stat_con, new_value: newCon });

  await client.query(
    `UPDATE characters
        SET protein_goal_days = $2,
            calorie_goal_days = $3,
            stat_wis = $4,
            stat_con = $5,
            updated_at = NOW()
      WHERE user_id = $1`,
    [userId, newProteinDays, newCalorieDays, newWis, newCon],
  );

  return gains;
}
