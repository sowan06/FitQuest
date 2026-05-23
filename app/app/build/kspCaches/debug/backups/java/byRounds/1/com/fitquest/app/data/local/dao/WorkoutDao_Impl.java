package com.fitquest.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fitquest.app.data.local.entity.WorkoutSessionEntity;
import com.fitquest.app.data.local.entity.WorkoutSetEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutDao_Impl implements WorkoutDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutSessionEntity> __insertionAdapterOfWorkoutSessionEntity;

  private final EntityInsertionAdapter<WorkoutSetEntity> __insertionAdapterOfWorkoutSetEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public WorkoutDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutSessionEntity = new EntityInsertionAdapter<WorkoutSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workout_sessions` (`localId`,`serverId`,`notes`,`totalVolumeKg`,`xpEarned`,`loggedAt`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutSessionEntity entity) {
        statement.bindLong(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNotes());
        }
        statement.bindDouble(4, entity.getTotalVolumeKg());
        statement.bindLong(5, entity.getXpEarned());
        statement.bindLong(6, entity.getLoggedAt());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__insertionAdapterOfWorkoutSetEntity = new EntityInsertionAdapter<WorkoutSetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workout_sets` (`localId`,`localSessionId`,`exerciseName`,`setNumber`,`reps`,`weightKg`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutSetEntity entity) {
        statement.bindLong(1, entity.getLocalId());
        statement.bindLong(2, entity.getLocalSessionId());
        statement.bindString(3, entity.getExerciseName());
        statement.bindLong(4, entity.getSetNumber());
        statement.bindLong(5, entity.getReps());
        statement.bindDouble(6, entity.getWeightKg());
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE workout_sessions SET serverId = ?, isSynced = 1, xpEarned = ? WHERE localId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_sessions";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final WorkoutSessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutSessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSets(final List<WorkoutSetEntity> sets,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWorkoutSetEntity.insert(sets);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markSynced(final long localId, final String serverId, final int xpEarned,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, serverId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, xpEarned);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, localId);
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
          __preparedStmtOfMarkSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutSessionWithSets>> observeAll() {
    final String _sql = "SELECT * FROM workout_sessions ORDER BY loggedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"workout_sets",
        "workout_sessions"}, new Callable<List<WorkoutSessionWithSets>>() {
      @Override
      @NonNull
      public List<WorkoutSessionWithSets> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
            final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfTotalVolumeKg = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolumeKg");
            final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
            final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
            final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
            final LongSparseArray<ArrayList<WorkoutSetEntity>> _collectionSets = new LongSparseArray<ArrayList<WorkoutSetEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfLocalId);
              if (!_collectionSets.containsKey(_tmpKey)) {
                _collectionSets.put(_tmpKey, new ArrayList<WorkoutSetEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipworkoutSetsAscomFitquestAppDataLocalEntityWorkoutSetEntity(_collectionSets);
            final List<WorkoutSessionWithSets> _result = new ArrayList<WorkoutSessionWithSets>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WorkoutSessionWithSets _item;
              final WorkoutSessionEntity _tmpSession;
              final long _tmpLocalId;
              _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
              final String _tmpServerId;
              if (_cursor.isNull(_cursorIndexOfServerId)) {
                _tmpServerId = null;
              } else {
                _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
              }
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final double _tmpTotalVolumeKg;
              _tmpTotalVolumeKg = _cursor.getDouble(_cursorIndexOfTotalVolumeKg);
              final int _tmpXpEarned;
              _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
              final long _tmpLoggedAt;
              _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
              final boolean _tmpIsSynced;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
              _tmpIsSynced = _tmp != 0;
              _tmpSession = new WorkoutSessionEntity(_tmpLocalId,_tmpServerId,_tmpNotes,_tmpTotalVolumeKg,_tmpXpEarned,_tmpLoggedAt,_tmpIsSynced);
              final ArrayList<WorkoutSetEntity> _tmpSetsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfLocalId);
              _tmpSetsCollection = _collectionSets.get(_tmpKey_1);
              _item = new WorkoutSessionWithSets(_tmpSession,_tmpSetsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedSessions(
      final Continuation<? super List<WorkoutSessionEntity>> $completion) {
    final String _sql = "SELECT * FROM workout_sessions WHERE isSynced = 0 ORDER BY loggedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSessionEntity>>() {
      @Override
      @NonNull
      public List<WorkoutSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTotalVolumeKg = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolumeKg");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<WorkoutSessionEntity> _result = new ArrayList<WorkoutSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSessionEntity _item;
            final long _tmpLocalId;
            _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final double _tmpTotalVolumeKg;
            _tmpTotalVolumeKg = _cursor.getDouble(_cursorIndexOfTotalVolumeKg);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new WorkoutSessionEntity(_tmpLocalId,_tmpServerId,_tmpNotes,_tmpTotalVolumeKg,_tmpXpEarned,_tmpLoggedAt,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSetsForSession(final long localSessionId,
      final Continuation<? super List<WorkoutSetEntity>> $completion) {
    final String _sql = "SELECT * FROM workout_sets WHERE localSessionId = ? ORDER BY setNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, localSessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSetEntity>>() {
      @Override
      @NonNull
      public List<WorkoutSetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfLocalSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "localSessionId");
          final int _cursorIndexOfExerciseName = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseName");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final List<WorkoutSetEntity> _result = new ArrayList<WorkoutSetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSetEntity _item;
            final long _tmpLocalId;
            _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
            final long _tmpLocalSessionId;
            _tmpLocalSessionId = _cursor.getLong(_cursorIndexOfLocalSessionId);
            final String _tmpExerciseName;
            _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            _item = new WorkoutSetEntity(_tmpLocalId,_tmpLocalSessionId,_tmpExerciseName,_tmpSetNumber,_tmpReps,_tmpWeightKg);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailyVolume>> observeVolumeByDay(final long since) {
    final String _sql = "\n"
            + "        SELECT date(loggedAt / 1000, 'unixepoch') AS day,\n"
            + "               SUM(totalVolumeKg) AS volumeKg\n"
            + "          FROM workout_sessions\n"
            + "         WHERE loggedAt >= ?\n"
            + "         GROUP BY day\n"
            + "         ORDER BY day\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_sessions"}, new Callable<List<DailyVolume>>() {
      @Override
      @NonNull
      public List<DailyVolume> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDay = 0;
          final int _cursorIndexOfVolumeKg = 1;
          final List<DailyVolume> _result = new ArrayList<DailyVolume>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyVolume _item;
            final String _tmpDay;
            _tmpDay = _cursor.getString(_cursorIndexOfDay);
            final double _tmpVolumeKg;
            _tmpVolumeKg = _cursor.getDouble(_cursorIndexOfVolumeKg);
            _item = new DailyVolume(_tmpDay,_tmpVolumeKg);
            _result.add(_item);
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

  private void __fetchRelationshipworkoutSetsAscomFitquestAppDataLocalEntityWorkoutSetEntity(
      @NonNull final LongSparseArray<ArrayList<WorkoutSetEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipworkoutSetsAscomFitquestAppDataLocalEntityWorkoutSetEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `localId`,`localSessionId`,`exerciseName`,`setNumber`,`reps`,`weightKg` FROM `workout_sets` WHERE `localSessionId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "localSessionId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfLocalId = 0;
      final int _cursorIndexOfLocalSessionId = 1;
      final int _cursorIndexOfExerciseName = 2;
      final int _cursorIndexOfSetNumber = 3;
      final int _cursorIndexOfReps = 4;
      final int _cursorIndexOfWeightKg = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<WorkoutSetEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final WorkoutSetEntity _item_1;
          final long _tmpLocalId;
          _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
          final long _tmpLocalSessionId;
          _tmpLocalSessionId = _cursor.getLong(_cursorIndexOfLocalSessionId);
          final String _tmpExerciseName;
          _tmpExerciseName = _cursor.getString(_cursorIndexOfExerciseName);
          final int _tmpSetNumber;
          _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
          final int _tmpReps;
          _tmpReps = _cursor.getInt(_cursorIndexOfReps);
          final double _tmpWeightKg;
          _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
          _item_1 = new WorkoutSetEntity(_tmpLocalId,_tmpLocalSessionId,_tmpExerciseName,_tmpSetNumber,_tmpReps,_tmpWeightKg);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
