package com.neotelemetrixgdscunand.kakaoxpert.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ShopApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.ShopRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.ShopItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepositoryImpl @Inject constructor(
    private val shopApiService: ShopApiService,
    private val dataMapper: DataMapper
) : ShopRepository {
    override suspend fun getShopItems(query: String): Result<List<ShopItem>, DataError.NetworkError> {
        return callApiFromNetwork {
            val response = shopApiService.getShopItems(query)
            val shopItemsDto = response.data ?: return@callApiFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val shopItems = shopItemsDto.mapNotNull {
                dataMapper.mapShopItemDtoToDomain(it)
            }

            Result.Success(shopItems)
        }
    }
}