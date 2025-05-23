package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaDiseaseSellPriceInfoEntity

@Dao
interface CocoaDiseaseSellPriceInfoDao {

    @Query("SELECT * FROM cocoa_disease_sell_price_info")
    suspend fun getAllCocoaDiseaseSellPriceInfo(): List<CocoaDiseaseSellPriceInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocoaDiseaseSellPriceInfo(cocoaDiseaseSellPriceInfoEntities: List<CocoaDiseaseSellPriceInfoEntity>)

    @Query("DELETE FROM cocoa_disease_sell_price_info")
    suspend fun deleteAllCocoaDiseaseSellPriceInfo()

    @Query("SELECT EXISTS (SELECT 1 FROM cocoa_disease_sell_price_info)")
    suspend fun checkIfDataExists(): Boolean

}