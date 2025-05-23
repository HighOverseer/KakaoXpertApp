package com.neotelemetrixgdscunand.kakaoxpert.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.CocoaAverageSellPriceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocoaAverageSellPriceInfoDao {
    @Query("SELECT * FROM cocoa_average_sell_price_history ORDER BY time DESC LIMIT 1")
    fun getLatest(): Flow<CocoaAverageSellPriceHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cocoaAverageSellPriceHistoryEntity: CocoaAverageSellPriceHistoryEntity)

    @Query("DELETE FROM cocoa_average_sell_price_history")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM cocoa_average_sell_price_history)")
    suspend fun checkIfDataExists(): Boolean
}