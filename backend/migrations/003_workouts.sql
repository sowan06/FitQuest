-- Up Migration
CREATE TABLE workout_sessions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  logged_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  notes TEXT,
  total_volume_kg NUMERIC(10,2) NOT NULL DEFAULT 0,
  xp_earned INTEGER NOT NULL DEFAULT 0,
  synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE workout_sets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id UUID NOT NULL REFERENCES workout_sessions(id) ON DELETE CASCADE,
  exercise_name TEXT NOT NULL,
  set_number INTEGER NOT NULL,
  reps INTEGER NOT NULL,
  weight_kg NUMERIC(6,2) NOT NULL,
  xp_earned INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_workout_sessions_user_id ON workout_sessions(user_id);
CREATE INDEX idx_workout_sessions_logged_at ON workout_sessions(logged_at);
CREATE INDEX idx_workout_sets_session_id ON workout_sets(session_id);

-- Down Migration
-- DROP TABLE workout_sets;
-- DROP TABLE workout_sessions;
