package com.neotelemetrixgdscunand.kamekapp.data

import com.neotelemetrixgdscunand.kakaoxpert.data.remote.ApiService
import com.neotelemetrixgdscunand.kamekapp.data.utils.fetchFromNetwork
import com.neotelemetrixgdscunand.kamekapp.domain.common.DataError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kamekapp.domain.data.ShopRepository
import com.neotelemetrixgdscunand.kamekapp.domain.model.ShopItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataMapper: DataMapper
) : ShopRepository {
    override suspend fun getShopItems(query: String): Result<List<ShopItem>, DataError.NetworkError> {
        return fetchFromNetwork {
            val response = apiService.getShopItems(query)
            val shopItemsDto = response.data ?: return@fetchFromNetwork Result.Error(
                RootNetworkError.UNEXPECTED_ERROR
            )
            val shopItems = shopItemsDto.mapNotNull {
                dataMapper.mapShopItemDtoToDomain(it)
            }

            Result.Success(shopItems)
        }
    }
}