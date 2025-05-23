package com.neotelemetrixgdscunand.kakaoxpert.data

import androidx.room.withTransaction
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.CocoaAnalysisDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.CocoaSellPriceInfoDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.EntityMapper
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.CocoaPriceInfoService
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaPriceInfoRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaAverageSellPriceInfo
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.CocoaDiseaseSellPriceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocoaPriceInfoRepositoryImpl @Inject constructor(
    private val cocaPriceInfoService: CocoaPriceInfoService,
    private val cocoaSellPriceInfoDatabase: CocoaSellPriceInfoDatabase,
    private val dataMapper: DataMapper,
    private val entityMapper: EntityMapper,
): CocoaPriceInfoRepository{

    private val cocoaDiseaseSellPriceInfoDao = cocoaSellPriceInfoDatabase.cocoaDiseaseSellPriceInfoDao()
    private val cocoaAverageSellPriceHistoryDao = cocoaSellPriceInfoDatabase.cocoaAverageSellPriceInfoDao()

    override suspend fun syncAllPricesInfo():Result<Unit, DataError> {
        val result = callApiFromNetwork {
            val response = cocaPriceInfoService.getAllLatest()
            val cocoaSellPriceInfoDto = response.data ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            val cocoaAverageSellPriceHistoryEntity = entityMapper.mapCocoaSellPriceInfoDtoToEntity(cocoaSellPriceInfoDto) ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)
            val cocoaDiseaseSellPriceEntities = cocoaSellPriceInfoDto.latestDiseasePriceInfo.mapNotNull {
                entityMapper.mapCocoaDiseasePriceInfoDtoToEntity(it ?: return@mapNotNull null)
            }

            return@callApiFromNetwork Result.Success(Pair(cocoaAverageSellPriceHistoryEntity, cocoaDiseaseSellPriceEntities))
        }

        when(result){
            is Result.Error -> {
                return Result.Error(result.error)
            }
            is Result.Success -> {
                withContext(NonCancellable){
                    val (cocoaAverageSellPriceHistoryEntity, cocoaDiseaseSellPriceEntities) = result.data
                    cocoaSellPriceInfoDatabase.withTransaction {
                        cocoaDiseaseSellPriceInfoDao.deleteAllCocoaDiseaseSellPriceInfo()
                        cocoaDiseaseSellPriceInfoDao.insertCocoaDiseaseSellPriceInfo(
                            cocoaDiseaseSellPriceEntities
                        )
                        cocoaAverageSellPriceHistoryDao.insert(cocoaAverageSellPriceHistoryEntity)
                    }
                }
                return Result.Success(Unit)
            }

        }
    }

    override fun getCocoaPriceInfo(): Flow<CocoaAverageSellPriceInfo?> = flow{
        val isDataExist = cocoaAverageSellPriceHistoryDao.checkIfDataExists()
        if(!isDataExist){
            syncAllPricesInfo()
        }

        emitAll(
            cocoaAverageSellPriceHistoryDao.getLatest().map {
            if (it != null) {
                dataMapper.mapCocoaAverageSellPriceInfoEntityToDomain(it)
            } else null
            }
        )
    }
}