package com.fitquest.app.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fitquest.app.data.local.dao.FoodDao
import com.fitquest.app.data.local.dao.WorkoutDao
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.LogFoodRequest
import com.fitquest.app.data.remote.dto.LogWorkoutRequest
import com.fitquest.app.data.remote.dto.LogWorkoutSet
import com.fitquest.app.data.repository.CharacterRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val workoutDao: WorkoutDao,
    private val foodDao: FoodDao,
    private val api: ApiService,
    private val characterRepository: CharacterRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        var pushedAny = false
        try {
            for (session in workoutDao.getUnsyncedSessions()) {
                val sets = workoutDao.getSetsForSession(session.localId)
                if (sets.isEmpty()) continue
                runCatching {
                    val res = api.logWorkout(
                        LogWorkoutRequest(
                            notes = session.notes,
                            sets = sets.map {
                                LogWorkoutSet(it.exerciseName, it.reps, it.weightKg)
                            },
                        ),
                    )
                    workoutDao.markSynced(session.localId, res.sessionId, res.xpEarned)
                    pushedAny = true
                }
            }
            for (food in foodDao.getUnsynced()) {
                runCatching {
                    val res = api.logFood(
                        LogFoodRequest(
                            foodName = food.foodName,
                            calories = food.calories,
                            proteinG = food.proteinG,
                            carbsG = food.carbsG,
                            fatG = food.fatG,
                            servingG = food.servingG,
                            mealType = food.mealType,
                        ),
                    )
                    foodDao.markSynced(food.localId, res.entry.id, res.xpEarned)
                    pushedAny = true
                }
            }
            if (pushedAny) characterRepository.refresh()
        } catch (_: Exception) {
            // tolerate failures — next periodic run retries
        }
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "fitquest-sync"
    }
}
