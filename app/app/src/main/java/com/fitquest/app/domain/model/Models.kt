package com.fitquest.app.domain.model

/**
 * Plain domain types used in tests and shared logic. Keep these decoupled from
 * Room entities and Retrofit DTOs so we can swap data sources later.
 */

data class Character(
    val name: String,
    val level: Int,
    val xp: Int,
    val xpToNext: Int,
    val avatarStage: Int,
    val avatarLabel: String,
    val statStr: Int,
    val statVit: Int,
    val statEnd: Int,
    val statWis: Int,
    val statCon: Int,
    val streakDays: Int,
)

data class Workout(
    val sessionId: String?,
    val sets: List<WorkoutSet>,
    val totalVolumeKg: Double,
)

data class WorkoutSet(
    val exerciseName: String,
    val reps: Int,
    val weightKg: Double,
)

data class Food(
    val name: String,
    val calories: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val mealType: String,
)

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,
    val status: String,
    val progress: Int,
    val target: Int,
)
