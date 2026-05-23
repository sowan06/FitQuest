-- Up Migration
CREATE TABLE quest_definitions (
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  xp_reward INTEGER NOT NULL,
  quest_type TEXT NOT NULL CHECK (quest_type IN ('daily','weekly','milestone')),
  target INTEGER NOT NULL DEFAULT 1
);

INSERT INTO quest_definitions (id, title, description, xp_reward, quest_type, target) VALUES
  ('daily_log_workout',   'Sweat It Out',     'Log 1 workout today',          30, 'daily',  1),
  ('daily_log_all_meals', 'Fuel the Hero',    'Log 3 meals today',            20, 'daily',  3),
  ('daily_hit_protein',   'Protein Champion', 'Hit your protein goal today',  25, 'daily',  1),
  ('weekly_5_workouts',   'Iron Will',        'Log 5 workouts this week',    150, 'weekly', 5),
  ('weekly_7_day_log',    'Consistent Hero',  'Log food 7 days this week',   100, 'weekly', 7);

CREATE TABLE user_quests (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  quest_id TEXT NOT NULL REFERENCES quest_definitions(id),
  status TEXT NOT NULL DEFAULT 'active' CHECK (status IN ('active','completed','claimed')),
  progress INTEGER NOT NULL DEFAULT 0,
  target INTEGER NOT NULL DEFAULT 1,
  assigned_date DATE NOT NULL DEFAULT CURRENT_DATE,
  completed_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX idx_user_quests_unique ON user_quests(user_id, quest_id, assigned_date);
CREATE INDEX idx_user_quests_user_status ON user_quests(user_id, status);

-- Down Migration
-- DROP TABLE user_quests;
-- DROP TABLE quest_definitions;
