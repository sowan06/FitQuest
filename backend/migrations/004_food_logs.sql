-- Up Migration
CREATE TABLE food_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  food_name TEXT NOT NULL,
  calories NUMERIC(7,2) NOT NULL,
  protein_g NUMERIC(6,2) NOT NULL,
  carbs_g NUMERIC(6,2) NOT NULL,
  fat_g NUMERIC(6,2) NOT NULL,
  serving_g NUMERIC(6,2),
  meal_type TEXT NOT NULL CHECK (meal_type IN ('breakfast','lunch','dinner','snack')),
  logged_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  xp_earned INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX idx_food_logs_user_id ON food_logs(user_id);
CREATE INDEX idx_food_logs_logged_at ON food_logs(logged_at);

-- Down Migration
-- DROP TABLE food_logs;
