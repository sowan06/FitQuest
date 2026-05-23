-- Up Migration
CREATE TABLE characters (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  name TEXT NOT NULL,
  level INTEGER NOT NULL DEFAULT 1,
  xp INTEGER NOT NULL DEFAULT 0,
  xp_to_next INTEGER NOT NULL DEFAULT 100,
  avatar_stage INTEGER NOT NULL DEFAULT 0,
  stat_str INTEGER NOT NULL DEFAULT 1,
  stat_vit INTEGER NOT NULL DEFAULT 1,
  stat_end INTEGER NOT NULL DEFAULT 1,
  stat_wis INTEGER NOT NULL DEFAULT 1,
  stat_con INTEGER NOT NULL DEFAULT 1,
  streak_days INTEGER NOT NULL DEFAULT 0,
  last_activity_date DATE,
  total_volume_kg NUMERIC(14,2) NOT NULL DEFAULT 0,
  total_workouts INTEGER NOT NULL DEFAULT 0,
  protein_goal_days INTEGER NOT NULL DEFAULT 0,
  calorie_goal_days INTEGER NOT NULL DEFAULT 0,
  daily_calorie_goal INTEGER NOT NULL DEFAULT 2500,
  daily_protein_goal_g INTEGER NOT NULL DEFAULT 180,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_characters_user_id ON characters(user_id);

-- Down Migration
-- DROP TABLE characters;
