/**
 * Integration test helpers. Requires TEST_DATABASE_URL env var pointing at a
 * disposable Postgres database with all migrations applied.
 *
 * Each test should call `cleanupAll()` in afterEach to wipe rows.
 */
import { pool } from '../src/db.js';

export async function resetDatabase() {
  await pool.query(`
    TRUNCATE
      user_achievements, user_quests,
      workout_sets, workout_sessions,
      food_logs,
      characters, users
    RESTART IDENTITY CASCADE
  `);
}

export async function closeDatabase() {
  await pool.end();
}
