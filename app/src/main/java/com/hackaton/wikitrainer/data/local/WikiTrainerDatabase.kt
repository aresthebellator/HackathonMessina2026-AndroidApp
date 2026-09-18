package com.hackaton.wikitrainer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackaton.wikitrainer.data.local.dao.TopicHistoryDao
import com.hackaton.wikitrainer.data.local.dao.UserStreakDao
import com.hackaton.wikitrainer.data.local.entity.TopicHistoryEntity
import com.hackaton.wikitrainer.data.local.entity.UserStreakEntity

@Database(
    entities = [
        TopicHistoryEntity::class,
        UserStreakEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WikiTrainerDatabase : RoomDatabase() {
    abstract fun topicHistoryDao(): TopicHistoryDao
    abstract fun userStreakDao(): UserStreakDao

    companion object {
        const val DATABASE_NAME = "wikitrainer.db"
    }
}
