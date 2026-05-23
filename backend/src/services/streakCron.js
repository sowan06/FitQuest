import cron from 'node-cron';
import { pool } from '../db.js';
import { sendFcmNotification } from './fcm.js';

/**
 * Daily 20:00 UTC streak reminder. Notifies any user who has a streak ≥ 1
 * and has NOT logged anything today (last_activity_date < today).
 */
export function startStreakCron() {
  // 20:00 UTC every day
  cron.schedule('0 20 * * *', async () => {
    try {
      const { rows } = await pool.query(
        `SELECT u.fcm_token, c.streak_days
           FROM characters c
           JOIN users u ON u.id = c.user_id
          WHERE u.fcm_token IS NOT NULL
            AND c.streak_days >= 1
            AND (c.last_activity_date IS NULL OR c.last_activity_date < CURRENT_DATE)`,
      );
      for (const row of rows) {
        await sendFcmNotification(
          row.fcm_token,
          "Don't break your streak!",
          `You're on a ${row.streak_days} day streak. Log something today!`,
          { type: 'streak_reminder' },
        );
      }
    } catch (err) {
      console.warn('[streakCron] failed', err?.message ?? err);
    }
  });
  console.log('[streakCron] scheduled daily at 20:00 UTC');
}
