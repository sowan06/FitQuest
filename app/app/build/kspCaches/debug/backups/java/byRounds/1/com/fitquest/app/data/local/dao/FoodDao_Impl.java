package com.fitquest.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fitquest.app.data.local.entity.FoodEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class FoodDao_Impl implements FoodDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FoodEntity> __insertionAdapterOfFoodEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public FoodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFoodEntity = new EntityInsertionAdapter<FoodEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `food_logs` (`localId`,`serverId`,`foodName`,`calories`,`proteinG`,`carbsG`,`fatG`,`servingG`,`mealType`,`xpEarned`,`loggedAt`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodEntity entity) {
        statement.bindLong(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getServerId());
        }
        statement.bindString(3, entity.getFoodName());
        statement.bindDouble(4, entity.getCalories());
        statement.bindDouble(5, entity.getProteinG());
        statement.bindDouble(6, entity.getCarbsG());
        statement.bindDouble(7, entity.getFatG());
        if (entity.getServingG() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getServingG());
        }
        statement.bindString(9, entity.getMealType());
        statement.bindLong(10, entity.getXpEarned());
        statement.bindLong(11, entity.getLoggedAt());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(12, _tmp);
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE food_logs SET serverId = ?, isSynced = 1, xpEarned = ? WHERE localId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM food_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FoodEntity food, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFoodEntity.insertAndReturnId(food);
          __db.setTransactionSuccessful();
          return _result;
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
  public Flow<List<FoodEntity>> observeToday() {
    final String _sql = "\n"
            + "        SELECT * FROM food_logs\n"
            + "         WHERE date(loggedAt / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')\n"
            + "         ORDER BY loggedAt\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_logs"}, new Callable<List<FoodEntity>>() {
      @Override
      @NonNull
      public List<FoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProteinG = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinG");
          final int _cursorIndexOfCarbsG = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsG");
          final int _cursorIndexOfFatG = CursorUtil.getColumnIndexOrThrow(_cursor, "fatG");
          final int _cursorIndexOfServingG = CursorUtil.getColumnIndexOrThrow(_cursor, "servingG");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<FoodEntity> _result = new ArrayList<FoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodEntity _item;
            final long _tmpLocalId;
            _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            final double _tmpProteinG;
            _tmpProteinG = _cursor.getDouble(_cursorIndexOfProteinG);
            final double _tmpCarbsG;
            _tmpCarbsG = _cursor.getDouble(_cursorIndexOfCarbsG);
            final double _tmpFatG;
            _tmpFatG = _cursor.getDouble(_cursorIndexOfFatG);
            final Double _tmpServingG;
            if (_cursor.isNull(_cursorIndexOfServingG)) {
              _tmpServingG = null;
            } else {
              _tmpServingG = _cursor.getDouble(_cursorIndexOfServingG);
            }
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new FoodEntity(_tmpLocalId,_tmpServerId,_tmpFoodName,_tmpCalories,_tmpProteinG,_tmpCarbsG,_tmpFatG,_tmpServingG,_tmpMealType,_tmpXpEarned,_tmpLoggedAt,_tmpIsSynced);
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

  @Override
  public Flow<List<FoodEntity>> observeAll(final int limit, final int offset) {
    final String _sql = "SELECT * FROM food_logs ORDER BY loggedAt DESC LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 2;
    _statement.bindLong(_argIndex, offset);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_logs"}, new Callable<List<FoodEntity>>() {
      @Override
      @NonNull
      public List<FoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProteinG = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinG");
          final int _cursorIndexOfCarbsG = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsG");
          final int _cursorIndexOfFatG = CursorUtil.getColumnIndexOrThrow(_cursor, "fatG");
          final int _cursorIndexOfServingG = CursorUtil.getColumnIndexOrThrow(_cursor, "servingG");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<FoodEntity> _result = new ArrayList<FoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodEntity _item;
            final long _tmpLocalId;
            _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            final double _tmpProteinG;
            _tmpProteinG = _cursor.getDouble(_cursorIndexOfProteinG);
            final double _tmpCarbsG;
            _tmpCarbsG = _cursor.getDouble(_cursorIndexOfCarbsG);
            final double _tmpFatG;
            _tmpFatG = _cursor.getDouble(_cursorIndexOfFatG);
            final Double _tmpServingG;
            if (_cursor.isNull(_cursorIndexOfServingG)) {
              _tmpServingG = null;
            } else {
              _tmpServingG = _cursor.getDouble(_cursorIndexOfServingG);
            }
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new FoodEntity(_tmpLocalId,_tmpServerId,_tmpFoodName,_tmpCalories,_tmpProteinG,_tmpCarbsG,_tmpFatG,_tmpServingG,_tmpMealType,_tmpXpEarned,_tmpLoggedAt,_tmpIsSynced);
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

  @Override
  public Object getUnsynced(final Continuation<? super List<FoodEntity>> $completion) {
    final String _sql = "SELECT * FROM food_logs WHERE isSynced = 0 ORDER BY loggedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FoodEntity>>() {
      @Override
      @NonNull
      public List<FoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProteinG = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinG");
          final int _cursorIndexOfCarbsG = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsG");
          final int _cursorIndexOfFatG = CursorUtil.getColumnIndexOrThrow(_cursor, "fatG");
          final int _cursorIndexOfServingG = CursorUtil.getColumnIndexOrThrow(_cursor, "servingG");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfXpEarned = CursorUtil.getColumnIndexOrThrow(_cursor, "xpEarned");
          final int _cursorIndexOfLoggedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "loggedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<FoodEntity> _result = new ArrayList<FoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodEntity _item;
            final long _tmpLocalId;
            _tmpLocalId = _cursor.getLong(_cursorIndexOfLocalId);
            final String _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getString(_cursorIndexOfServerId);
            }
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final double _tmpCalories;
            _tmpCalories = _cursor.getDouble(_cursorIndexOfCalories);
            final double _tmpProteinG;
            _tmpProteinG = _cursor.getDouble(_cursorIndexOfProteinG);
            final double _tmpCarbsG;
            _tmpCarbsG = _cursor.getDouble(_cursorIndexOfCarbsG);
            final double _tmpFatG;
            _tmpFatG = _cursor.getDouble(_cursorIndexOfFatG);
            final Double _tmpServingG;
            if (_cursor.isNull(_cursorIndexOfServingG)) {
              _tmpServingG = null;
            } else {
              _tmpServingG = _cursor.getDouble(_cursorIndexOfServingG);
            }
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final int _tmpXpEarned;
            _tmpXpEarned = _cursor.getInt(_cursorIndexOfXpEarned);
            final long _tmpLoggedAt;
            _tmpLoggedAt = _cursor.getLong(_cursorIndexOfLoggedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new FoodEntity(_tmpLocalId,_tmpServerId,_tmpFoodName,_tmpCalories,_tmpProteinG,_tmpCarbsG,_tmpFatG,_tmpServingG,_tmpMealType,_tmpXpEarned,_tmpLoggedAt,_tmpIsSynced);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
