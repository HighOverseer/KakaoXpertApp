package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaAverageSellPriceInfo
import kotlinx.coroutines.flow.Flow

interface CocoaPriceInfoRepository {

    suspend fun syncAllPricesInfo():Result<Unit, DataError>

    fun getCocoaPriceInfo(): Flow<CocoaAverageSellPriceInfo?>
}