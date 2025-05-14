package com.neotelemetrixgdscunand.kakaoxpert.domain.usecase

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.CocoaAnalysisSyncType
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.DataError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.SyncSuccess

interface SyncCocoaAnalysisDataUseCase{
    suspend operator fun invoke(syncType: CocoaAnalysisSyncType):Result<SyncSuccess, DataError>
}