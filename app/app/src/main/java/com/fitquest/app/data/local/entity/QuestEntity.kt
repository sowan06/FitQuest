package com.fitquest.app.data.local.entity

import androidx.room.Entity

@Entity(tableName = "quests", primaryKeys = ["questId", "assignedDate"])
data class QuestEntity(
    val questId: String,
    val assignedDate: String, // YYYY-MM-DD
    val title: String,
    val description: String,
    val xpReward: Int,
    val questType: String, // 'daily' | 'weekly'
    val status: String,    // 'active' | 'completed' | 'claimed'
    val progress: Int,
    val target: Int,
    val cachedAt: Long,
)
