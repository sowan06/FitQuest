package com.fitquest.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitquest.app.data.local.dao.CharacterDao
import com.fitquest.app.data.local.dao.FoodDao
import com.fitquest.app.data.local.dao.QuestDao
import com.fitquest.app.data.local.dao.WorkoutDao
import com.fitquest.app.data.local.entity.CharacterEntity
import com.fitquest.app.data.local.entity.FoodEntity
import com.fitquest.app.data.local.entity.QuestEntity
import com.fitquest.app.data.local.entity.WorkoutSessionEntity
import com.fitquest.app.data.local.entity.WorkoutSetEntity

@Database(
    entities = [
        CharacterEntity::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class,
        FoodEntity::class,
        QuestEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class FitQuestDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun foodDao(): FoodDao
    abstract fun questDao(): QuestDao
}
