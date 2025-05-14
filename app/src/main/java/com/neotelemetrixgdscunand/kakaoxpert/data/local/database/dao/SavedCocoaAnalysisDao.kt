package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity

@Dao
interface SavedCocoaAnalysisDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listSavedCocoaAnalysis: List<SavedCocoaAnalysisEntity>)

}