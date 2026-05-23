package com.fitquest.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val serverId: String? = null,
    val foodName: String,
    val calories: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val servingG: Double? = null,
    val mealType: String,
    val xpEarned: Int = 0,
    val loggedAt: Long, // epoch ms
    val isSynced: Boolean = false,
)
