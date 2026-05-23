package com.fitquest.app.data.repository

import com.fitquest.app.data.local.dao.DailyVolume
import com.fitquest.app.data.local.dao.WorkoutDao
import com.fitquest.app.data.local.dao.WorkoutSessionWithSets
import com.fitquest.app.data.local.entity.WorkoutSessionEntity
import com.fitquest.app.data.local.entity.WorkoutSetEntity
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.LogWorkoutRequest
import com.fitquest.app.data.remote.dto.LogWorkoutSet
import com.fitquest.app.data.remote.dto.WorkoutSummaryResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val api: ApiService,
    private val dao: WorkoutDao,
    private val characterRepo: CharacterRepository,
) {
    fun observeSessions(): Flow<List<WorkoutSessionWithSets>> = dao.observeAll()

    fun observeVolume(days: Int = 7): Flow<List<DailyVolume>> {
        val since = System.currentTimeMillis() - days * 86_400_000L
        return dao.observeVolumeByDay(since)
    }

    /**
     * Log a workout. Always saves locally first (offline-first), then attempts
     * to push. On success the Room row is marked synced and character refreshed.
     * On failure SyncWorker will retry.
     */
    suspend fun logWorkout(
        sets: List<WorkoutSetEntity>,
        notes: String?,
    ): Result<WorkoutSummaryResponse?> = runCatching {
        val totalVolume = sets.sumOf { it.reps * it.weightKg }
        val session = WorkoutSessionEntity(
            notes = notes,
            totalVolumeKg = totalVolume,
            loggedAt = System.currentTimeMillis(),
            isSynced = false,
        )
        val localId = dao.insertSession(session)
        val numbered = sets.mapIndexed { idx, s -> s.copy(localSessionId = localId, setNumber = idx + 1) }
        dao.insertSets(numbered)

        try {
            val res = api.logWorkout(
                LogWorkoutRequest(
                    notes = notes,
                    sets = numbered.map {
                        LogWorkoutSet(it.exerciseName, it.reps, it.weightKg)
                    },
                ),
            )
            dao.markSynced(localId, res.sessionId, res.xpEarned)
            characterRepo.refresh()
            return@runCatching res
        } catch (e: Exception) {
            // network failure — keep local row marked unsynced, SyncWorker retries
            return@runCatching null
        }
    }
}
