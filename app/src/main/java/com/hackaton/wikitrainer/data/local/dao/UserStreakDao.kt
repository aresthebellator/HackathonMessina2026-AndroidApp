package com.hackaton.wikitrainer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackaton.wikitrainer.data.local.entity.UserStreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStreakDao {

    @Query("SELECT * FROM user_streak WHERE id = 1")
    fun getStreak(): Flow<UserStreakEntity?>

    @Query("SELECT * FROM user_streak WHERE id = 1")
    suspend fun getStreakSync(): UserStreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStreak(streak: UserStreakEntity): Long
}
