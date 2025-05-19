package com.neotelemetrixgdscunand.kakaoxpert.data

import android.icu.util.Calendar
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.CocoaAnalysisDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.EntityMapper
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.SavedDetectedCocoaEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.entity.UnsavedCocoaAnalysisEntity
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.CocoaAnalysisApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.getDetectedDiseaseCacaos
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class CocoaAnalysisRepositoryImpl @Inject constructor(
    private val cocoaAnalysisApiService: CocoaAnalysisApiService,
    private val cocoaAnalysisDatabase: CocoaAnalysisDatabase,
    private val dataMapper: DataMapper,
    private val entityMapper: EntityMapper
) : CocoaAnalysisRepository {

    private val cocoaAnalysisPreviewDao by lazy { cocoaAnalysisDatabase.cocoaAnalysisPreviewDao() }
    private val savedCocoaAnalysisDao by lazy { cocoaAnalysisDatabase.savedCocoaAnalysisDao() }
    private val unsavedCocoaAnalysisDao by lazy { cocoaAnalysisDatabase.unsavedCocoaAnalysisDao() }
    private val savedDetectedCocoaDao by lazy { cocoaAnalysisDatabase.savedDetectedCocoaDao() }
    private val unsavedDetectedCocoaDao by lazy { cocoaAnalysisDatabase.unsavedDetectedCocoaDao() }

    val analysisSessionSamples = listOf(
        AnalysisSession(
            id = -1,
            title = "Sampel Kakao Pak Tono"/*"Sampel Kakao Pak Tono"*/,
            imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
            createdAt = System.currentTimeMillis(),
            predictedPrice = 1200f,
            detectedCocoas = getDetectedDiseaseCacaos()
        ),
        AnalysisSession(
            id = -2,
            title = "Sampel Kakao Pak Doni"/*"Sampel Kakao Pak Tono"*/,
            imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
            createdAt = System.currentTimeMillis(),
            predictedPrice = 1400f,
            detectedCocoas = getDetectedDiseaseCacaos()
        ),
    )

    override fun getSomeSessionPreviews(): Flow<List<AnalysisSessionPreview>> {
        return cocoaAnalysisPreviewDao.getSome()
            .map { list ->
                list.map {
                    entityMapper.mapCocoaAnalysisPreviewRelationToDomain(it)
                }
            }
    }

    override suspend fun saveDiagnosis(
        sessionName: String,
        imageOrUrlPath: String,
        predictedPrice: Float,
        detectedCocoas: List<DetectedCocoa>
    ): Int {
        val result = callApiFromNetwork {
            val postRequestBody = CocoaAnalysisApiService
                .PostRequestBody.createPostRequestBody(
                    sessionName = sessionName,
                    sessionImagePath = imageOrUrlPath,
                    detectedCocoas = detectedCocoas
                )

            val response = postNewAnalysisSession(postRequestBody)
            val analysisSessionDto = response.data ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            val newSavedCocoaAnalysisEntity = entityMapper.mapCocoaAnalysisDtoToSavedEntity(analysisSessionDto)
                ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            val savedDetectedCocoaEntities =
                analysisSessionDto.detectedCocoas?.map {
                    entityMapper.mapDetectedCocoaDtoToSavedEntity(
                        it,
                        newSavedCocoaAnalysisEntity.sessionId
                    )
                        ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)
                }
                    ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            Result.Success(Pair(newSavedCocoaAnalysisEntity, savedDetectedCocoaEntities))
        }

        if (result is Result.Success) {
            val (newSavedAnalysisSessionEntity, newSavedDetectedCocoaEntities) = result.data
            withContext(NonCancellable){
                val newCocoaAnalysisPreview = entityMapper
                    .mapSavedCocoaAnalysisEntityToCocoaAnalysisPreviewEntity(
                        newSavedAnalysisSessionEntity
                    )
                cocoaAnalysisDatabase.withTransaction {
                    cocoaAnalysisPreviewDao.insertAll(listOf(newCocoaAnalysisPreview))
                    savedCocoaAnalysisDao.insertAll(listOf(newSavedAnalysisSessionEntity))
                    savedDetectedCocoaDao.insertAll(newSavedDetectedCocoaEntities)
                }

            }
            return newSavedAnalysisSessionEntity.sessionId

        } else {
            val createdAt = System.currentTimeMillis()
            val unsavedCocoaAnalysisEntity = UnsavedCocoaAnalysisEntity(
                sessionName = sessionName,
                sessionImagePath = imageOrUrlPath,
                createdAt = createdAt
            )
            val unsavedSessionId = withContext(NonCancellable){
                cocoaAnalysisDatabase.withTransaction {
                    val unsavedSessionId = unsavedCocoaAnalysisDao.insert(unsavedCocoaAnalysisEntity).toInt()
                    val unsavedDetectedCocoaEntities = detectedCocoas.map {
                        entityMapper.mapDetectedCocoaToUnsavedEntity(it, unsavedSessionId)
                    }
                    unsavedDetectedCocoaDao.insertAll(unsavedDetectedCocoaEntities)
                    unsavedSessionId
                }
            }
            return unsavedSessionId
        }
    }


    override suspend fun getDiagnosisSession(sessionId: Int): Result<AnalysisSession, DataError.NetworkError> {
        val unsavedCocoaAnalysisRelation = unsavedCocoaAnalysisDao.getById(sessionId)

        if(unsavedCocoaAnalysisRelation != null){
            val cocoaAnalysisSessionDomain = entityMapper.mapUnsavedCocoaAnalysisEntityToDomain(
                unsavedCocoaAnalysisRelation.unsavedCocoaAnalysisEntity, unsavedCocoaAnalysisRelation.unsavedDetectedCocoaEntities
            )
            return Result.Success(cocoaAnalysisSessionDomain)
        }

        val savedCocoaAnalysisRelation = savedCocoaAnalysisDao.getById(sessionId)

        if(savedCocoaAnalysisRelation != null){
            val cocoaAnalysisSessionDomain = entityMapper.mapSavedCocoaAnalysisEntityToDomain(
                savedCocoaAnalysisRelation.savedCocoaAnalysisEntity, savedCocoaAnalysisRelation.savedDetectedCocoaEntities
            )
            return Result.Success(cocoaAnalysisSessionDomain)
        }

        val cocoaAnalysisSessionDtoResult = getCocoaAnalysisSessionByIdFromNetwork(sessionId)
        when(cocoaAnalysisSessionDtoResult){
            is Result.Error -> return Result.Error(cocoaAnalysisSessionDtoResult.error)
            is Result.Success -> {
                val analysisSessionEntity = entityMapper.mapCocoaAnalysisDtoToSavedEntity(
                    analysisSessionDto = cocoaAnalysisSessionDtoResult.data
                )?: return Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                val detectedCocoaEntities = cocoaAnalysisSessionDtoResult.data.detectedCocoas?.mapNotNull {
                    entityMapper.mapDetectedCocoaDtoToSavedEntity(it, sessionId)
                }?:return Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                withContext(NonCancellable){
                    cocoaAnalysisDatabase.withTransaction {
                        cocoaAnalysisPreviewDao.updateLastSyncedTime(
                            listOf(analysisSessionEntity.sessionId),
                            System.currentTimeMillis()
                        )
                        savedCocoaAnalysisDao.insertAll(listOf(analysisSessionEntity))
                        savedDetectedCocoaDao.insertAll(detectedCocoaEntities)

                    }
                }

                val justSavedCocoaAnalysisRelation = savedCocoaAnalysisDao.getById(analysisSessionEntity.sessionId)
                if(justSavedCocoaAnalysisRelation != null){
                    val cocoaAnalysisSessionDomain = entityMapper.mapSavedCocoaAnalysisEntityToDomain(
                        justSavedCocoaAnalysisRelation.savedCocoaAnalysisEntity, justSavedCocoaAnalysisRelation.savedDetectedCocoaEntities
                    )
                    return Result.Success(cocoaAnalysisSessionDomain)

                }else return Result.Error(RootNetworkError.UNEXPECTED_ERROR)
            }
        }
    }

    private suspend fun getCocoaAnalysisSessionByIdFromNetwork(
        sessionId: Int
    ):Result<AnalysisSessionDto, DataError.NetworkError>{
        return callApiFromNetwork {
            val response = cocoaAnalysisApiService.getAnalysisSessionById(sessionId)
            val analysisSessionDto = response.data ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

            return@callApiFromNetwork Result.Success(analysisSessionDto)
        }
    }

    override fun getAllSessionPreviews(query:String): Flow<PagingData<AnalysisSessionPreview>>{
        return Pager(
            config = PagingConfig(
                pageSize = CocoaAnalysisRepository.PAGE_SIZE,
                enablePlaceholders = CocoaAnalysisRepository.ENABLE_PLACE_HOLDERS
            ),
            pagingSourceFactory = {
                cocoaAnalysisPreviewDao.getAllAsPagingSource(query)
            }
        ).flow
            .flowOn(Dispatchers.IO)
            .map { pagingData ->
            pagingData.map {
                    entityMapper.mapCocoaAnalysisPreviewRelationToDomain(it)
                }
            }.flowOn(Dispatchers.Default)
    }

    override suspend fun syncAllSessionPreviewsFromRemote(): Result<Unit, DataError> {
        val result = callApiFromNetwork {
            val response = cocoaAnalysisApiService.getAllAnalysisSessionPreviews()
            withContext(Dispatchers.Default) {
                val listAnalysisSessionPreviewDto = response.data
                val listAnalysisSessionPreview = listAnalysisSessionPreviewDto?.mapNotNull {
                    entityMapper.mapCocoaAnalysisSessionPreviewDtoToEntity(it)
                }

                Result.Success(listAnalysisSessionPreview)
            }
        }

        return when (result) {
            is Result.Success -> {
                val analysisSessionPreviewsEntities =
                    result.data ?: return Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                cocoaAnalysisDatabase.withTransaction {
                    cocoaAnalysisPreviewDao.setAllIsDeletedToTrue()
                    cocoaAnalysisPreviewDao.insertAll(analysisSessionPreviewsEntities)
                    cocoaAnalysisPreviewDao.updateAll(analysisSessionPreviewsEntities)
                    cocoaAnalysisPreviewDao.deleteAllIsDeleted()
                    cocoaAnalysisPreviewDao.updateAllLastSyncedTime(System.currentTimeMillis())
                }

                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun syncAllUnsavedSessionsFromLocal(): Result<Unit, DataError> {
        val unsavedCocoaAnalysisSessionWithDetectedCocoas =
            unsavedCocoaAnalysisDao.getFiveOldestData()

        if (unsavedCocoaAnalysisSessionWithDetectedCocoas.isEmpty()) return Result.Success(Unit)

        return supervisorScope {
            val resultsDeferred =
                unsavedCocoaAnalysisSessionWithDetectedCocoas.map { unsavedAnalysis ->
                    async {
                        val sessionName = unsavedAnalysis.unsavedCocoaAnalysisEntity.sessionName
                        val sessionImagePath =
                            unsavedAnalysis.unsavedCocoaAnalysisEntity.sessionImagePath
                        val detectedCocoas = withContext(Dispatchers.Default) {
                            unsavedAnalysis.unsavedDetectedCocoaEntities.map {
                                entityMapper.mapUnsavedDetectedCocoaEntityToDomain(it)
                                    ?: throw CancellationException()
                            }
                        }

                        callApiFromNetwork {
                            val postRequestBody = CocoaAnalysisApiService
                                .PostRequestBody.createPostRequestBody(
                                    sessionName = sessionName,
                                    sessionImagePath = sessionImagePath,
                                    detectedCocoas = detectedCocoas
                                )

                            val response = postNewAnalysisSession(postRequestBody)
                            val cocoaAnalysisSessionDto =
                                response.data ?: return@callApiFromNetwork Result.Error(
                                    RootNetworkError.UNEXPECTED_ERROR
                                )

                            withContext(Dispatchers.Default) {
                                val savedCocoaAnalysisEntity =
                                    entityMapper.mapCocoaAnalysisDtoToSavedEntity(
                                        cocoaAnalysisSessionDto
                                    )
                                        ?: return@withContext Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                                val savedDetectedCocoaEntities =
                                    cocoaAnalysisSessionDto.detectedCocoas?.map {
                                        entityMapper.mapDetectedCocoaDtoToSavedEntity(
                                            it,
                                            savedCocoaAnalysisEntity.sessionId
                                        )
                                            ?: return@withContext Result.Error(RootNetworkError.UNEXPECTED_ERROR)
                                    }
                                        ?: return@withContext Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                                Result.Success(
                                    Triple(
                                        unsavedAnalysis.unsavedCocoaAnalysisEntity.unsavedSessionId,
                                        savedCocoaAnalysisEntity,
                                        savedDetectedCocoaEntities
                                    )
                                )
                            }
                        }
                    }
                }

            val results = resultsDeferred.awaitAll()
            val successResults: List<Result.Success<Triple<Int, SavedCocoaAnalysisEntity, List<SavedDetectedCocoaEntity>>, DataError.NetworkError>>
            withContext(NonCancellable) {
                successResults = results
                    .filterIsInstance<Result.Success<Triple<Int, SavedCocoaAnalysisEntity, List<SavedDetectedCocoaEntity>>, DataError.NetworkError>>()

                val allWasUnsavedSessionsIds = successResults.map { it.data.first }
                val allJustSavedSessions = successResults.map { it.data.second }
                val allJustSavedDetectedCocoas = successResults.map { it.data.third }.flatten()
                val allJustSavedCocoaAnalysisPreviews = allJustSavedSessions.map {
                    entityMapper.mapSavedCocoaAnalysisEntityToCocoaAnalysisPreviewEntity(it)
                }

                cocoaAnalysisDatabase.withTransaction {
                    cocoaAnalysisPreviewDao.insertAll(allJustSavedCocoaAnalysisPreviews)
                    unsavedCocoaAnalysisDao.deleteAllByIds(allWasUnsavedSessionsIds)
                    savedCocoaAnalysisDao.insertAll(allJustSavedSessions)
                    savedDetectedCocoaDao.insertAll(allJustSavedDetectedCocoas)

                }
            }

            return@supervisorScope when {
                successResults.isEmpty() -> {
                    val firstErrorResult =
                        results
                            .filterIsInstance<Result.Error<Triple<Int, SavedCocoaAnalysisEntity, List<SavedDetectedCocoaEntity>>, DataError.NetworkError>>()
                    return@supervisorScope Result.Error(
                        firstErrorResult.firstOrNull()?.error ?: RootNetworkError.UNEXPECTED_ERROR
                    )
                }

                else -> Result.Success(Unit)
            }
        }
    }

    override suspend fun syncAllSavedSessionsFromRemote(): Result<Unit, DataError> {
        val sessionIdsWhichDoNotHaveCachedDetailsYet =
            cocoaAnalysisPreviewDao.getFiveOldestWhichNotHaveDetailsYet()

        if (sessionIdsWhichDoNotHaveCachedDetailsYet.isEmpty()) return Result.Success(Unit)

        return supervisorScope {
            val resultsDeferred = sessionIdsWhichDoNotHaveCachedDetailsYet.map { sessionId ->
                async {
                    callApiFromNetwork {
                        val response = cocoaAnalysisApiService.getAnalysisSessionById(sessionId)
                        val analysisSessionDto = response.data

                        val savedAnalysisSessionEntity = entityMapper.mapCocoaAnalysisDtoToSavedEntity(
                            analysisSessionDto = analysisSessionDto
                                ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)
                        )
                            ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                        val savedDetectedCocoaEntities =
                            analysisSessionDto.detectedCocoas?.map {
                                entityMapper.mapDetectedCocoaDtoToSavedEntity(
                                    it,
                                    savedAnalysisSessionEntity.sessionId
                                )
                                    ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)
                            }
                                ?: return@callApiFromNetwork Result.Error(RootNetworkError.UNEXPECTED_ERROR)

                        Result.Success(Pair(savedAnalysisSessionEntity, savedDetectedCocoaEntities))
                    }
                }
            }

            val results = resultsDeferred.awaitAll()
            val successResults: List<Result.Success<Pair<SavedCocoaAnalysisEntity, List<SavedDetectedCocoaEntity>>, DataError.NetworkError>>
            withContext(NonCancellable) {
                successResults =
                    results.filterIsInstance<Result.Success<Pair<SavedCocoaAnalysisEntity, List<SavedDetectedCocoaEntity>>, DataError.NetworkError>>()
                val allJustGotSavedAnalysisSessionEntities = successResults.map { it.data.first }
                val allJustGotSavedDetectedCocoas = successResults.map { it.data.second }.flatten()
                val allJustGotSavedCocoaAnalysisIds = allJustGotSavedAnalysisSessionEntities.map {
                    it.sessionId
                }

                cocoaAnalysisDatabase.withTransaction {
                    savedCocoaAnalysisDao.insertAll(allJustGotSavedAnalysisSessionEntities)
                    savedDetectedCocoaDao.insertAll(allJustGotSavedDetectedCocoas)
                    cocoaAnalysisPreviewDao.updateLastSyncedTime(
                        allJustGotSavedCocoaAnalysisIds,
                        System.currentTimeMillis()
                    )
                }

            }

            return@supervisorScope when {
                successResults.isEmpty() -> {
                    val firstErrorResult =
                        results
                            .filterIsInstance<Result.Error<SavedCocoaAnalysisEntity, DataError.NetworkError>>()
                    return@supervisorScope Result.Error(
                        firstErrorResult.firstOrNull()?.error ?: RootNetworkError.UNEXPECTED_ERROR
                    )
                }

                else -> Result.Success(Unit)
            }
        }
    }

    override suspend fun resetAllLocalData() {
        cocoaAnalysisDatabase.withTransaction {
            cocoaAnalysisPreviewDao.resetTableData()
            savedCocoaAnalysisDao.resetTableData()
            savedDetectedCocoaDao.resetTableData()
            unsavedCocoaAnalysisDao.resetTableData()
            unsavedDetectedCocoaDao.resetTableData()
        }
    }

    private suspend fun postNewAnalysisSession(
        postRequestBody: CocoaAnalysisApiService.PostRequestBody
    ): Response<AnalysisSessionDto> {
        return cocoaAnalysisApiService.addNewAnalysisHistory(
            sessionImage = postRequestBody.sessionImage,
            sessionName = postRequestBody.sessionName,
            detectedDiseaseIds = postRequestBody.detectedDiseaseIds,
            varietyIds = postRequestBody.varietyIds,
            damagePercentage = postRequestBody.damagePercentage,
            cocoaNumbers = postRequestBody.cocoaNumbers,
            bbCoordinatesLeft = postRequestBody.bbCoordinatesLeft,
            bbCoordinatesTop = postRequestBody.bbCoordinatesTop,
            bbCoordinatesRight = postRequestBody.bbCoordinatesRight,
            bbCoordinatesBottom = postRequestBody.bbCoordinatesBottom,
            bbWidths = postRequestBody.bbWidths,
            bbHeights = postRequestBody.bbHeights,
            bbCentersX = postRequestBody.bbCentersX,
            bbCentersY = postRequestBody.bbCentersY,
            bbConfidences = postRequestBody.bbConfidences,
            bbCls = postRequestBody.bbCls,
            bbLabels = postRequestBody.bbLabels
        )
    }


}