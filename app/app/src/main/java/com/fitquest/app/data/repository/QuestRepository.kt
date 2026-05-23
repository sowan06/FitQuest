package com.fitquest.app.data.repository

import com.fitquest.app.data.local.dao.QuestDao
import com.fitquest.app.data.local.entity.QuestEntity
import com.fitquest.app.data.remote.ApiService
import com.fitquest.app.data.remote.dto.AchievementResponse
import com.fitquest.app.data.remote.dto.QuestResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestRepository @Inject constructor(
    private val api: ApiService,
    private val dao: QuestDao,
    private val characterRepo: CharacterRepository,
) {
    fun observe(): Flow<List<QuestEntity>> = dao.observeAll()

    suspend fun refresh(): Result<Unit> = runCatching {
        val quests = api.getQuests()
        val now = System.currentTimeMillis()
        // Replace cached set so old quests roll out cleanly.
        dao.clearAll()
        dao.upsertAll(quests.map { it.toEntity(now) })
    }

    suspend fun claim(questId: String): Result<Int> = runCatching {
        val res = api.claimQuest(questId)
        characterRepo.refresh()
        refresh()
        res.xpAwarded
    }

    suspend fun listAchievements(): Result<List<AchievementResponse>> = runCatching {
        api.getAchievements()
    }
}

private fun QuestResponse.toEntity(cachedAt: Long): QuestEntity = QuestEntity(
    questId = questId,
    assignedDate = assignedDate,
    title = title,
    description = description,
    xpReward = xpReward,
    questType = questType,
    status = status,
    progress = progress,
    target = target,
    cachedAt = cachedAt,
)
