package com.neotelemetrixgdscunand.kamekapp.domain.data

import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.model.ShopItem

interface ShopRepository {

    suspend fun getShopItems(query: String = ""): Result<List<ShopItem>, DataError.NetworkError>

}