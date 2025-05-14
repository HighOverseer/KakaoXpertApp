package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity

@Dao
interface SavedDetectedCocoaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listSavedDetectedCocoa:List<SavedDetectedCocoaEntity>)
}