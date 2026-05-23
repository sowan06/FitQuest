package com.fitquest.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fitquest.app.data.local.entity.CharacterEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CharacterDao_Impl implements CharacterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CharacterEntity> __insertionAdapterOfCharacterEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public CharacterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCharacterEntity = new EntityInsertionAdapter<CharacterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `characters` (`userId`,`name`,`level`,`xp`,`xpToNext`,`xpProgressPct`,`avatarStage`,`avatarLabel`,`statStr`,`statVit`,`statEnd`,`statWis`,`statCon`,`streakDays`,`totalVolumeKg`,`totalWorkouts`,`dailyCalorieGoal`,`dailyProteinGoalG`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CharacterEntity entity) {
        statement.bindString(1, entity.getUserId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getLevel());
        statement.bindLong(4, entity.getXp());
        statement.bindLong(5, entity.getXpToNext());
        statement.bindDouble(6, entity.getXpProgressPct());
        statement.bindLong(7, entity.getAvatarStage());
        statement.bindString(8, entity.getAvatarLabel());
        statement.bindLong(9, entity.getStatStr());
        statement.bindLong(10, entity.getStatVit());
        statement.bindLong(11, entity.getStatEnd());
        statement.bindLong(12, entity.getStatWis());
        statement.bindLong(13, entity.getStatCon());
        statement.bindLong(14, entity.getStreakDays());
        statement.bindDouble(15, entity.getTotalVolumeKg());
        statement.bindLong(16, entity.getTotalWorkouts());
        statement.bindLong(17, entity.getDailyCalorieGoal());
        statement.bindLong(18, entity.getDailyProteinGoalG());
        statement.bindLong(19, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM characters";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final CharacterEntity character,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCharacterEntity.insert(character);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<CharacterEntity> observe(final String userId) {
    final String _sql = "SELECT * FROM characters WHERE userId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"characters"}, new Callable<CharacterEntity>() {
      @Override
      @Nullable
      public CharacterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfXpToNext = CursorUtil.getColumnIndexOrThrow(_cursor, "xpToNext");
          final int _cursorIndexOfXpProgressPct = CursorUtil.getColumnIndexOrThrow(_cursor, "xpProgressPct");
          final int _cursorIndexOfAvatarStage = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarStage");
          final int _cursorIndexOfAvatarLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarLabel");
          final int _cursorIndexOfStatStr = CursorUtil.getColumnIndexOrThrow(_cursor, "statStr");
          final int _cursorIndexOfStatVit = CursorUtil.getColumnIndexOrThrow(_cursor, "statVit");
          final int _cursorIndexOfStatEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "statEnd");
          final int _cursorIndexOfStatWis = CursorUtil.getColumnIndexOrThrow(_cursor, "statWis");
          final int _cursorIndexOfStatCon = CursorUtil.getColumnIndexOrThrow(_cursor, "statCon");
          final int _cursorIndexOfStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "streakDays");
          final int _cursorIndexOfTotalVolumeKg = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolumeKg");
          final int _cursorIndexOfTotalWorkouts = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWorkouts");
          final int _cursorIndexOfDailyCalorieGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyCalorieGoal");
          final int _cursorIndexOfDailyProteinGoalG = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyProteinGoalG");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CharacterEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpXpToNext;
            _tmpXpToNext = _cursor.getInt(_cursorIndexOfXpToNext);
            final float _tmpXpProgressPct;
            _tmpXpProgressPct = _cursor.getFloat(_cursorIndexOfXpProgressPct);
            final int _tmpAvatarStage;
            _tmpAvatarStage = _cursor.getInt(_cursorIndexOfAvatarStage);
            final String _tmpAvatarLabel;
            _tmpAvatarLabel = _cursor.getString(_cursorIndexOfAvatarLabel);
            final int _tmpStatStr;
            _tmpStatStr = _cursor.getInt(_cursorIndexOfStatStr);
            final int _tmpStatVit;
            _tmpStatVit = _cursor.getInt(_cursorIndexOfStatVit);
            final int _tmpStatEnd;
            _tmpStatEnd = _cursor.getInt(_cursorIndexOfStatEnd);
            final int _tmpStatWis;
            _tmpStatWis = _cursor.getInt(_cursorIndexOfStatWis);
            final int _tmpStatCon;
            _tmpStatCon = _cursor.getInt(_cursorIndexOfStatCon);
            final int _tmpStreakDays;
            _tmpStreakDays = _cursor.getInt(_cursorIndexOfStreakDays);
            final double _tmpTotalVolumeKg;
            _tmpTotalVolumeKg = _cursor.getDouble(_cursorIndexOfTotalVolumeKg);
            final int _tmpTotalWorkouts;
            _tmpTotalWorkouts = _cursor.getInt(_cursorIndexOfTotalWorkouts);
            final int _tmpDailyCalorieGoal;
            _tmpDailyCalorieGoal = _cursor.getInt(_cursorIndexOfDailyCalorieGoal);
            final int _tmpDailyProteinGoalG;
            _tmpDailyProteinGoalG = _cursor.getInt(_cursorIndexOfDailyProteinGoalG);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CharacterEntity(_tmpUserId,_tmpName,_tmpLevel,_tmpXp,_tmpXpToNext,_tmpXpProgressPct,_tmpAvatarStage,_tmpAvatarLabel,_tmpStatStr,_tmpStatVit,_tmpStatEnd,_tmpStatWis,_tmpStatCon,_tmpStreakDays,_tmpTotalVolumeKg,_tmpTotalWorkouts,_tmpDailyCalorieGoal,_tmpDailyProteinGoalG,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<CharacterEntity> observeAny() {
    final String _sql = "SELECT * FROM characters LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"characters"}, new Callable<CharacterEntity>() {
      @Override
      @Nullable
      public CharacterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfXp = CursorUtil.getColumnIndexOrThrow(_cursor, "xp");
          final int _cursorIndexOfXpToNext = CursorUtil.getColumnIndexOrThrow(_cursor, "xpToNext");
          final int _cursorIndexOfXpProgressPct = CursorUtil.getColumnIndexOrThrow(_cursor, "xpProgressPct");
          final int _cursorIndexOfAvatarStage = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarStage");
          final int _cursorIndexOfAvatarLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "avatarLabel");
          final int _cursorIndexOfStatStr = CursorUtil.getColumnIndexOrThrow(_cursor, "statStr");
          final int _cursorIndexOfStatVit = CursorUtil.getColumnIndexOrThrow(_cursor, "statVit");
          final int _cursorIndexOfStatEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "statEnd");
          final int _cursorIndexOfStatWis = CursorUtil.getColumnIndexOrThrow(_cursor, "statWis");
          final int _cursorIndexOfStatCon = CursorUtil.getColumnIndexOrThrow(_cursor, "statCon");
          final int _cursorIndexOfStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "streakDays");
          final int _cursorIndexOfTotalVolumeKg = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolumeKg");
          final int _cursorIndexOfTotalWorkouts = CursorUtil.getColumnIndexOrThrow(_cursor, "totalWorkouts");
          final int _cursorIndexOfDailyCalorieGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyCalorieGoal");
          final int _cursorIndexOfDailyProteinGoalG = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyProteinGoalG");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CharacterEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpLevel;
            _tmpLevel = _cursor.getInt(_cursorIndexOfLevel);
            final int _tmpXp;
            _tmpXp = _cursor.getInt(_cursorIndexOfXp);
            final int _tmpXpToNext;
            _tmpXpToNext = _cursor.getInt(_cursorIndexOfXpToNext);
            final float _tmpXpProgressPct;
            _tmpXpProgressPct = _cursor.getFloat(_cursorIndexOfXpProgressPct);
            final int _tmpAvatarStage;
            _tmpAvatarStage = _cursor.getInt(_cursorIndexOfAvatarStage);
            final String _tmpAvatarLabel;
            _tmpAvatarLabel = _cursor.getString(_cursorIndexOfAvatarLabel);
            final int _tmpStatStr;
            _tmpStatStr = _cursor.getInt(_cursorIndexOfStatStr);
            final int _tmpStatVit;
            _tmpStatVit = _cursor.getInt(_cursorIndexOfStatVit);
            final int _tmpStatEnd;
            _tmpStatEnd = _cursor.getInt(_cursorIndexOfStatEnd);
            final int _tmpStatWis;
            _tmpStatWis = _cursor.getInt(_cursorIndexOfStatWis);
            final int _tmpStatCon;
            _tmpStatCon = _cursor.getInt(_cursorIndexOfStatCon);
            final int _tmpStreakDays;
            _tmpStreakDays = _cursor.getInt(_cursorIndexOfStreakDays);
            final double _tmpTotalVolumeKg;
            _tmpTotalVolumeKg = _cursor.getDouble(_cursorIndexOfTotalVolumeKg);
            final int _tmpTotalWorkouts;
            _tmpTotalWorkouts = _cursor.getInt(_cursorIndexOfTotalWorkouts);
            final int _tmpDailyCalorieGoal;
            _tmpDailyCalorieGoal = _cursor.getInt(_cursorIndexOfDailyCalorieGoal);
            final int _tmpDailyProteinGoalG;
            _tmpDailyProteinGoalG = _cursor.getInt(_cursorIndexOfDailyProteinGoalG);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CharacterEntity(_tmpUserId,_tmpName,_tmpLevel,_tmpXp,_tmpXpToNext,_tmpXpProgressPct,_tmpAvatarStage,_tmpAvatarLabel,_tmpStatStr,_tmpStatVit,_tmpStatEnd,_tmpStatWis,_tmpStatCon,_tmpStreakDays,_tmpTotalVolumeKg,_tmpTotalWorkouts,_tmpDailyCalorieGoal,_tmpDailyProteinGoalG,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
