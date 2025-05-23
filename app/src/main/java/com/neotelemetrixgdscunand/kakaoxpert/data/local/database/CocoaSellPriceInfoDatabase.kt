package com.neotelemetrixgdscunand.kakaoxpert.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao.CocoaAverageSellPriceInfoDao
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao.CocoaDiseaseSellPriceInfoDao
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAnalysisPreviewEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAverageSellPriceHistoryEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaDiseaseSellPriceInfoEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedDetectedCocoaEntity

@Database(
    entities = [
        CocoaAverageSellPriceHistoryEntity::class,
        CocoaDiseaseSellPriceInfoEntity::class,
    ],
    version = 1
)
abstract class CocoaSellPriceInfoDatabase:RoomDatabase() {

    abstract fun cocoaDiseaseSellPriceInfoDao(): CocoaDiseaseSellPriceInfoDao
    abstract fun cocoaAverageSellPriceInfoDao(): CocoaAverageSellPriceInfoDao

    companion object{
        const val DATABASE_NAME = "cocoa_sell_price_info_database.db"
    }
}