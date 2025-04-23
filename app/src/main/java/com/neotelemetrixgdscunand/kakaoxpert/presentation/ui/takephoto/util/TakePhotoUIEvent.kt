package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.takephoto.util

import com.dicoding.asclepius.domain.common.StringRes

interface TakePhotoUIEvent {
    data class OnToastMessage(val message: StringRes) : TakePhotoUIEvent
    data class NavigateToResult(
        val sessionName: String,
        val imagePath: String
    ) : TakePhotoUIEvent
}