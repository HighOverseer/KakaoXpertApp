package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.SavedCocoaAnalysisAndDetectedCocoasRelation
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.relations.UnsavedCocoaAnalysisAndDetectedCocoasRelation

@Dao
interface SavedCocoaAnalysisDao {

    @Query("SELECT * FROM saved_cocoa_analysis")
    suspend fun getAll(): List<SavedCocoaAnalysisEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listSavedCocoaAnalysis: List<SavedCocoaAnalysisEntity>)

    @Query("SELECT * FROM saved_cocoa_analysis WHERE session_id = :sessionId")
    suspend fun getById(sessionId:Int): SavedCocoaAnalysisAndDetectedCocoasRelation?

    @Query("DELETE FROM saved_cocoa_analysis")
    suspend fun resetTableData()
}