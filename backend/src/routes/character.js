import { Router } from 'express';
import { query } from '../db.js';
import { authRequired } from '../middleware/auth.js';
import { HttpError } from '../middleware/errorHandler.js';
import { avatarLabelForStage } from '../services/xp.js';

const router = Router();

function shape(row) {
  if (!row) return null;
  const xp = row.xp;
  const xpToNext = row.xp_to_next;
  const pct = xpToNext > 0 ? Math.max(0, Math.min(100, (xp / xpToNext) * 100)) : 0;
  return {
    id: row.id,
    user_id: row.user_id,
    name: row.name,
    level: row.level,
    xp,
    xp_to_next: xpToNext,
    xp_progress_pct: Number(pct.toFixed(2)),
    avatar_stage: row.avatar_stage,
    avatar_label: avatarLabelForStage(row.avatar_stage),
    stat_str: row.stat_str,
    stat_vit: row.stat_vit,
    stat_end: row.stat_end,
    stat_wis: row.stat_wis,
    stat_con: row.stat_con,
    streak_days: row.streak_days,
    last_activity_date: row.last_activity_date,
    total_volume_kg: Number(row.total_volume_kg),
    total_workouts: row.total_workouts,
    daily_calorie_goal: row.daily_calorie_goal,
    daily_protein_goal_g: row.daily_protein_goal_g,
    updated_at: row.updated_at,
  };
}

router.get('/', authRequired, async (req, res, next) => {
  try {
    const { rows } = await query(
      `SELECT * FROM characters WHERE user_id = $1`,
      [req.userId],
    );
    if (rows.length === 0) throw new HttpError(404, 'character_not_found');
    res.json(shape(rows[0]));
  } catch (err) {
    next(err);
  }
});

router.patch('/', authRequired, async (req, res, next) => {
  try {
    const { name } = req.body || {};
    if (typeof name !== 'string' || name.trim().length < 1 || name.length > 32) {
      throw new HttpError(400, 'invalid_name');
    }
    const { rows } = await query(
      `UPDATE characters SET name = $1, updated_at = NOW()
        WHERE user_id = $2
        RETURNING *`,
      [name.trim(), req.userId],
    );
    if (rows.length === 0) throw new HttpError(404, 'character_not_found');
    res.json(shape(rows[0]));
  } catch (err) {
    next(err);
  }
});

export default router;
