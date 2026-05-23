package com.fitquest.app.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.fitquest.app.data.local.entity.WorkoutSessionEntity
import com.fitquest.app.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

data class WorkoutSessionWithSets(
    @Embedded val session: WorkoutSessionEntity,
    @Relation(parentColumn = "localId", entityColumn = "localSessionId")
    val sets: List<WorkoutSetEntity>,
)

data class DailyVolume(
    val day: String,
    val volumeKg: Double,
)

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Insert
    suspend fun insertSets(sets: List<WorkoutSetEntity>)

    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<WorkoutSessionWithSets>>

    @Query("SELECT * FROM workout_sessions WHERE isSynced = 0 ORDER BY loggedAt ASC")
    suspend fun getUnsyncedSessions(): List<WorkoutSessionEntity>

    @Query("SELECT * FROM workout_sets WHERE localSessionId = :localSessionId ORDER BY setNumber")
    suspend fun getSetsForSession(localSessionId: Long): List<WorkoutSetEntity>

    @Query("UPDATE workout_sessions SET serverId = :serverId, isSynced = 1, xpEarned = :xpEarned WHERE localId = :localId")
    suspend fun markSynced(localId: Long, serverId: String, xpEarned: Int)

    @Query(
        """
        SELECT date(loggedAt / 1000, 'unixepoch') AS day,
               SUM(totalVolumeKg) AS volumeKg
          FROM workout_sessions
         WHERE loggedAt >= :since
         GROUP BY day
         ORDER BY day
        """,
    )
    fun observeVolumeByDay(since: Long): Flow<List<DailyVolume>>

    @Query("DELETE FROM workout_sessions")
    suspend fun clearAll()
}
