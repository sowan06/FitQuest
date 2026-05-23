package com.fitquest.app.data.repository

import com.fitquest.app.data.local.dao.CharacterDao
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.CharacterResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val api: ApiService,
    private val dao: CharacterDao,
) {
    /** Always emit the cached character from Room. UI calls refresh() to fetch. */
    fun observe(): Flow<CharacterEntity?> = dao.observeAny()

    suspend fun refresh(): Result<Unit> = runCatching {
        val res = api.getCharacter()
        dao.upsert(res.toEntity())
    }

    suspend fun rename(name: String): Result<Unit> = runCatching {
        val res = api.updateCharacter(
            com.fitquest.app.data.remote.dto.UpdateCharacterRequest(name),
        )
        dao.upsert(res.toEntity())
    }
}

internal fun CharacterResponse.toEntity(): CharacterEntity = CharacterEntity(
    userId = userId,
    name = name,
    level = level,
    xp = xp,
    xpToNext = xpToNext,
    xpProgressPct = xpProgressPct.toFloat(),
    avatarStage = avatarStage,
    avatarLabel = avatarLabel,
    statStr = statStr,
    statVit = statVit,
    statEnd = statEnd,
    statWis = statWis,
    statCon = statCon,
    streakDays = streakDays,
    totalVolumeKg = totalVolumeKg,
    totalWorkouts = totalWorkouts,
    dailyCalorieGoal = dailyCalorieGoal,
    dailyProteinGoalG = dailyProteinGoalG,
    updatedAt = System.currentTimeMillis(),
)
