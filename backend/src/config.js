import 'dotenv/config';

function requireEnv(name, { minLength } = {}) {
  const value = process.env[name];
  if (value === undefined || value === null || value === '') {
    throw new Error(`[config] missing required env var: ${name}`);
  }
  if (minLength && value.length < minLength) {
    throw new Error(
      `[config] env var ${name} must be at least ${minLength} characters (got ${value.length})`,
    );
  }
  return value;
}

export const config = {
  databaseUrl: requireEnv('DATABASE_URL'),
  jwtSecret: requireEnv('JWT_SECRET', { minLength: 32 }),
  jwtExpiryH: Number(process.env.JWT_EXPIRY_HOURS ?? 168),
  port: Number(process.env.PORT ?? 8080),
  fcmServerKey: process.env.FCM_SERVER_KEY || null,
  nodeEnv: process.env.NODE_ENV ?? 'development',
};

if (!Number.isFinite(config.jwtExpiryH) || config.jwtExpiryH <= 0) {
  throw new Error('[config] JWT_EXPIRY_HOURS must be a positive number');
}

if (!Number.isFinite(config.port) || config.port <= 0) {
  throw new Error('[config] PORT must be a positive number');
}
