#!/usr/bin/env node
/**
 * Tiny migration runner for plain SQL files in `migrations/`.
 *
 *  - Files must be named `NNN_*.sql` and applied in numeric order.
 *  - Each file is split on `-- Down Migration` (everything below is ignored
 *    on `up` runs) and executed inside one transaction.
 *  - Applied files are recorded in `_migrations` so each runs at most once.
 *
 * Usage:
 *   node scripts/migrate.js up        # default
 *   node scripts/migrate.js status    # list applied vs pending
 */
import 'dotenv/config';
import fs from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import pg from 'pg';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const migrationsDir = path.resolve(__dirname, '..', 'migrations');

function databaseUrl() {
  const url = process.env.DATABASE_URL;
  if (!url) {
    console.error('[migrate] DATABASE_URL is required');
    process.exit(1);
  }
  return url;
}

async function readMigrations() {
  const files = (await fs.readdir(migrationsDir))
    .filter((f) => f.endsWith('.sql'))
    .sort();
  const out = [];
  for (const file of files) {
    const full = await fs.readFile(path.join(migrationsDir, file), 'utf8');
    // strip the down half if present, keep only the up section
    const downIdx = full.search(/^--\s*Down Migration/im);
    const up = downIdx === -1 ? full : full.slice(0, downIdx);
    out.push({ file, sql: up.trim() });
  }
  return out;
}

async function ensureMigrationsTable(client) {
  await client.query(`
    CREATE TABLE IF NOT EXISTS _migrations (
      file TEXT PRIMARY KEY,
      applied_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    )
  `);
}

async function up() {
  const pool = new pg.Pool({ connectionString: databaseUrl() });
  try {
    const client = await pool.connect();
    try {
      await ensureMigrationsTable(client);
      const applied = new Set(
        (await client.query('SELECT file FROM _migrations')).rows.map((r) => r.file),
      );
      const migrations = await readMigrations();
      const pending = migrations.filter((m) => !applied.has(m.file));
      if (pending.length === 0) {
        console.log('[migrate] no pending migrations');
        return;
      }
      for (const m of pending) {
        process.stdout.write(`[migrate] applying ${m.file} ... `);
        await client.query('BEGIN');
        try {
          await client.query(m.sql);
          await client.query('INSERT INTO _migrations(file) VALUES ($1)', [m.file]);
          await client.query('COMMIT');
          console.log('ok');
        } catch (err) {
          await client.query('ROLLBACK');
          console.log('FAILED');
          throw err;
        }
      }
    } finally {
      client.release();
    }
  } finally {
    await pool.end();
  }
}

async function status() {
  const pool = new pg.Pool({ connectionString: databaseUrl() });
  try {
    const client = await pool.connect();
    try {
      await ensureMigrationsTable(client);
      const applied = new Set(
        (await client.query('SELECT file FROM _migrations')).rows.map((r) => r.file),
      );
      const migrations = await readMigrations();
      for (const m of migrations) {
        console.log(`${applied.has(m.file) ? '✓' : ' '} ${m.file}`);
      }
    } finally {
      client.release();
    }
  } finally {
    await pool.end();
  }
}

const cmd = process.argv[2] ?? 'up';
const action = { up, status }[cmd];
if (!action) {
  console.error(`[migrate] unknown command: ${cmd}`);
  process.exit(2);
}
action().catch((err) => {
  const msg = err?.message || err?.code || String(err);
  console.error(`[migrate] ${msg}`);
  if (err?.code === 'ECONNREFUSED') {
    console.error('[migrate] hint: is PostgreSQL running and DATABASE_URL correct?');
  }
  process.exit(1);
});
