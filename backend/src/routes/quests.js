import { Router } from 'express';
import { withTransaction, query } from '../db.js';
import { authRequired } from '../middleware/auth.js';
import { HttpError } from '../middleware/errorHandler.js';
import {
  assignDailyQuests,
  listUserQuests,
  claimQuestReward,
} from '../services/quests.js';
import { awardXp } from '../services/xp.js';
import { listAchievements } from '../services/achievements.js';

const router = Router();

router.get('/', authRequired, async (req, res, next) => {
  try {
    const isAchievementsRoute = req.baseUrl.endsWith('/achievements');
    if (isAchievementsRoute) {
      const result = await withTransaction(async (client) => {
        return listAchievements(client, req.userId);
      });
      return res.json(result);
    }
    const result = await withTransaction(async (client) => {
      await assignDailyQuests(client, req.userId);
      return listUserQuests(client, req.userId);
    });
    res.json(result);
  } catch (err) {
    next(err);
  }
});

router.post('/:quest_id/claim', authRequired, async (req, res, next) => {
  try {
    const result = await withTransaction(async (client) => {
      const claim = await claimQuestReward(client, req.userId, req.params.quest_id);
      if (!claim.ok) throw new HttpError(409, 'quest_not_claimable');
      const xpRes = await awardXp(client, req.userId, claim.xp);
      return {
        xp_awarded: claim.xp,
        leveled_up: xpRes.leveled_up,
        new_level: xpRes.new_level,
        new_avatar_stage: xpRes.new_avatar_stage,
      };
    });
    res.json(result);
  } catch (err) {
    next(err);
  }
});

// Standalone helper used by /achievements route mount
export async function getAchievements(userId) {
  const { rows } = await query(
    `SELECT ad.id, ad.title, ad.description, ad.xp_reward, ad.icon, ua.unlocked_at
       FROM achievement_definitions ad
       LEFT JOIN user_achievements ua
         ON ua.achievement_id = ad.id AND ua.user_id = $1
      ORDER BY ad.id`,
    [userId],
  );
  return rows;
}

export default router;
