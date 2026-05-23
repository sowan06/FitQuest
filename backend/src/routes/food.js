import { Router } from 'express';
import { query, withTransaction } from '../db.js';
import { authRequired } from '../middleware/auth.js';
import { HttpError } from '../middleware/errorHandler.js';
import { awardXp, foodXp } from '../services/xp.js';
import { applyFoodStats } from '../services/stats.js';
import { updateStreak } from '../services/streaks.js';
import { evaluateOnFood, assignDailyQuests } from '../services/quests.js';
import { checkAchievements } from '../services/achievements.js';

const router = Router();

const MEAL_TYPES = new Set(['breakfast', 'lunch', 'dinner', 'snack']);

function calorieDeviationWarning(input) {
  const computed = input.protein_g * 4 + input.carbs_g * 4 + input.fat_g * 9;
  if (computed === 0) return null;
  const ratio = Math.abs(input.calories - computed) / computed;
  if (ratio > 0.10) {
    return {
      message: 'macro_calorie_mismatch',
      computed_calories: Number(computed.toFixed(1)),
      reported_calories: input.calories,
      deviation_pct: Number((ratio * 100).toFixed(1)),
    };
  }
  return null;
}

router.post('/', authRequired, async (req, res, next) => {
  try {
    const b = req.body || {};
    const required = ['food_name', 'calories', 'protein_g', 'carbs_g', 'fat_g', 'meal_type'];
    for (const k of required) {
      if (b[k] === undefined || b[k] === null) {
        throw new HttpError(400, 'missing_field', { field: k });
      }
    }
    if (typeof b.food_name !== 'string' || b.food_name.trim().length === 0) {
      throw new HttpError(400, 'invalid_food_name');
    }
    if (!MEAL_TYPES.has(b.meal_type)) {
      throw new HttpError(400, 'invalid_meal_type');
    }
    for (const k of ['calories', 'protein_g', 'carbs_g', 'fat_g']) {
      if (!Number.isFinite(b[k]) || b[k] < 0) {
        throw new HttpError(400, 'invalid_macro', { field: k });
      }
    }
    const servingG = b.serving_g === undefined || b.serving_g === null
      ? null
      : Number(b.serving_g);
    if (servingG !== null && (!Number.isFinite(servingG) || servingG < 0)) {
      throw new HttpError(400, 'invalid_serving_g');
    }

    const result = await withTransaction(async (client) => {
      await assignDailyQuests(client, req.userId);

      // detect "first meal of today" and "first meal of this calendar day"
      const { rows: priorToday } = await client.query(
        `SELECT COUNT(*)::int AS c FROM food_logs
          WHERE user_id = $1 AND logged_at::date = CURRENT_DATE`,
        [req.userId],
      );
      const isFirstMealToday = priorToday[0].c === 0;

      const baseXp = foodXp();
      const { rows: foodRows } = await client.query(
        `INSERT INTO food_logs
          (user_id, food_name, calories, protein_g, carbs_g, fat_g, serving_g, meal_type, xp_earned)
         VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
         RETURNING id, food_name, calories, protein_g, carbs_g, fat_g, serving_g, meal_type, logged_at, xp_earned`,
        [
          req.userId, b.food_name.trim(),
          b.calories, b.protein_g, b.carbs_g, b.fat_g,
          servingG, b.meal_type, baseXp,
        ],
      );
      const food = foodRows[0];

      // sum today's macros to detect goal-hit transitions
      const { rows: charRows } = await client.query(
        `SELECT daily_calorie_goal, daily_protein_goal_g
           FROM characters WHERE user_id = $1`,
        [req.userId],
      );
      const goals = charRows[0];
      const { rows: totalsRows } = await client.query(
        `SELECT COALESCE(SUM(calories),0)::float AS cal,
                COALESCE(SUM(protein_g),0)::float AS pro
           FROM food_logs
          WHERE user_id = $1 AND logged_at::date = CURRENT_DATE`,
        [req.userId],
      );
      const cal = totalsRows[0].cal;
      const pro = totalsRows[0].pro;

      const calLow = goals.daily_calorie_goal * 0.9;
      const calHigh = goals.daily_calorie_goal * 1.1;
      const hitCalorieNow = cal >= calLow && cal <= calHigh;
      const hitProteinNow = pro >= goals.daily_protein_goal_g * 0.9;

      // detect transition: was the goal already hit before this entry?
      const calBefore = cal - Number(food.calories);
      const proBefore = pro - Number(food.protein_g);
      const hitCalorieBefore = calBefore >= calLow && calBefore <= calHigh;
      const hitProteinBefore = proBefore >= goals.daily_protein_goal_g * 0.9;

      const newlyHitCalorie = !hitCalorieBefore && hitCalorieNow;
      const newlyHitProtein = !hitProteinBefore && hitProteinNow;

      const goalBonus = (newlyHitCalorie ? 10 : 0) + (newlyHitProtein ? 10 : 0);

      const streak = await updateStreak(client, req.userId);
      const statGains = await applyFoodStats(client, req.userId, {
        hitCalorie: newlyHitCalorie,
        hitProtein: newlyHitProtein,
      });

      const totalXp = baseXp + goalBonus;
      const xpResult = await awardXp(client, req.userId, totalXp);

      const completedQuests = await evaluateOnFood(client, req.userId, {
        hitProteinToday: newlyHitProtein,
        isNewDayInWeek: isFirstMealToday,
      });
      const unlockedAchievements = await checkAchievements(client, req.userId);

      // update the persisted xp_earned field with bonus
      if (totalXp !== baseXp) {
        await client.query(
          `UPDATE food_logs SET xp_earned = $1 WHERE id = $2`,
          [totalXp, food.id],
        );
        food.xp_earned = totalXp;
      }

      return {
        entry: {
          ...food,
          calories: Number(food.calories),
          protein_g: Number(food.protein_g),
          carbs_g: Number(food.carbs_g),
          fat_g: Number(food.fat_g),
          serving_g: food.serving_g === null ? null : Number(food.serving_g),
          xp_earned: food.xp_earned,
        },
        xp_earned: totalXp,
        stat_gains: statGains,
        leveled_up: xpResult.leveled_up,
        new_level: xpResult.new_level,
        new_avatar_stage: xpResult.new_avatar_stage,
        streak_days: streak.streak_days,
        warning: calorieDeviationWarning(b),
        completed_quests: completedQuests,
        unlocked_achievements: unlockedAchievements.map((a) => ({
          id: a.id, title: a.title, xp_reward: a.xp_reward,
        })),
        daily_totals: { calories: cal, protein_g: pro },
      };
    });

    res.status(201).json(result);
  } catch (err) {
    next(err);
  }
});

