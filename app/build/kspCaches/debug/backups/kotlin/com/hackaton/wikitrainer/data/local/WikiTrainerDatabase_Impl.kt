package com.hackaton.wikitrainer.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.hackaton.wikitrainer.`data`.local.dao.TopicHistoryDao
import com.hackaton.wikitrainer.`data`.local.dao.TopicHistoryDao_Impl
import com.hackaton.wikitrainer.`data`.local.dao.UserStreakDao
import com.hackaton.wikitrainer.`data`.local.dao.UserStreakDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class WikiTrainerDatabase_Impl : WikiTrainerDatabase() {
  private val _topicHistoryDao: Lazy<TopicHistoryDao> = lazy {
    TopicHistoryDao_Impl(this)
  }

  private val _userStreakDao: Lazy<UserStreakDao> = lazy {
    UserStreakDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "157b5bc5aec13ccd8518f61c9a8a2a03", "a38955c9b7ecfd7ed91b1054c222ed8e") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `topic_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pageId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `extract` TEXT NOT NULL, `thumbnailUrl` TEXT, `wikiUrl` TEXT NOT NULL, `score` INTEGER NOT NULL, `totalQuestions` INTEGER NOT NULL, `completedAt` INTEGER NOT NULL, `xpEarned` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_streak` (`id` INTEGER NOT NULL, `currentStreak` INTEGER NOT NULL, `bestStreak` INTEGER NOT NULL, `totalXp` INTEGER NOT NULL, `totalLessonsCompleted` INTEGER NOT NULL, `lastActiveDate` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '157b5bc5aec13ccd8518f61c9a8a2a03')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `topic_history`")
        connection.execSQL("DROP TABLE IF EXISTS `user_streak`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsTopicHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTopicHistory.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("pageId", TableInfo.Column("pageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("extract", TableInfo.Column("extract", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("thumbnailUrl", TableInfo.Column("thumbnailUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("wikiUrl", TableInfo.Column("wikiUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("score", TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("totalQuestions", TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("completedAt", TableInfo.Column("completedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopicHistory.put("xpEarned", TableInfo.Column("xpEarned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTopicHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTopicHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTopicHistory: TableInfo = TableInfo("topic_history", _columnsTopicHistory, _foreignKeysTopicHistory, _indicesTopicHistory)
        val _existingTopicHistory: TableInfo = read(connection, "topic_history")
        if (!_infoTopicHistory.equals(_existingTopicHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |topic_history(com.hackaton.wikitrainer.data.local.entity.TopicHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoTopicHistory + """
              |
              | Found:
              |""".trimMargin() + _existingTopicHistory)
        }
        val _columnsUserStreak: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserStreak.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserStreak.put("currentStreak", TableInfo.Column("currentStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserStreak.put("bestStreak", TableInfo.Column("bestStreak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserStreak.put("totalXp", TableInfo.Column("totalXp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserStreak.put("totalLessonsCompleted", TableInfo.Column("totalLessonsCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserStreak.put("lastActiveDate", TableInfo.Column("lastActiveDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserStreak: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserStreak: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserStreak: TableInfo = TableInfo("user_streak", _columnsUserStreak, _foreignKeysUserStreak, _indicesUserStreak)
        val _existingUserStreak: TableInfo = read(connection, "user_streak")
        if (!_infoUserStreak.equals(_existingUserStreak)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_streak(com.hackaton.wikitrainer.data.local.entity.UserStreakEntity).
              | Expected:
              |""".trimMargin() + _infoUserStreak + """
              |
              | Found:
              |""".trimMargin() + _existingUserStreak)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "topic_history", "user_streak")
  }

  public override fun clearAllTables() {
    super.performClear(false, "topic_history", "user_streak")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(TopicHistoryDao::class, TopicHistoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserStreakDao::class, UserStreakDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun topicHistoryDao(): TopicHistoryDao = _topicHistoryDao.value

  public override fun userStreakDao(): UserStreakDao = _userStreakDao.value
}
