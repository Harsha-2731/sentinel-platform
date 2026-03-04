package com.sentinel.agent.data.local.dao;

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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sentinel.agent.data.local.entity.RiskEventEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RiskEventDao_Impl implements RiskEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RiskEventEntity> __insertionAdapterOfRiskEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldEvents;

  public RiskEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRiskEventEntity = new EntityInsertionAdapter<RiskEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `risk_events` (`id`,`timestamp`,`eventType`,`riskDelta`,`currentRiskScore`,`reasoning`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RiskEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getEventType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEventType());
        }
        statement.bindLong(4, entity.getRiskDelta());
        statement.bindLong(5, entity.getCurrentRiskScore());
        if (entity.getReasoning() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReasoning());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__preparedStmtOfDeleteOldEvents = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM risk_events WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final RiskEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRiskEventEntity.insert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldEvents(final long expiryTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldEvents.acquire();
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
          __preparedStmtOfDeleteOldEvents.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllEvents(final Continuation<? super List<RiskEventEntity>> $completion) {
    final String _sql = "SELECT * FROM risk_events ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RiskEventEntity>>() {
      @Override
      @NonNull
      public List<RiskEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfRiskDelta = CursorUtil.getColumnIndexOrThrow(_cursor, "riskDelta");
          final int _cursorIndexOfCurrentRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "currentRiskScore");
          final int _cursorIndexOfReasoning = CursorUtil.getColumnIndexOrThrow(_cursor, "reasoning");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<RiskEventEntity> _result = new ArrayList<RiskEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RiskEventEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final int _tmpRiskDelta;
            _tmpRiskDelta = _cursor.getInt(_cursorIndexOfRiskDelta);
            final int _tmpCurrentRiskScore;
            _tmpCurrentRiskScore = _cursor.getInt(_cursorIndexOfCurrentRiskScore);
            final String _tmpReasoning;
            if (_cursor.isNull(_cursorIndexOfReasoning)) {
              _tmpReasoning = null;
            } else {
              _tmpReasoning = _cursor.getString(_cursorIndexOfReasoning);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new RiskEventEntity(_tmpId,_tmpTimestamp,_tmpEventType,_tmpRiskDelta,_tmpCurrentRiskScore,_tmpReasoning,_tmpIsSynced);
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
  public Object getUnsyncedEvents(final Continuation<? super List<RiskEventEntity>> $completion) {
    final String _sql = "SELECT * FROM risk_events WHERE isSynced = 0 ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RiskEventEntity>>() {
      @Override
      @NonNull
      public List<RiskEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfRiskDelta = CursorUtil.getColumnIndexOrThrow(_cursor, "riskDelta");
          final int _cursorIndexOfCurrentRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "currentRiskScore");
          final int _cursorIndexOfReasoning = CursorUtil.getColumnIndexOrThrow(_cursor, "reasoning");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<RiskEventEntity> _result = new ArrayList<RiskEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RiskEventEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final int _tmpRiskDelta;
            _tmpRiskDelta = _cursor.getInt(_cursorIndexOfRiskDelta);
            final int _tmpCurrentRiskScore;
            _tmpCurrentRiskScore = _cursor.getInt(_cursorIndexOfCurrentRiskScore);
            final String _tmpReasoning;
            if (_cursor.isNull(_cursorIndexOfReasoning)) {
              _tmpReasoning = null;
            } else {
              _tmpReasoning = _cursor.getString(_cursorIndexOfReasoning);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new RiskEventEntity(_tmpId,_tmpTimestamp,_tmpEventType,_tmpRiskDelta,_tmpCurrentRiskScore,_tmpReasoning,_tmpIsSynced);
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
  public Object markAsSynced(final List<Integer> eventIds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE risk_events SET isSynced = 1 WHERE id IN (");
        final int _inputSize = eventIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (Integer _item : eventIds) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindLong(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
