package com.hackaton.wikitrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackaton.wikitrainer.data.local.entity.TopicHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicHistoryEntity): Long

    @Query("SELECT * FROM topic_history ORDER BY completedAt DESC")
    fun getAllTopics(): Flow<List<TopicHistoryEntity>>

    @Query("SELECT * FROM topic_history WHERE id = :id")
    suspend fun getTopicById(id: Long): TopicHistoryEntity?

    @Query("SELECT COUNT(*) FROM topic_history")
    suspend fun getTopicCount(): Int
}
