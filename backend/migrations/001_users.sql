-- Up Migration
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  username TEXT NOT NULL UNIQUE,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  fcm_token TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Down Migration
-- DROP TABLE users;
