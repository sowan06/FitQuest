package com.fitquest.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val serverId: String? = null,
    val notes: String? = null,
    val totalVolumeKg: Double,
    val xpEarned: Int = 0,
    val loggedAt: Long, // epoch ms
    val isSynced: Boolean = false,
)