router.get('/today', authRequired, async (req, res, next) => {
  try {
    const { rows: entries } = await query(
      `SELECT id, food_name, calories, protein_g, carbs_g, fat_g,
              serving_g, meal_type, logged_at, xp_earned
         FROM food_logs
        WHERE user_id = $1 AND logged_at::date = CURRENT_DATE
        ORDER BY logged_at`,
      [req.userId],
    );
    const totals = entries.reduce(
      (acc, e) => ({
        calories: acc.calories + Number(e.calories),
        protein_g: acc.protein_g + Number(e.protein_g),
        carbs_g: acc.carbs_g + Number(e.carbs_g),
        fat_g: acc.fat_g + Number(e.fat_g),
      }),
      { calories: 0, protein_g: 0, carbs_g: 0, fat_g: 0 },
    );
    res.json({
      totals: {
        calories: Number(totals.calories.toFixed(1)),
        protein_g: Number(totals.protein_g.toFixed(1)),
        carbs_g: Number(totals.carbs_g.toFixed(1)),
        fat_g: Number(totals.fat_g.toFixed(1)),
      },
      entries: entries.map((e) => ({
        ...e,
        calories: Number(e.calories),
        protein_g: Number(e.protein_g),
        carbs_g: Number(e.carbs_g),
        fat_g: Number(e.fat_g),
        serving_g: e.serving_g === null ? null : Number(e.serving_g),
      })),
    });
  } catch (err) {
    next(err);
  }
});

router.get('/', authRequired, async (req, res, next) => {
  try {
    const page = Math.max(1, Number(req.query.page) || 1);
    const limit = Math.min(50, Math.max(1, Number(req.query.limit) || 30));
    const offset = (page - 1) * limit;
    const { rows } = await query(
      `SELECT id, food_name, calories, protein_g, carbs_g, fat_g,
              serving_g, meal_type, logged_at, xp_earned
         FROM food_logs WHERE user_id = $1
        ORDER BY logged_at DESC
        LIMIT $2 OFFSET $3`,
      [req.userId, limit, offset],
    );
    res.json({
      page, limit,
      entries: rows.map((e) => ({
        ...e,
        calories: Number(e.calories),
        protein_g: Number(e.protein_g),
        carbs_g: Number(e.carbs_g),
        fat_g: Number(e.fat_g),
        serving_g: e.serving_g === null ? null : Number(e.serving_g),
      })),
    });
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', authRequired, async (req, res, next) => {
  try {
    const { rowCount } = await query(
      `DELETE FROM food_logs WHERE user_id = $1 AND id = $2`,
      [req.userId, req.params.id],
    );
    if (rowCount === 0) throw new HttpError(404, 'food_not_found');
    res.status(204).send();
  } catch (err) {
    next(err);
  }
});

export default router;
