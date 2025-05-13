package com.neotelemetrixgdscunand.kakaoxpert.data

import android.icu.util.Calendar
import androidx.room.withTransaction
import com.neotelemetrixgdscunand.kakaoxpert.data.local.database.CocoaAnalysisDatabase
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.CocoaAnalysisApiService
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.AnalysisSessionDto
import com.neotelemetrixgdscunand.kakaoxpert.data.remote.dto.Response
import com.neotelemetrixgdscunand.kakaoxpert.data.utils.callApiFromNetwork
import com.neotelemetrixgdscunand.kakaoxpert.domain.DomainMapper
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.CocoaAnalysisRepository
import com.neotelemetrixgdscunand.kakaoxpert.domain.data.DataPreference
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSession
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.AnalysisSessionPreview
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.getDetectedDiseaseCacaos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class CocoaAnalysisRepositoryImpl @Inject constructor(
    private val cocoaAnalysisApiService: CocoaAnalysisApiService,
    private val cocoaAnalysisDatabase: CocoaAnalysisDatabase,
    private val dataPreference: DataPreference,
    private val dataMapper: DataMapper,
) : CocoaAnalysisRepository {

    private val cocoaAnalysisPreviewDao by lazy {
        cocoaAnalysisDatabase.cocoaAnalysisPreviewDao()
    }
    private val savedCocoaAnalysisDao by lazy {
        cocoaAnalysisDatabase.savedCocoaAnalysisDao()
    }
    private val unsavedCocoaAnalysisDao by lazy {
        cocoaAnalysisDatabase.unsavedCocoaAnalysisDao()
    }

    private val savedAnalysisSession = MutableStateFlow(
        listOf(
            AnalysisSession(
                id = 101,
                title = "Sampel Kakao Pak Tono"/*"Sampel Kakao Pak Tono"*/,
                imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
                createdAt = System.currentTimeMillis(),
                predictedPrice = 1200f,
                detectedCocoas = getDetectedDiseaseCacaos()
            ),
            AnalysisSession(
                id = 102,
                title = "Sampel Kakao Pak Doni"/*"Sampel Kakao Pak Tono"*/,
                imageUrlOrPath = "https://lh3.googleusercontent.com/fife/ALs6j_FBmyzFhcT48JsWTnlztAKHZJVIxq_KWTNbYFRO8aOkqiJtsH0HDNSDjaqO-1SrWvE8AR85sU2QcU93FD_OWlJ_xZQsL4JpmKESvrTSvPr37FzRfsudWwBrs6HPawtN9qXiqM6JRd4htsh1cuv4SUajVBpjwkIuUVFhfdmMgV-qlZsqn7Ui_W2zj3Kf2Z6BuAeQ-rxzhVoMWxsiXxZL1OVHRz4permiV_G8nOHZLLLbJ_hwKLNHpnFPH15efQarYQtdrfiTXwcYSC_F5_sEasYb-IgUrr1o-_T-__wrnZSc6GEF9YUxQcv2urAt5EqcPdGumJ9GWToq9AtYz9XfD5zB0rsBNl2GhHn23p8DIkq3NCJNUM4hGXSVBLIi5GmBxZXnKlox4vKRarRvtzipvfCpd-iCm6i-66uvx31ghCU2LH5DrqjaPB5-hL30W20F7vbzmHHf2j78Hd6wM7xlhptZChs7_AzZxq9fZ_D9dAAkXaZ9uWuKvKddSFiaajWNapI2mcOae_QKEraLv2t41uxjxAhz1kEhgERN_ZQXAuTpaCudlpaqZRa88LkVwdrmEsfcgAcLAWiEnvcwWJNhjD-B7hWMOx4NvxC_Nz3MAhflOI4-i1u4HDkY6GsZ3mBpzaWiKY6Po6tkjJx1UMTozff4YmDK1W59wqd-YZ8ZQkLCjrRJO_FIkVo9RTaQm-kY_4pCz2f4PjWPbVxsUUiHVQJEB-nus4cK2kGS3DMheO11j2i_EcnuEBXH69fazqZFTU9ahQ4_fARXMxn_k30h5Nkl7YiLMW_R3YK4huwaJUCxwhTTv3vWMeQDkSWOTI2lnks1soQyPRRaBCn3tFYY0BlaR9Gdz__3OrXtFECyGcqVfOEuY-tvGtqwQfHBXNG5XqN8e28CM7Pt2f0gFk3ha9K1m6DOJR2dkLy9monvnx_L4UEPfGWIMa2pnSPOORePp9v_nK380f2t15GPjtpznc4ezON3dHidJV3pkwXpsgXQf0gOjSBWdSf8GtIDoHALK0BpPb5nW0nTMkw7bunhDhk9BCB11WjzbsTLi-Tlau17NQAcFaJE6EQlz0ujZsKMK6oshGveVET6gt7480hPoiGo0LCarO8gDXR8Y6QQDY6mearThrt9xquxCwff-SS-sleeJMk9hqahiAHiureTtT_lwKOJIscCZA7PkDy22tx-gCyquIIh0N3layVLsKX-y07p3Bu18y7KFU1Ld9-RA7IfX6aaOyGyqJMDswpiIytQyQXh9cRlCX0Qjn81YNKmQurktSRZ7c99ht17JBo5dGdmXlVk87kHVthwjqygGU9slR6OhgNW0nOHEgDVS-RDv_7Cy3sA55Vs9P82RO8qC1WW7zlUV5gIETD0o_mgy5zBKEgWRhKixYnSoAuXodVNvUNpG4u7Df29GJkq_exP_iOL0OGS3qPMneG_OC6V9Uez4Jd9nVrNpCnySAXksOGNYUiwt_KkOsc98VRAsb5jV5qrd3ad-bziRcIci09WjDNbMjKJ89bO8yZV8DMCJd0uOVRp3DDHRLh8l5vCha8gtLQqLcMV8htomJtJy7hME1v-C8v9iXt6rIGoRRgYOfnnDNQCwBnRhUxA4WSNMbqnw_qQ2F6LEAODfhJfXfR_di9OyPh2q3K9h3mS=w1920-h927",
                createdAt = System.currentTimeMillis(),
                predictedPrice = 1400f,
                detectedCocoas = getDetectedDiseaseCacaos()
            ),
        )
    )

    private val unsavedAnalysisSession = MutableStateFlow<List<AnalysisSession>>(emptyList())


    override suspend fun saveDiagnosis(
        sessionName: String,
        imageOrUrlPath: String,
        predictedPrice: Float,
        detectedCocoas: List<DetectedCocoa>
    ): Int {
        val createdAt = Calendar.getInstance().timeInMillis

        val unsavedId = Random.nextInt(500_000, 1000_000)

        val newUnsavedAnalysisSession = AnalysisSession(
            id = unsavedId,
            title = sessionName,
            imageUrlOrPath = imageOrUrlPath,
            createdAt = createdAt,
            predictedPrice = predictedPrice,
            detectedCocoas = detectedCocoas
        )
        unsavedAnalysisSession.update { it + listOf(newUnsavedAnalysisSession) }

        val result = callApiFromNetwork {

            val postRequestBody = CocoaAnalysisApiService
                .PostRequestBody.createPostRequestBody(
                    sessionName = sessionName,
                    sessionImagePath = imageOrUrlPath,
                    detectedCocoas = detectedCocoas
                )

            val response = postNewAnalysisSession(postRequestBody)

            val analysisSession = DataMapper.mapAnalysisSessionDtoToDomain(
                response.data ?: throw Exception("error")
            ) ?: throw Exception("error")

            Result.Success(analysisSession)
        }

        if (result is Result.Success) {
            val newSavedAnalysisSession = result.data
            savedAnalysisSession.update {
                (it + listOf(
                    newSavedAnalysisSession
                )).toMutableList()
            }

            withContext(NonCancellable) {
                unsavedAnalysisSession.update { list ->
                    list.filter { it.id != newUnsavedAnalysisSession.id }
                }
            }

            return newSavedAnalysisSession.id
        } else {

            return newUnsavedAnalysisSession.id
        }
    }

    override suspend fun syncAllSessionsFromRemote(): Result<Unit, DataError.NetworkError> {
        val needToSync = dataPreference.needToSync()

        if (!needToSync) return Result.Success(Unit)

        try {
            dataPreference.setIsSyncing(true)

            val result = callApiFromNetwork {
                val response = cocoaAnalysisApiService.getAllAnalysisSessionPreviews()
                withContext(Dispatchers.Default) {
                    val listAnalysisSessionPreviewDto = response.data
                    val listAnalysisSessionPreview = listAnalysisSessionPreviewDto
                        ?.mapNotNull {
                            dataMapper.mapCocoaAnalysisSessionPreviewDtoToDomain(it)
                        } ?: emptyList()

                    Result.Success(listAnalysisSessionPreview)
                }
            }

            val finalResult: Result<Unit, DataError.NetworkError> = when (result) {
                is Result.Success -> {
                    val analysisSessionPreviewsEntities = withContext(Dispatchers.Default) {
                        result.data.map {
                            DataMapper.mapAnalysisSessionPreviewToEntity(it)
                        }
                    }

                    cocoaAnalysisDatabase.withTransaction {
                        cocoaAnalysisPreviewDao.setAllIsDeletedToTrue()
                        cocoaAnalysisPreviewDao.insertAll(analysisSessionPreviewsEntities)
                        cocoaAnalysisPreviewDao.deleteAllIsDeleted()
                    }
                    withContext(NonCancellable) {
                        dataPreference.updateLastSyncTime()
                    }

                    Result.Success(Unit)
                }

                is Result.Error -> Result.Error(result.error)
            }
            return finalResult
        } finally {
            withContext(NonCancellable) {
                dataPreference.setIsSyncing(false)
            }
        }
    }


    override fun getAllSessionPreviews(): Flow<List<AnalysisSessionPreview>> = flow {
        val result = callApiFromNetwork {
            val response = cocoaAnalysisApiService.getAllAnalysisSessionPreviews()
            withContext(Dispatchers.Default) {
                val listAnalysisSessionPreviewDto = response.data
                val listAnalysisSessionPreview = listAnalysisSessionPreviewDto
                    ?.mapNotNull {
                        dataMapper.mapCocoaAnalysisSessionPreviewDtoToDomain(it)
                    } ?: emptyList()

                Result.Success(listAnalysisSessionPreview)
            }

        }

        val listFromNetwork = if (result is Result.Success) {
            result.data
        } else emptyList()

        emit(listFromNetwork)

        val analysisSessionPreviewLocal = combine(savedAnalysisSession, unsavedAnalysisSession)
        { savedList, unsavedList ->
            withContext(Dispatchers.Default) {
                val mappedSavedListDeferred = async {
                    savedList.map {
                        DomainMapper.mapDiagnosisSessionToPreview(it)
                    }
                }
                val mappedUnsavedListDeferred = async {
                    unsavedList.map {
                        DomainMapper.mapDiagnosisSessionToPreview(it)
                    }
                }
                (mappedUnsavedListDeferred.await() + listFromNetwork + mappedSavedListDeferred.await())
                    .toSet().toList()

            }
        }
        emitAll(analysisSessionPreviewLocal)
    }

    override suspend fun getDiagnosisSession(id: Int): AnalysisSession {
        val output = savedAnalysisSession.value.find { it.id == id }
        if (output != null) return output

        val result = callApiFromNetwork {
            val response = cocoaAnalysisApiService.getAnalysisSessionById(id)
            val analysisSessionDto = response.data
            val analysisSession = dataMapper.mapAnalysisSessionDtoToDomain(
                analysisSessionDto = analysisSessionDto ?: return@callApiFromNetwork Result.Error(
                    RootNetworkError.UNEXPECTED_ERROR
                )
            )
            if (analysisSession == null) {
                Result.Error(RootNetworkError.UNEXPECTED_ERROR)
            } else {
                Result.Success(analysisSession)
            }
        }
        if (result is Result.Success) {
            savedAnalysisSession.update {
                (it + listOf(result.data))
            }
            return result.data
        } else throw Exception("error")
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