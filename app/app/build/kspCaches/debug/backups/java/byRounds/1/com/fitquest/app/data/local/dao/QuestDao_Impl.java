package com.fitquest.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.fitquest.app.data.local.entity.QuestEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class QuestDao_Impl implements QuestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuestEntity> __insertionAdapterOfQuestEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public QuestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuestEntity = new EntityInsertionAdapter<QuestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quests` (`questId`,`assignedDate`,`title`,`description`,`xpReward`,`questType`,`status`,`progress`,`target`,`cachedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuestEntity entity) {
        statement.bindString(1, entity.getQuestId());
        statement.bindString(2, entity.getAssignedDate());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getDescription());
        statement.bindLong(5, entity.getXpReward());
        statement.bindString(6, entity.getQuestType());
        statement.bindString(7, entity.getStatus());
        statement.bindLong(8, entity.getProgress());
        statement.bindLong(9, entity.getTarget());
        statement.bindLong(10, entity.getCachedAt());
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM quests";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<QuestEntity> quests,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestEntity.insert(quests);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Flow<List<QuestEntity>> observeAll() {
    final String _sql = "SELECT * FROM quests ORDER BY questType, questId";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quests"}, new Callable<List<QuestEntity>>() {
      @Override
      @NonNull
      public List<QuestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfQuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "questId");
          final int _cursorIndexOfAssignedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "assignedDate");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfXpReward = CursorUtil.getColumnIndexOrThrow(_cursor, "xpReward");
          final int _cursorIndexOfQuestType = CursorUtil.getColumnIndexOrThrow(_cursor, "questType");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "target");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final List<QuestEntity> _result = new ArrayList<QuestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuestEntity _item;
            final String _tmpQuestId;
            _tmpQuestId = _cursor.getString(_cursorIndexOfQuestId);
            final String _tmpAssignedDate;
            _tmpAssignedDate = _cursor.getString(_cursorIndexOfAssignedDate);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final int _tmpXpReward;
            _tmpXpReward = _cursor.getInt(_cursorIndexOfXpReward);
            final String _tmpQuestType;
            _tmpQuestType = _cursor.getString(_cursorIndexOfQuestType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final int _tmpTarget;
            _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _item = new QuestEntity(_tmpQuestId,_tmpAssignedDate,_tmpTitle,_tmpDescription,_tmpXpReward,_tmpQuestType,_tmpStatus,_tmpProgress,_tmpTarget,_tmpCachedAt);
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
}
