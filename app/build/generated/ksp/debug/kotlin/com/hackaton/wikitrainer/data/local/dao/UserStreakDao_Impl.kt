package com.hackaton.wikitrainer.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.hackaton.wikitrainer.`data`.local.entity.UserStreakEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserStreakDao_Impl(
  __db: RoomDatabase,
) : UserStreakDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserStreakEntity: EntityInsertAdapter<UserStreakEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserStreakEntity = object : EntityInsertAdapter<UserStreakEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `user_streak` (`id`,`currentStreak`,`bestStreak`,`totalXp`,`totalLessonsCompleted`,`lastActiveDate`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserStreakEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.currentStreak.toLong())
        statement.bindLong(3, entity.bestStreak.toLong())
        statement.bindLong(4, entity.totalXp.toLong())
        statement.bindLong(5, entity.totalLessonsCompleted.toLong())
        statement.bindText(6, entity.lastActiveDate)
      }
    }
  }

  public override suspend fun saveStreak(streak: UserStreakEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfUserStreakEntity.insertAndReturnId(_connection, streak)
    _result
  }

  public override fun getStreak(): Flow<UserStreakEntity?> {
    val _sql: String = "SELECT * FROM user_streak WHERE id = 1"
    return createFlow(__db, false, arrayOf("user_streak")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfBestStreak: Int = getColumnIndexOrThrow(_stmt, "bestStreak")
        val _columnIndexOfTotalXp: Int = getColumnIndexOrThrow(_stmt, "totalXp")
        val _columnIndexOfTotalLessonsCompleted: Int = getColumnIndexOrThrow(_stmt, "totalLessonsCompleted")
        val _columnIndexOfLastActiveDate: Int = getColumnIndexOrThrow(_stmt, "lastActiveDate")
        val _result: UserStreakEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpBestStreak: Int
          _tmpBestStreak = _stmt.getLong(_columnIndexOfBestStreak).toInt()
          val _tmpTotalXp: Int
          _tmpTotalXp = _stmt.getLong(_columnIndexOfTotalXp).toInt()
          val _tmpTotalLessonsCompleted: Int
          _tmpTotalLessonsCompleted = _stmt.getLong(_columnIndexOfTotalLessonsCompleted).toInt()
          val _tmpLastActiveDate: String
          _tmpLastActiveDate = _stmt.getText(_columnIndexOfLastActiveDate)
          _result = UserStreakEntity(_tmpId,_tmpCurrentStreak,_tmpBestStreak,_tmpTotalXp,_tmpTotalLessonsCompleted,_tmpLastActiveDate)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getStreakSync(): UserStreakEntity? {
    val _sql: String = "SELECT * FROM user_streak WHERE id = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfBestStreak: Int = getColumnIndexOrThrow(_stmt, "bestStreak")
        val _columnIndexOfTotalXp: Int = getColumnIndexOrThrow(_stmt, "totalXp")
        val _columnIndexOfTotalLessonsCompleted: Int = getColumnIndexOrThrow(_stmt, "totalLessonsCompleted")
        val _columnIndexOfLastActiveDate: Int = getColumnIndexOrThrow(_stmt, "lastActiveDate")
        val _result: UserStreakEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpBestStreak: Int
          _tmpBestStreak = _stmt.getLong(_columnIndexOfBestStreak).toInt()
          val _tmpTotalXp: Int
          _tmpTotalXp = _stmt.getLong(_columnIndexOfTotalXp).toInt()
          val _tmpTotalLessonsCompleted: Int
          _tmpTotalLessonsCompleted = _stmt.getLong(_columnIndexOfTotalLessonsCompleted).toInt()
          val _tmpLastActiveDate: String
          _tmpLastActiveDate = _stmt.getText(_columnIndexOfLastActiveDate)
          _result = UserStreakEntity(_tmpId,_tmpCurrentStreak,_tmpBestStreak,_tmpTotalXp,_tmpTotalLessonsCompleted,_tmpLastActiveDate)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
