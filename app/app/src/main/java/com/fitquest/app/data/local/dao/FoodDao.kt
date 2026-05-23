package com.fitquest.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitquest.app.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(food: FoodEntity): Long

    @Query(
        """
        SELECT * FROM food_logs
         WHERE date(loggedAt / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')
         ORDER BY loggedAt
        """,
    )
    fun observeToday(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food_logs ORDER BY loggedAt DESC LIMIT :limit OFFSET :offset")
    fun observeAll(limit: Int = 50, offset: Int = 0): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food_logs WHERE isSynced = 0 ORDER BY loggedAt ASC")
    suspend fun getUnsynced(): List<FoodEntity>

    @Query("UPDATE food_logs SET serverId = :serverId, isSynced = 1, xpEarned = :xpEarned WHERE localId = :localId")
    suspend fun markSynced(localId: Long, serverId: String, xpEarned: Int)

    @Query("DELETE FROM food_logs")
    suspend fun clearAll()
}
