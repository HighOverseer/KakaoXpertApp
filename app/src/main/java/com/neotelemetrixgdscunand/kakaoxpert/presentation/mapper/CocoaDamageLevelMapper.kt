package com.neotelemetrixgdscunand.kakaoxpert.presentation.mapper

import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DamageLevelCategory
import com.neotelemetrixgdscunand.kakaoxpert.presentation.utils.UIText

object CocoaDamageLevelMapper {

    private val mapDamageLevelToFormatResId = hashMapOf(
        DamageLevelCategory.LOW to R.string.ringan_buah_rusak,
        DamageLevelCategory.MEDIUM to R.string.sedang_buah_rusak,
        DamageLevelCategory.HIGH to R.string.berat_buah_rusak,
    )

    fun getFormattedDamageLevelDescription(
        damageLevel: Float
    ): UIText {
        val damageLevelCategory = DamageLevelCategory.getFromDamageLevel(damageLevel = damageLevel)

        val formatResId =
            mapDamageLevelToFormatResId[damageLevelCategory] ?: R.string.ringan_buah_rusak

        return UIText.StringResource(formatResId, arrayOf(damageLevel.toInt().toString()))
    }
}