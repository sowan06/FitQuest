import { Router } from 'express';
import bcrypt from 'bcryptjs';
import { withTransaction, query } from '../db.js';
import { authRequired, signToken } from '../middleware/auth.js';
import { HttpError } from '../middleware/errorHandler.js';

const router = Router();

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const USERNAME_RE = /^[a-zA-Z0-9_]{3,32}$/;

router.post('/register', async (req, res, next) => {
  try {
    const { username, email, password } = req.body || {};
    if (!username || !USERNAME_RE.test(username)) {
      throw new HttpError(400, 'invalid_username');
    }
    if (!email || !EMAIL_RE.test(email)) {
      throw new HttpError(400, 'invalid_email');
    }
    if (!password || password.length < 8) {
      throw new HttpError(400, 'weak_password');
    }

    const passwordHash = await bcrypt.hash(password, 12);

    const userId = await withTransaction(async (client) => {
      const existing = await client.query(
        'SELECT 1 FROM users WHERE email = $1 OR username = $2',
        [email, username],
      );
      if (existing.rowCount > 0) throw new HttpError(409, 'user_exists');

      const { rows: userRows } = await client.query(
        `INSERT INTO users (username, email, password_hash)
         VALUES ($1, $2, $3) RETURNING id`,
        [username, email, passwordHash],
      );
      const id = userRows[0].id;

      await client.query(
        `INSERT INTO characters (user_id, name) VALUES ($1, $2)`,
        [id, username],
      );
      return id;
    });

    res.status(201).json({ token: signToken(userId) });
  } catch (err) {
    next(err);
  }
});

router.post('/login', async (req, res, next) => {
  try {
    const { email, password } = req.body || {};
    if (!email || !password) throw new HttpError(400, 'missing_credentials');

    const { rows } = await query(
      'SELECT id, password_hash FROM users WHERE email = $1',
      [email],
    );
    if (rows.length === 0) throw new HttpError(401, 'invalid_credentials');
    const ok = await bcrypt.compare(password, rows[0].password_hash);
    if (!ok) throw new HttpError(401, 'invalid_credentials');

    res.json({ token: signToken(rows[0].id) });
  } catch (err) {
    next(err);
  }
});

router.get('/me', authRequired, async (req, res, next) => {
  try {
    const { rows } = await query(
      'SELECT id, username, email, created_at FROM users WHERE id = $1',
      [req.userId],
    );
    if (rows.length === 0) throw new HttpError(404, 'user_not_found');
    res.json(rows[0]);
  } catch (err) {
    next(err);
  }
});

router.patch('/fcm-token', authRequired, async (req, res, next) => {
  try {
    const { fcm_token } = req.body || {};
    if (typeof fcm_token !== 'string' || fcm_token.length < 8) {
      throw new HttpError(400, 'invalid_fcm_token');
    }
    await query('UPDATE users SET fcm_token = $1 WHERE id = $2', [fcm_token, req.userId]);
    res.json({ ok: true });
  } catch (err) {
    next(err);
  }
});

export default router;
