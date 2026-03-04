package com.sentinel.agent.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
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
import com.sentinel.agent.data.local.entity.TaskEntity;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaskEntity> __insertionAdapterOfTaskEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldTasks;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaskEntity = new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `agent_tasks` (`id`,`agentName`,`taskDescription`,`actionType`,`amount`,`status`,`timestamp`,`previousHash`,`currentHash`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAgentName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAgentName());
        }
        if (entity.getTaskDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTaskDescription());
        }
        if (entity.getActionType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getActionType());
        }
        statement.bindLong(5, entity.getAmount());
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        statement.bindLong(7, entity.getTimestamp());
        if (entity.getPreviousHash() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreviousHash());
        }
        if (entity.getCurrentHash() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCurrentHash());
        }
      }
    };
    this.__preparedStmtOfDeleteOldTasks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM agent_tasks WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTaskEntity.insert(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldTasks(final long expiryTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldTasks.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, expiryTime);
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
          __preparedStmtOfDeleteOldTasks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllTasks(final Continuation<? super List<TaskEntity>> $completion) {
    final String _sql = "SELECT * FROM agent_tasks ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAgentName = CursorUtil.getColumnIndexOrThrow(_cursor, "agentName");
          final int _cursorIndexOfTaskDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "taskDescription");
          final int _cursorIndexOfActionType = CursorUtil.getColumnIndexOrThrow(_cursor, "actionType");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPreviousHash = CursorUtil.getColumnIndexOrThrow(_cursor, "previousHash");
          final int _cursorIndexOfCurrentHash = CursorUtil.getColumnIndexOrThrow(_cursor, "currentHash");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpAgentName;
            if (_cursor.isNull(_cursorIndexOfAgentName)) {
              _tmpAgentName = null;
            } else {
              _tmpAgentName = _cursor.getString(_cursorIndexOfAgentName);
            }
            final String _tmpTaskDescription;
            if (_cursor.isNull(_cursorIndexOfTaskDescription)) {
              _tmpTaskDescription = null;
            } else {
              _tmpTaskDescription = _cursor.getString(_cursorIndexOfTaskDescription);
            }
            final String _tmpActionType;
            if (_cursor.isNull(_cursorIndexOfActionType)) {
              _tmpActionType = null;
            } else {
              _tmpActionType = _cursor.getString(_cursorIndexOfActionType);
            }
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPreviousHash;
            if (_cursor.isNull(_cursorIndexOfPreviousHash)) {
              _tmpPreviousHash = null;
            } else {
              _tmpPreviousHash = _cursor.getString(_cursorIndexOfPreviousHash);
            }
            final String _tmpCurrentHash;
            if (_cursor.isNull(_cursorIndexOfCurrentHash)) {
              _tmpCurrentHash = null;
            } else {
              _tmpCurrentHash = _cursor.getString(_cursorIndexOfCurrentHash);
            }
            _item = new TaskEntity(_tmpId,_tmpAgentName,_tmpTaskDescription,_tmpActionType,_tmpAmount,_tmpStatus,_tmpTimestamp,_tmpPreviousHash,_tmpCurrentHash);
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
  public Object getLatestTaskHash(final Continuation<? super String> $completion) {
    final String _sql = "SELECT currentHash FROM agent_tasks ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
          } else {
            _result = null;
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
