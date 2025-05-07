package com.neotelemetrixgdscunand.kamekapp.domain.model

import com.neotelemetrixgdscunand.kakaoxpert.R


abstract class DamageLevelCategory(
    val titleResId: Int,
    val descriptionResId: Int,
    val boundLevel: Pair<Int, Int>,
    val firstSubLevelCategory: DamageLevelSubCategory,
    val secondSubLevelCategory: DamageLevelSubCategory,
    val thirdSubLevelCategory: DamageLevelSubCategory,
) {

    data object Low : DamageLevelCategory(
        titleResId = R.string.tingkat_kerusakan_rendah,
        descriptionResId = R.string.rendah_0_30_buah_rusak,
        boundLevel = Pair(0, 30),
        firstSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.rendah_0_10_rusak,
            subBoundLevel = Pair(0.0, 10.0)
        ),
        secondSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.rendah_11_20_rusak,
            subBoundLevel = Pair(10.01, 20.0)
        ),
        thirdSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.rendah_21_30_rusak,
            subBoundLevel = Pair(20.01, 30.0)
        )
    )

    data object Medium : DamageLevelCategory(
        titleResId = R.string.tingkat_kerusakan_sedang,
        descriptionResId = R.string.sedang_31_60_buah_rusak,
        boundLevel = Pair(30, 60),
        firstSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.sedang_31_40_rusak,
            subBoundLevel = Pair(30.01, 40.0)
        ),
        secondSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.sedang_41_50_rusak,
            subBoundLevel = Pair(40.01, 50.0)
        ),
        thirdSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.sedang_51_60_rusak,
            subBoundLevel = Pair(50.01, 60.0)
        )
    )

    data object High : DamageLevelCategory(
        titleResId = R.string.tingkat_kerusakan_berat,
        descriptionResId = R.string.berat_61_90_buah_rusak,
        boundLevel = Pair(60, 90),
        firstSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.berat_61_70_rusak,
            subBoundLevel = Pair(60.01, 70.0)
        ),
        secondSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.berat_71_80_rusak,
            subBoundLevel = Pair(70.01, 80.0)
        ),
        thirdSubLevelCategory = DamageLevelSubCategory(
            subTitleResId = R.string.berat_81_90_rusak,
            subBoundLevel = Pair(80.01, 90.0)
        )
    )

}

class DamageLevelSubCategory(
    val subTitleResId: Int,
    val subBoundLevel: Pair<Double, Double>,
)

