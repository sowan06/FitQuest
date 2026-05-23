package com.fitquest.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitquest.app.data.local.entity.QuestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(quests: List<QuestEntity>)

    @Query("SELECT * FROM quests ORDER BY questType, questId")
    fun observeAll(): Flow<List<QuestEntity>>

    @Query("DELETE FROM quests")
    suspend fun clearAll()
}
