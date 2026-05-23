/**
 * XP and leveling engine.
 *
 * Pure functions live at the top so they can be unit tested without a DB.
 * The DB-touching `awardXp` runs inside an existing transaction (caller passes
 * a `pg` client from withTransaction).
 */

export const AVATAR_STAGES = [
  { stage: 0, label: 'Rookie',  minLevel: 1 },
  { stage: 1, label: 'Fighter', minLevel: 5 },
  { stage: 2, label: 'Warrior', minLevel: 15 },
  { stage: 3, label: 'Legend',  minLevel: 30 },
];

/** XP needed to advance from `level` to `level + 1`. */
export function xpToNextLevel(level) {
  return Math.round(100 * Math.pow(level, 1.5));
}

/** Avatar stage for a given level. */
export function avatarStageForLevel(level) {
  let stage = 0;
  for (const s of AVATAR_STAGES) {
    if (level >= s.minLevel) stage = s.stage;
  }
  return stage;
}

export function avatarLabelForStage(stage) {
  return AVATAR_STAGES.find((s) => s.stage === stage)?.label ?? 'Rookie';
}

/** XP for a workout: 20 base + floor(volume / 1000) bonus. */
export function workoutXp(totalVolumeKg) {
  const safeVolume = Number.isFinite(totalVolumeKg) ? Math.max(0, totalVolumeKg) : 0;
  return 20 + Math.floor(safeVolume / 1000);
}

/** XP for a single food log entry. */
export function foodXp() {
  return 5;
}

/** Streak bonus XP when streak grows. */
export function streakBonusXp(streakDays) {
  if (!Number.isFinite(streakDays) || streakDays <= 0) return 0;
  return streakDays * 2;
}

/**
 * Pure leveling step. Given current state, returns post-application state.
 * Loops to handle multi-level-ups.
 */
export function applyXp({ level, xp, xpToNext }, gainedXp) {
  let newLevel = level;
  let newXp = xp + gainedXp;
  let newXpToNext = xpToNext;
  let leveledUp = false;
  let levelsGained = 0;

  while (newXp >= newXpToNext) {
    newXp -= newXpToNext;
    newLevel += 1;
    levelsGained += 1;
    newXpToNext = xpToNextLevel(newLevel);
    leveledUp = true;
  }

  return {
    level: newLevel,
    xp: newXp,
    xpToNext: newXpToNext,
    leveledUp,
    levelsGained,
    avatarStage: avatarStageForLevel(newLevel),
  };
}

/**
 * Apply XP to the character row in DB. Returns an award summary.
 * Must be called inside a transaction (`client` is a pg client, not the pool).
 */
export async function awardXp(client, userId, gainedXp) {
  const { rows } = await client.query(
    `SELECT id, level, xp, xp_to_next, avatar_stage
       FROM characters
      WHERE user_id = $1
      FOR UPDATE`,
    [userId],
  );
  if (rows.length === 0) {
    throw new Error(`character not found for user ${userId}`);
  }
  const before = {
    level: rows[0].level,
    xp: rows[0].xp,
    xpToNext: rows[0].xp_to_next,
    avatarStage: rows[0].avatar_stage,
  };

  const after = applyXp(before, gainedXp);

  await client.query(
    `UPDATE characters
        SET level = $2,
            xp = $3,
            xp_to_next = $4,
            avatar_stage = $5,
            updated_at = NOW()
      WHERE user_id = $1`,
    [userId, after.level, after.xp, after.xpToNext, after.avatarStage],
  );

  return {
    xp_earned: gainedXp,
    leveled_up: after.leveledUp,
    new_level: after.leveledUp ? after.level : null,
    new_avatar_stage:
      after.avatarStage !== before.avatarStage ? after.avatarStage : null,
  };
}
