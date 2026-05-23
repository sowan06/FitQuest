package com.fitquest.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["localId"],
            childColumns = ["localSessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["localSessionId"])],
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val localSessionId: Long,
    val exerciseName: String,
    val setNumber: Int,
    val reps: Int,
    val weightKg: Double,
)
