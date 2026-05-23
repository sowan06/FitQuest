package com.fitquest.app.data.repository

import com.fitquest.app.data.local.dao.FoodDao
import com.fitquest.app.data.local.entity.FoodEntity
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.FoodLogResponse
import com.fitquest.app.data.remote.dto.LogFoodRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: ApiService,
    private val dao: FoodDao,
    private val characterRepo: CharacterRepository,
) {
    fun observeToday(): Flow<List<FoodEntity>> = dao.observeToday()
    fun observeAll(): Flow<List<FoodEntity>> = dao.observeAll()

    suspend fun logFood(entry: FoodEntity): Result<FoodLogResponse?> = runCatching {
        val toInsert = entry.copy(isSynced = false, loggedAt = System.currentTimeMillis())
        val localId = dao.insert(toInsert)

        try {
            val res = api.logFood(
                LogFoodRequest(
                    foodName = entry.foodName,
                    calories = entry.calories,
                    proteinG = entry.proteinG,
                    carbsG = entry.carbsG,
                    fatG = entry.fatG,
                    servingG = entry.servingG,
                    mealType = entry.mealType,
                ),
            )
            dao.markSynced(localId, res.entry.id, res.xpEarned)
            characterRepo.refresh()
            return@runCatching res
        } catch (e: Exception) {
            return@runCatching null
        }
    }
}
