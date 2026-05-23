-- Up Migration
CREATE TABLE achievement_definitions (
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  xp_reward INTEGER NOT NULL,
  icon TEXT NOT NULL
);

INSERT INTO achievement_definitions (id, title, description, xp_reward, icon) VALUES
  ('first_workout',     'First Blood',    'Log your first workout',  50,  'sword'),
  ('streak_7',          'Week Warrior',   'Reach a 7-day streak',    75,  'fire'),
  ('streak_30',         'Unstoppable',    'Reach a 30-day streak',  200,  'crown'),
  ('level_5',           'Fighter Rank',   'Reach level 5',           50,  'shield'),
  ('level_15',          'Warrior Rank',   'Reach level 15',         100,  'armor'),
  ('total_volume_100k', 'Tonnage Master', 'Lift 100,000 kg total',  150,  'dumbbell');

CREATE TABLE user_achievements (
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  achievement_id TEXT NOT NULL REFERENCES achievement_definitions(id),
  unlocked_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  PRIMARY KEY (user_id, achievement_id)
);

-- Down Migration
-- DROP TABLE user_achievements;
-- DROP TABLE achievement_definitions;
