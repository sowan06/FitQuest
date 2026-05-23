import pg from 'pg';
import { config } from './config.js';

export const pool = new pg.Pool({
  connectionString: config.databaseUrl,
  max: 10,
  idleTimeoutMillis: 30_000,
});

pool.on('error', (err) => {
  console.error('[db] unexpected pool error', err);
});

export function query(text, params) {
  return pool.query(text, params);
}

/**
 * Run a callback inside a transaction. Acquires a client, BEGINs, calls the
 * callback with the client, COMMITs on success, ROLLBACKs on error.
 */
export async function withTransaction(fn) {
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    const result = await fn(client);
    await client.query('COMMIT');
    return result;
  } catch (err) {
    try {
      await client.query('ROLLBACK');
    } catch (_rollbackErr) {
      // ignore rollback failures, propagate the original error
    }
    throw err;
  } finally {
    client.release();
  }
}
