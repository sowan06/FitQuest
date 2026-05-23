import { Router } from 'express';
import { query, withTransaction } from '../db.js';
import { authRequired } from '../middleware/auth.js';
import { HttpError } from '../middleware/errorHandler.js';
import { awardXp, workoutXp, streakBonusXp } from '../services/xp.js';
import { applyWorkoutStats } from '../services/stats.js';
import { updateStreak } from '../services/streaks.js';
import { evaluateOnWorkout, assignDailyQuests } from '../services/quests.js';
import { checkAchievements } from '../services/achievements.js';

const router = Router();

router.post('/', authRequired, async (req, res, next) => {
  try {
    const { notes, sets } = req.body || {};
    if (!Array.isArray(sets) || sets.length === 0) {
      throw new HttpError(400, 'no_sets');
    }
    for (const s of sets) {
      if (
        typeof s.exercise_name !== 'string' ||
        s.exercise_name.trim().length === 0 ||
        !Number.isFinite(s.reps) || s.reps < 1 ||
        !Number.isFinite(s.weight_kg) || s.weight_kg <= 0
      ) {
        throw new HttpError(400, 'invalid_set');
      }
    }

    const totalVolume = sets.reduce(
      (sum, s) => sum + s.reps * s.weight_kg,
      0,
    );

    const result = await withTransaction(async (client) => {
      await assignDailyQuests(client, req.userId);

      const baseXp = workoutXp(totalVolume);

      const { rows: sessRows } = await client.query(
        `INSERT INTO workout_sessions (user_id, notes, total_volume_kg, xp_earned)
         VALUES ($1, $2, $3, $4)
         RETURNING id, logged_at`,
        [req.userId, notes ?? null, totalVolume, baseXp],
      );
      const session = sessRows[0];

      // bulk insert sets
      const perSetXp = Math.floor(baseXp / sets.length);
      const setRows = [];
      for (let i = 0; i < sets.length; i++) {
        const s = sets[i];
        const { rows } = await client.query(
          `INSERT INTO workout_sets
             (session_id, exercise_name, set_number, reps, weight_kg, xp_earned)
           VALUES ($1, $2, $3, $4, $5, $6)
           RETURNING id, exercise_name, set_number, reps, weight_kg, xp_earned`,
          [session.id, s.exercise_name.trim(), i + 1, s.reps, s.weight_kg, perSetXp],
        );
        setRows.push(rows[0]);
      }

      // streak first (so VIT can pick up new streak)
      const streak = await updateStreak(client, req.userId);
      const streakBonus = streak.transition === 'increment' || streak.transition === 'first'
        ? streakBonusXp(streak.streak_days)
        : 0;

      const totalXp = baseXp + streakBonus;

      const statGains = await applyWorkoutStats(client, req.userId, totalVolume);
      const xpResult = await awardXp(client, req.userId, totalXp);

      // quests after XP so completion notifications reflect updated XP
      const completedQuests = await evaluateOnWorkout(client, req.userId);
      const unlockedAchievements = await checkAchievements(client, req.userId);

      return {
        session_id: session.id,
        logged_at: session.logged_at,
        sets: setRows,
        total_volume: totalVolume,
        xp_earned: totalXp,
        stat_gains: statGains,
        leveled_up: xpResult.leveled_up,
        new_level: xpResult.new_level,
        new_avatar_stage: xpResult.new_avatar_stage,
        streak_days: streak.streak_days,
        completed_quests: completedQuests,
        unlocked_achievements: unlockedAchievements.map((a) => ({
          id: a.id,
          title: a.title,
          xp_reward: a.xp_reward,
        })),
      };
    });

    res.status(201).json(result);
  } catch (err) {
    next(err);
  }
});

router.get('/', authRequired, async (req, res, next) => {
  try {
    const page = Math.max(1, Number(req.query.page) || 1);
    const limit = Math.min(50, Math.max(1, Number(req.query.limit) || 20));
    const offset = (page - 1) * limit;

    const { rows: sessions } = await query(
      `SELECT id, logged_at, notes, total_volume_kg, xp_earned
         FROM workout_sessions
        WHERE user_id = $1
        ORDER BY logged_at DESC
        LIMIT $2 OFFSET $3`,
      [req.userId, limit, offset],
    );
    if (sessions.length === 0) {
      return res.json({ page, limit, sessions: [] });
    }
    const ids = sessions.map((s) => s.id);
    const { rows: sets } = await query(
      `SELECT id, session_id, exercise_name, set_number, reps, weight_kg, xp_earned
         FROM workout_sets WHERE session_id = ANY($1::uuid[])
        ORDER BY set_number`,
      [ids],
    );
    const grouped = sessions.map((s) => ({
      ...s,
      total_volume_kg: Number(s.total_volume_kg),
      sets: sets.filter((x) => x.session_id === s.id).map((x) => ({
        ...x, weight_kg: Number(x.weight_kg),
      })),
    }));
    res.json({ page, limit, sessions: grouped });
  } catch (err) {
    next(err);
  }
});

router.get('/stats/volume', authRequired, async (req, res, next) => {
  try {
    const days = Math.min(365, Math.max(1, Number(req.query.days) || 30));
    const { rows } = await query(
      `SELECT DATE(logged_at) AS day, COALESCE(SUM(total_volume_kg), 0) AS volume_kg
         FROM workout_sessions
        WHERE user_id = $1 AND logged_at >= NOW() - $2::interval
        GROUP BY day
        ORDER BY day`,
      [req.userId, `${days} days`],
    );
    res.json(
      rows.map((r) => ({ day: r.day, volume_kg: Number(r.volume_kg) })),
    );
  } catch (err) {
    next(err);
  }
});

router.get('/:id', authRequired, async (req, res, next) => {
  try {
    const { rows: sessions } = await query(
      `SELECT id, logged_at, notes, total_volume_kg, xp_earned
         FROM workout_sessions
        WHERE user_id = $1 AND id = $2`,
      [req.userId, req.params.id],
    );
    if (sessions.length === 0) throw new HttpError(404, 'session_not_found');
    const { rows: sets } = await query(
      `SELECT id, exercise_name, set_number, reps, weight_kg, xp_earned
         FROM workout_sets WHERE session_id = $1 ORDER BY set_number`,
      [req.params.id],
    );
    res.json({
      ...sessions[0],
      total_volume_kg: Number(sessions[0].total_volume_kg),
      sets: sets.map((s) => ({ ...s, weight_kg: Number(s.weight_kg) })),
    });
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', authRequired, async (req, res, next) => {
  try {
    const { rowCount } = await query(
      `DELETE FROM workout_sessions WHERE user_id = $1 AND id = $2`,
      [req.userId, req.params.id],
    );
    if (rowCount === 0) throw new HttpError(404, 'session_not_found');
    res.status(204).send();
  } catch (err) {
    next(err);
  }
});

export default router;
