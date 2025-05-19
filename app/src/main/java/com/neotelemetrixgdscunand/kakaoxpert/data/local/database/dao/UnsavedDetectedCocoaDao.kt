package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity

@Dao
interface UnsavedDetectedCocoaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(unsavedDetectedCocoaEntities: List<UnsavedDetectedCocoaEntity>)

    @Query("DELETE FROM unsaved_detected_cocoa")
    suspend fun resetTableData()
}