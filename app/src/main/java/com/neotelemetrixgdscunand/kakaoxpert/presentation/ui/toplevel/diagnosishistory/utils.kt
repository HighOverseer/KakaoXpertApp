package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel.diagnosishistory

import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.SearchAnalysisHistoryCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

fun SearchAnalysisHistoryCategory.getTabText():UIText{
    val textStringResource = when(this){
        SearchAnalysisHistoryCategory.ALL -> R.string.semua
        SearchAnalysisHistoryCategory.TODAY -> R.string.hari_ini
        SearchAnalysisHistoryCategory.WEEK -> R.string.minggu_ini
        SearchAnalysisHistoryCategory.MONTH -> R.string.bulan_ini
    }

    return UIText.StringResource(textStringResource)
}