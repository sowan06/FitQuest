package com.fitquest.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val level: Int,
    val xp: Int,
    val xpToNext: Int,
    val xpProgressPct: Float,
    val avatarStage: Int,
    val avatarLabel: String,
    val statStr: Int,
    val statVit: Int,
    val statEnd: Int,
    val statWis: Int,
    val statCon: Int,
    val streakDays: Int,
    val totalVolumeKg: Double,
    val totalWorkouts: Int,
    val dailyCalorieGoal: Int,
    val dailyProteinGoalG: Int,
    val updatedAt: Long,
)
