package com.hackaton.wikitrainer.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.hackaton.wikitrainer.`data`.local.entity.TopicHistoryEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TopicHistoryDao_Impl(
  __db: RoomDatabase,
) : TopicHistoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTopicHistoryEntity: EntityInsertAdapter<TopicHistoryEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTopicHistoryEntity = object : EntityInsertAdapter<TopicHistoryEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `topic_history` (`id`,`pageId`,`title`,`description`,`extract`,`thumbnailUrl`,`wikiUrl`,`score`,`totalQuestions`,`completedAt`,`xpEarned`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TopicHistoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.pageId)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.description)
        statement.bindText(5, entity.extract)
        val _tmpThumbnailUrl: String? = entity.thumbnailUrl
        if (_tmpThumbnailUrl == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpThumbnailUrl)
        }
        statement.bindText(7, entity.wikiUrl)
        statement.bindLong(8, entity.score.toLong())
        statement.bindLong(9, entity.totalQuestions.toLong())
        statement.bindLong(10, entity.completedAt)
        statement.bindLong(11, entity.xpEarned.toLong())
      }
    }
  }

  public override suspend fun insertTopic(topic: TopicHistoryEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfTopicHistoryEntity.insertAndReturnId(_connection, topic)
    _result
  }

  public override fun getAllTopics(): Flow<List<TopicHistoryEntity>> {
    val _sql: String = "SELECT * FROM topic_history ORDER BY completedAt DESC"
    return createFlow(__db, false, arrayOf("topic_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPageId: Int = getColumnIndexOrThrow(_stmt, "pageId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfExtract: Int = getColumnIndexOrThrow(_stmt, "extract")
        val _columnIndexOfThumbnailUrl: Int = getColumnIndexOrThrow(_stmt, "thumbnailUrl")
        val _columnIndexOfWikiUrl: Int = getColumnIndexOrThrow(_stmt, "wikiUrl")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfTotalQuestions: Int = getColumnIndexOrThrow(_stmt, "totalQuestions")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfXpEarned: Int = getColumnIndexOrThrow(_stmt, "xpEarned")
        val _result: MutableList<TopicHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TopicHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPageId: Long
          _tmpPageId = _stmt.getLong(_columnIndexOfPageId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpExtract: String
          _tmpExtract = _stmt.getText(_columnIndexOfExtract)
          val _tmpThumbnailUrl: String?
          if (_stmt.isNull(_columnIndexOfThumbnailUrl)) {
            _tmpThumbnailUrl = null
          } else {
            _tmpThumbnailUrl = _stmt.getText(_columnIndexOfThumbnailUrl)
          }
          val _tmpWikiUrl: String
          _tmpWikiUrl = _stmt.getText(_columnIndexOfWikiUrl)
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpTotalQuestions: Int
          _tmpTotalQuestions = _stmt.getLong(_columnIndexOfTotalQuestions).toInt()
          val _tmpCompletedAt: Long
          _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          val _tmpXpEarned: Int
          _tmpXpEarned = _stmt.getLong(_columnIndexOfXpEarned).toInt()
          _item = TopicHistoryEntity(_tmpId,_tmpPageId,_tmpTitle,_tmpDescription,_tmpExtract,_tmpThumbnailUrl,_tmpWikiUrl,_tmpScore,_tmpTotalQuestions,_tmpCompletedAt,_tmpXpEarned)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTopicById(id: Long): TopicHistoryEntity? {
    val _sql: String = "SELECT * FROM topic_history WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPageId: Int = getColumnIndexOrThrow(_stmt, "pageId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfExtract: Int = getColumnIndexOrThrow(_stmt, "extract")
        val _columnIndexOfThumbnailUrl: Int = getColumnIndexOrThrow(_stmt, "thumbnailUrl")
        val _columnIndexOfWikiUrl: Int = getColumnIndexOrThrow(_stmt, "wikiUrl")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfTotalQuestions: Int = getColumnIndexOrThrow(_stmt, "totalQuestions")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfXpEarned: Int = getColumnIndexOrThrow(_stmt, "xpEarned")
        val _result: TopicHistoryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPageId: Long
          _tmpPageId = _stmt.getLong(_columnIndexOfPageId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpExtract: String
          _tmpExtract = _stmt.getText(_columnIndexOfExtract)
          val _tmpThumbnailUrl: String?
          if (_stmt.isNull(_columnIndexOfThumbnailUrl)) {
            _tmpThumbnailUrl = null
          } else {
            _tmpThumbnailUrl = _stmt.getText(_columnIndexOfThumbnailUrl)
          }
          val _tmpWikiUrl: String
          _tmpWikiUrl = _stmt.getText(_columnIndexOfWikiUrl)
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpTotalQuestions: Int
          _tmpTotalQuestions = _stmt.getLong(_columnIndexOfTotalQuestions).toInt()
          val _tmpCompletedAt: Long
          _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          val _tmpXpEarned: Int
          _tmpXpEarned = _stmt.getLong(_columnIndexOfXpEarned).toInt()
          _result = TopicHistoryEntity(_tmpId,_tmpPageId,_tmpTitle,_tmpDescription,_tmpExtract,_tmpThumbnailUrl,_tmpWikiUrl,_tmpScore,_tmpTotalQuestions,_tmpCompletedAt,_tmpXpEarned)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTopicCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM topic_history"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
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
