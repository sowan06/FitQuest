import jwt from 'jsonwebtoken';
import { config } from '../config.js';
import { HttpError } from './errorHandler.js';

export function signToken(userId) {
  return jwt.sign({ sub: userId }, config.jwtSecret, {
    expiresIn: `${config.jwtExpiryH}h`,
  });
}

export function authRequired(req, _res, next) {
  const header = req.header('authorization') || req.header('Authorization');
  if (!header || !header.toLowerCase().startsWith('bearer ')) {
    return next(new HttpError(401, 'missing_token'));
  }
  const token = header.slice(7).trim();
  try {
    const payload = jwt.verify(token, config.jwtSecret);
    req.userId = payload.sub;
    next();
  } catch (_err) {
    next(new HttpError(401, 'invalid_token'));
  }
}
