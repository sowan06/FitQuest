package com.fitquest.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    val id: String,
    @Json(name = "user_id") val userId: String,
    val name: String,
    val level: Int,
    val xp: Int,
    @Json(name = "xp_to_next") val xpToNext: Int,
    @Json(name = "xp_progress_pct") val xpProgressPct: Double,
    @Json(name = "avatar_stage") val avatarStage: Int,
    @Json(name = "avatar_label") val avatarLabel: String,
    @Json(name = "stat_str") val statStr: Int,
    @Json(name = "stat_vit") val statVit: Int,
    @Json(name = "stat_end") val statEnd: Int,
    @Json(name = "stat_wis") val statWis: Int,
    @Json(name = "stat_con") val statCon: Int,
    @Json(name = "streak_days") val streakDays: Int,
    @Json(name = "total_volume_kg") val totalVolumeKg: Double,
    @Json(name = "total_workouts") val totalWorkouts: Int,
    @Json(name = "daily_calorie_goal") val dailyCalorieGoal: Int,
    @Json(name = "daily_protein_goal_g") val dailyProteinGoalG: Int,
)

@JsonClass(generateAdapter = true)
data class UpdateCharacterRequest(val name: String)
