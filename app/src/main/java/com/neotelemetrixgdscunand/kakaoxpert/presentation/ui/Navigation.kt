package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui


import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface Navigation {

    @Serializable
    sealed interface Page : Navigation

    @Serializable
    sealed interface Route : Navigation {
        val stringVal: String?
            get() = this::class.java.canonicalName
    }

    @Serializable
    data object Splash : Route

    @Serializable
    data object OnBoarding : Route

    @Serializable
    data object Auth : Page {

        @Serializable
        sealed interface AuthRoute : Route

        @Serializable
        data object Login : AuthRoute

        @Serializable
        data object Register : AuthRoute
    }

    @Serializable
    data object Main : Page {

        @Serializable
        sealed interface MainRoute : Route

        @Serializable
        data object Home : MainRoute

        @Serializable
        data object Diagnosis : MainRoute

        @Serializable
        data object Account : MainRoute
    }

    @Serializable
    data object TakePhoto : Route

    @Serializable
    data class DiagnosisResult(
        val newUnsavedSessionImagePath: String?,
        val newSessionName: String?,
        val sessionId: Int?
    ) : Route

    @Serializable
    data object News : Route

    @Serializable
    data object NewsDetail : Route

    @Serializable
    data object Shop : Route

    @Serializable
    data object Weather : Route

    @Serializable
    data object Notification : Route

    @Serializable
    data object CacaoRequest : Route

    @Serializable
    data class CacaoImageDetail(
        val imagePath: String,
        val diagnosisSessionId: Int,
        val detectedCacaoId: Int?
    ) : Route

    @Serializable
    data object Profile : Route
}

