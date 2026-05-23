package com.fitquest.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogWorkoutSet(
    @Json(name = "exercise_name") val exerciseName: String,
    val reps: Int,
    @Json(name = "weight_kg") val weightKg: Double,
)

@JsonClass(generateAdapter = true)
data class LogWorkoutRequest(
    val notes: String?,
    val sets: List<LogWorkoutSet>,
)

@JsonClass(generateAdapter = true)
data class StatGain(
    val stat: String,
    @Json(name = "old_value") val oldValue: Int,
    @Json(name = "new_value") val newValue: Int,
)

@JsonClass(generateAdapter = true)
data class WorkoutSummaryResponse(
    @Json(name = "session_id") val sessionId: String,
    @Json(name = "total_volume") val totalVolume: Double,
    @Json(name = "xp_earned") val xpEarned: Int,
    @Json(name = "stat_gains") val statGains: List<StatGain>,
    @Json(name = "leveled_up") val leveledUp: Boolean,
    @Json(name = "new_level") val newLevel: Int?,
    @Json(name = "new_avatar_stage") val newAvatarStage: Int?,
    @Json(name = "streak_days") val streakDays: Int,
)

@JsonClass(generateAdapter = true)
data class WorkoutSetResponse(
    val id: String,
    @Json(name = "exercise_name") val exerciseName: String,
    @Json(name = "set_number") val setNumber: Int,
    val reps: Int,
    @Json(name = "weight_kg") val weightKg: Double,
)

@JsonClass(generateAdapter = true)
data class WorkoutSessionResponse(
    val id: String,
    @Json(name = "logged_at") val loggedAt: String,
    val notes: String?,
    @Json(name = "total_volume_kg") val totalVolumeKg: Double,
    @Json(name = "xp_earned") val xpEarned: Int,
    val sets: List<WorkoutSetResponse> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class WorkoutListResponse(
    val page: Int,
    val limit: Int,
    val sessions: List<WorkoutSessionResponse>,
)

@JsonClass(generateAdapter = true)
data class DailyVolumeResponse(
    val day: String,
    @Json(name = "volume_kg") val volumeKg: Double,
)
