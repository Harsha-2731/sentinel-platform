package com.sentinel.agent.data.local;

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
import com.sentinel.agent.data.local.dao.RiskEventDao;
import com.sentinel.agent.data.local.dao.RiskEventDao_Impl;
import com.sentinel.agent.data.local.dao.TaskDao;
import com.sentinel.agent.data.local.dao.TaskDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RiskEventDao _riskEventDao;

  private volatile TaskDao _taskDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `risk_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `eventType` TEXT NOT NULL, `riskDelta` INTEGER NOT NULL, `currentRiskScore` INTEGER NOT NULL, `reasoning` TEXT NOT NULL, `isSynced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `agent_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `agentName` TEXT NOT NULL, `taskDescription` TEXT NOT NULL, `actionType` TEXT NOT NULL, `amount` INTEGER NOT NULL, `status` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `previousHash` TEXT, `currentHash` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1e9da67504b9610f218332f20ffd28da')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `risk_events`");
        db.execSQL("DROP TABLE IF EXISTS `agent_tasks`");
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
        final HashMap<String, TableInfo.Column> _columnsRiskEvents = new HashMap<String, TableInfo.Column>(7);
        _columnsRiskEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("riskDelta", new TableInfo.Column("riskDelta", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("currentRiskScore", new TableInfo.Column("currentRiskScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("reasoning", new TableInfo.Column("reasoning", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRiskEvents.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRiskEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRiskEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRiskEvents = new TableInfo("risk_events", _columnsRiskEvents, _foreignKeysRiskEvents, _indicesRiskEvents);
        final TableInfo _existingRiskEvents = TableInfo.read(db, "risk_events");
        if (!_infoRiskEvents.equals(_existingRiskEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "risk_events(com.sentinel.agent.data.local.entity.RiskEventEntity).\n"
                  + " Expected:\n" + _infoRiskEvents + "\n"
                  + " Found:\n" + _existingRiskEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsAgentTasks = new HashMap<String, TableInfo.Column>(9);
        _columnsAgentTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("agentName", new TableInfo.Column("agentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("taskDescription", new TableInfo.Column("taskDescription", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("actionType", new TableInfo.Column("actionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("previousHash", new TableInfo.Column("previousHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAgentTasks.put("currentHash", new TableInfo.Column("currentHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAgentTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAgentTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAgentTasks = new TableInfo("agent_tasks", _columnsAgentTasks, _foreignKeysAgentTasks, _indicesAgentTasks);
        final TableInfo _existingAgentTasks = TableInfo.read(db, "agent_tasks");
        if (!_infoAgentTasks.equals(_existingAgentTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "agent_tasks(com.sentinel.agent.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoAgentTasks + "\n"
                  + " Found:\n" + _existingAgentTasks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1e9da67504b9610f218332f20ffd28da", "abd1611c58a519ef3df8b94cd085b7c3");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "risk_events","agent_tasks");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `risk_events`");
      _db.execSQL("DELETE FROM `agent_tasks`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(RiskEventDao.class, RiskEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
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
  public RiskEventDao riskEventDao() {
    if (_riskEventDao != null) {
      return _riskEventDao;
    } else {
      synchronized(this) {
        if(_riskEventDao == null) {
          _riskEventDao = new RiskEventDao_Impl(this);
        }
        return _riskEventDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
