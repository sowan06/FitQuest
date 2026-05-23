package com.fitquest.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestResponse(
    val id: String,
    @Json(name = "quest_id") val questId: String,
    val title: String,
    val description: String,
    val status: String,
    val progress: Int,
    val target: Int,
    @Json(name = "xp_reward") val xpReward: Int,
    @Json(name = "quest_type") val questType: String,
    @Json(name = "assigned_date") val assignedDate: String,
)

@JsonClass(generateAdapter = true)
data class ClaimResponse(
    @Json(name = "xp_awarded") val xpAwarded: Int,
    @Json(name = "leveled_up") val leveledUp: Boolean,
    @Json(name = "new_level") val newLevel: Int?,
)

@JsonClass(generateAdapter = true)
data class AchievementResponse(
    val id: String,
    val title: String,
    val description: String,
    @Json(name = "xp_reward") val xpReward: Int,
    val icon: String,
    @Json(name = "unlocked_at") val unlockedAt: String?,
)
