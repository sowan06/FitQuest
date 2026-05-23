package com.fitquest.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.fitquest.app.data.local.dao.CharacterDao;
import com.fitquest.app.data.local.dao.CharacterDao_Impl;
import com.fitquest.app.data.local.dao.FoodDao;
import com.fitquest.app.data.local.dao.FoodDao_Impl;
import com.fitquest.app.data.local.dao.QuestDao;
import com.fitquest.app.data.local.dao.QuestDao_Impl;
import com.fitquest.app.data.local.dao.WorkoutDao;
import com.fitquest.app.data.local.dao.WorkoutDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FitQuestDatabase_Impl extends FitQuestDatabase {
  private volatile CharacterDao _characterDao;

  private volatile WorkoutDao _workoutDao;

  private volatile FoodDao _foodDao;

  private volatile QuestDao _questDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `characters` (`userId` TEXT NOT NULL, `name` TEXT NOT NULL, `level` INTEGER NOT NULL, `xp` INTEGER NOT NULL, `xpToNext` INTEGER NOT NULL, `xpProgressPct` REAL NOT NULL, `avatarStage` INTEGER NOT NULL, `avatarLabel` TEXT NOT NULL, `statStr` INTEGER NOT NULL, `statVit` INTEGER NOT NULL, `statEnd` INTEGER NOT NULL, `statWis` INTEGER NOT NULL, `statCon` INTEGER NOT NULL, `streakDays` INTEGER NOT NULL, `totalVolumeKg` REAL NOT NULL, `totalWorkouts` INTEGER NOT NULL, `dailyCalorieGoal` INTEGER NOT NULL, `dailyProteinGoalG` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_sessions` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serverId` TEXT, `notes` TEXT, `totalVolumeKg` REAL NOT NULL, `xpEarned` INTEGER NOT NULL, `loggedAt` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_sets` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `localSessionId` INTEGER NOT NULL, `exerciseName` TEXT NOT NULL, `setNumber` INTEGER NOT NULL, `reps` INTEGER NOT NULL, `weightKg` REAL NOT NULL, FOREIGN KEY(`localSessionId`) REFERENCES `workout_sessions`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_workout_sets_localSessionId` ON `workout_sets` (`localSessionId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `food_logs` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serverId` TEXT, `foodName` TEXT NOT NULL, `calories` REAL NOT NULL, `proteinG` REAL NOT NULL, `carbsG` REAL NOT NULL, `fatG` REAL NOT NULL, `servingG` REAL, `mealType` TEXT NOT NULL, `xpEarned` INTEGER NOT NULL, `loggedAt` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quests` (`questId` TEXT NOT NULL, `assignedDate` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `xpReward` INTEGER NOT NULL, `questType` TEXT NOT NULL, `status` TEXT NOT NULL, `progress` INTEGER NOT NULL, `target` INTEGER NOT NULL, `cachedAt` INTEGER NOT NULL, PRIMARY KEY(`questId`, `assignedDate`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '442fcdb4aaaf2d123178ea4ee7b7d627')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `characters`");
        db.execSQL("DROP TABLE IF EXISTS `workout_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `workout_sets`");
        db.execSQL("DROP TABLE IF EXISTS `food_logs`");
        db.execSQL("DROP TABLE IF EXISTS `quests`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCharacters = new HashMap<String, TableInfo.Column>(19);
        _columnsCharacters.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("level", new TableInfo.Column("level", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("xp", new TableInfo.Column("xp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("xpToNext", new TableInfo.Column("xpToNext", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("xpProgressPct", new TableInfo.Column("xpProgressPct", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("avatarStage", new TableInfo.Column("avatarStage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("avatarLabel", new TableInfo.Column("avatarLabel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("statStr", new TableInfo.Column("statStr", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("statVit", new TableInfo.Column("statVit", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("statEnd", new TableInfo.Column("statEnd", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("statWis", new TableInfo.Column("statWis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("statCon", new TableInfo.Column("statCon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("streakDays", new TableInfo.Column("streakDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("totalVolumeKg", new TableInfo.Column("totalVolumeKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("totalWorkouts", new TableInfo.Column("totalWorkouts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("dailyCalorieGoal", new TableInfo.Column("dailyCalorieGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("dailyProteinGoalG", new TableInfo.Column("dailyProteinGoalG", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacters.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharacters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharacters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharacters = new TableInfo("characters", _columnsCharacters, _foreignKeysCharacters, _indicesCharacters);
        final TableInfo _existingCharacters = TableInfo.read(db, "characters");
        if (!_infoCharacters.equals(_existingCharacters)) {
          return new RoomOpenHelper.ValidationResult(false, "characters(com.fitquest.app.data.local.entity.CharacterEntity).\n"
                  + " Expected:\n" + _infoCharacters + "\n"
                  + " Found:\n" + _existingCharacters);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutSessions = new HashMap<String, TableInfo.Column>(7);
        _columnsWorkoutSessions.put("localId", new TableInfo.Column("localId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("totalVolumeKg", new TableInfo.Column("totalVolumeKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("xpEarned", new TableInfo.Column("xpEarned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("loggedAt", new TableInfo.Column("loggedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSessions.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkoutSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkoutSessions = new TableInfo("workout_sessions", _columnsWorkoutSessions, _foreignKeysWorkoutSessions, _indicesWorkoutSessions);
        final TableInfo _existingWorkoutSessions = TableInfo.read(db, "workout_sessions");
        if (!_infoWorkoutSessions.equals(_existingWorkoutSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_sessions(com.fitquest.app.data.local.entity.WorkoutSessionEntity).\n"
                  + " Expected:\n" + _infoWorkoutSessions + "\n"
                  + " Found:\n" + _existingWorkoutSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutSets = new HashMap<String, TableInfo.Column>(6);
        _columnsWorkoutSets.put("localId", new TableInfo.Column("localId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSets.put("localSessionId", new TableInfo.Column("localSessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSets.put("exerciseName", new TableInfo.Column("exerciseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSets.put("setNumber", new TableInfo.Column("setNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSets.put("reps", new TableInfo.Column("reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutSets.put("weightKg", new TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutSets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysWorkoutSets.add(new TableInfo.ForeignKey("workout_sessions", "CASCADE", "NO ACTION", Arrays.asList("localSessionId"), Arrays.asList("localId")));
        final HashSet<TableInfo.Index> _indicesWorkoutSets = new HashSet<TableInfo.Index>(1);
        _indicesWorkoutSets.add(new TableInfo.Index("index_workout_sets_localSessionId", false, Arrays.asList("localSessionId"), Arrays.asList("ASC")));
        final TableInfo _infoWorkoutSets = new TableInfo("workout_sets", _columnsWorkoutSets, _foreignKeysWorkoutSets, _indicesWorkoutSets);
        final TableInfo _existingWorkoutSets = TableInfo.read(db, "workout_sets");
        if (!_infoWorkoutSets.equals(_existingWorkoutSets)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_sets(com.fitquest.app.data.local.entity.WorkoutSetEntity).\n"
                  + " Expected:\n" + _infoWorkoutSets + "\n"
                  + " Found:\n" + _existingWorkoutSets);
        }
        final HashMap<String, TableInfo.Column> _columnsFoodLogs = new HashMap<String, TableInfo.Column>(12);
        _columnsFoodLogs.put("localId", new TableInfo.Column("localId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("serverId", new TableInfo.Column("serverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("foodName", new TableInfo.Column("foodName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("calories", new TableInfo.Column("calories", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("proteinG", new TableInfo.Column("proteinG", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("carbsG", new TableInfo.Column("carbsG", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("fatG", new TableInfo.Column("fatG", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("servingG", new TableInfo.Column("servingG", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("mealType", new TableInfo.Column("mealType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("xpEarned", new TableInfo.Column("xpEarned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("loggedAt", new TableInfo.Column("loggedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodLogs.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFoodLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFoodLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoodLogs = new TableInfo("food_logs", _columnsFoodLogs, _foreignKeysFoodLogs, _indicesFoodLogs);
        final TableInfo _existingFoodLogs = TableInfo.read(db, "food_logs");
        if (!_infoFoodLogs.equals(_existingFoodLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "food_logs(com.fitquest.app.data.local.entity.FoodEntity).\n"
                  + " Expected:\n" + _infoFoodLogs + "\n"
                  + " Found:\n" + _existingFoodLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsQuests = new HashMap<String, TableInfo.Column>(10);
        _columnsQuests.put("questId", new TableInfo.Column("questId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("assignedDate", new TableInfo.Column("assignedDate", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("xpReward", new TableInfo.Column("xpReward", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("questType", new TableInfo.Column("questType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("target", new TableInfo.Column("target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuests.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuests = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuests = new TableInfo("quests", _columnsQuests, _foreignKeysQuests, _indicesQuests);
        final TableInfo _existingQuests = TableInfo.read(db, "quests");
        if (!_infoQuests.equals(_existingQuests)) {
          return new RoomOpenHelper.ValidationResult(false, "quests(com.fitquest.app.data.local.entity.QuestEntity).\n"
                  + " Expected:\n" + _infoQuests + "\n"
                  + " Found:\n" + _existingQuests);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "442fcdb4aaaf2d123178ea4ee7b7d627", "03c667359f33b2bbc851ff353e1d3743");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "characters","workout_sessions","workout_sets","food_logs","quests");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `characters`");
      _db.execSQL("DELETE FROM `workout_sessions`");
      _db.execSQL("DELETE FROM `workout_sets`");
      _db.execSQL("DELETE FROM `food_logs`");
      _db.execSQL("DELETE FROM `quests`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CharacterDao.class, CharacterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutDao.class, WorkoutDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodDao.class, FoodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuestDao.class, QuestDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CharacterDao characterDao() {
    if (_characterDao != null) {
      return _characterDao;
    } else {
      synchronized(this) {
        if(_characterDao == null) {
          _characterDao = new CharacterDao_Impl(this);
        }
        return _characterDao;
      }
    }
  }

  @Override
  public WorkoutDao workoutDao() {
    if (_workoutDao != null) {
      return _workoutDao;
    } else {
      synchronized(this) {
        if(_workoutDao == null) {
          _workoutDao = new WorkoutDao_Impl(this);
        }
        return _workoutDao;
      }
    }
  }

  @Override
  public FoodDao foodDao() {
    if (_foodDao != null) {
      return _foodDao;
    } else {
      synchronized(this) {
        if(_foodDao == null) {
          _foodDao = new FoodDao_Impl(this);
        }
        return _foodDao;
      }
    }
  }

  @Override
  public QuestDao questDao() {
    if (_questDao != null) {
      return _questDao;
    } else {
      synchronized(this) {
        if(_questDao == null) {
          _questDao = new QuestDao_Impl(this);
        }
        return _questDao;
      }
    }
  }
}
