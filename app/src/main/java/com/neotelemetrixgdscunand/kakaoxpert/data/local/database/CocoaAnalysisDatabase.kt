package com.neotelemetrixgdscunand.kakaoxpert.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity

@Database(
    entities = [
        SavedCocoaAnalysisEntity::class,
        UnsavedCocoaAnalysisEntity::class,
        SavedDetectedCocoaEntity::class,
        UnsavedDetectedCocoaEntity::class,
        CocoaAnalysisPreviewEntity::class
    ],
    version = 1
)
abstract class CocoaAnalysisDatabase:RoomDatabase(){

    companion object{
        const val DATABASE_NAME = "cocoa_analysis_database.db"
    }
}