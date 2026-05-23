package com.fitquest.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitquest.app.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE userId = :userId LIMIT 1")
    fun observe(userId: String): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters LIMIT 1")
    fun observeAny(): Flow<CharacterEntity?>

    @Query("DELETE FROM characters")
    suspend fun clear()
}
