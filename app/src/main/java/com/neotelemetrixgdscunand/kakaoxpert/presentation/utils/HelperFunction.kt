package com.neotelemetrixgdscunand.kakaoxpert.presentation.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.neotelemetrixgdscunand.kakaoxpert.R
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.AuthError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.PasswordValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.UsernameValidator
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.BoundingBox
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.DetectedCocoa
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.NewsType
import com.neotelemetrixgdscunand.kakaoxpert.presentation.dui.AnalysisSessionDui
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> LifecycleOwner.collectChannelWhenStarted(
    channelFlow: Flow<T>, onCollect: suspend (T) -> Unit
) {
    this.lifecycleScope.launch {
        this@collectChannelWhenStarted.lifecycle.repeatOnLifecycle(
            Lifecycle.State.STARTED
        ) {
            withContext(Dispatchers.Main.immediate) {
                channelFlow.collect(onCollect)
            }
        }
    }
}

private val mapNewsTypeToResourceId = hashMapOf(
    NewsType.COCOA to R.string.semua,
    NewsType.AGRICULTURE to R.string.agrikultur,
    NewsType.AGRICULTURE_TECHNOLOGY to R.string.teknologi,
    NewsType.PLANT_DISEASE to R.string.penyakit_tanaman
)

fun NewsType.toUIText(): UIText {
    val stringResourceId = mapNewsTypeToResourceId[this] ?: R.string.semua
    return UIText.StringResource(stringResourceId)
}

private val mapAuthFieldValidationErrorToStringResource = hashMapOf(
    UsernameValidator.UsernameError.EMPTY to R.string.email_no_hp_tidak_boleh_kosong,
    UsernameValidator.UsernameError.TOO_SHORT to R.string.username_harus_lebih_dari_6_karakter,
    UsernameValidator.UsernameError.NOT_IN_VALID_FORMAT to R.string.username_harus_diisi_dengan_email_atau_nomor_handphone_yang_valid,
    PasswordValidator.PasswordError.EMPTY to R.string.password_tidak_boleh_kosong,
    PasswordValidator.PasswordError.TOO_SHORT to R.string.password_harus_lebih_dari_8_karakter,
    PasswordValidator.PasswordError.NO_UPPERCASE to R.string.password_harus_mengandung_setidaknya_satu_huruf_kapital,
    PasswordValidator.PasswordError.NO_DIGIT to R.string.password_harus_mengandung_setidaknya_satu_angka
)

private val mapNetworkErrorToStringResource = hashMapOf(
    RootNetworkError.UNEXPECTED_ERROR to R.string.telah_terjadi_kesalahan_mohon_coba_lagi,
    RootNetworkError.CONNECTIVITY_UNAVAILABLE to R.string.terdapat_kesalahan_coba_periksa_konektivitas_anda,
    RootNetworkError.NO_CONNECTIVITY_OR_SERVER_UNREACHABLE to R.string.maaf_sepertinya_ada_kesalahan_coba_periksa_koneksi_anda,
    RootNetworkError.REQUEST_TIMEOUT to R.string.terjadi_kesalahan_periska_konektivitas_anda_atau_coba_lagi_nanti,
    RootNetworkError.BAD_REQUEST to R.string.telah_terjadi_kesalahan_mohon_coba_lagi,
    RootNetworkError.FORBIDDEN to R.string.sesi_tidak_valid_mohon_coba_lagi,
    RootNetworkError.UNAUTHORIZED to R.string.sesi_tidak_valid_mohon_coba_lagi,
    RootNetworkError.SERVER_UNAVAILABLE to R.string.maaf_sepertinya_server_sedang_sibuk_coba_lagi_nanti,
    RootNetworkError.INTERNAL_SERVER_ERROR to R.string.telah_terjadi_kesalahan_mohon_coba_lagi_nanti,
    AuthError.USERNAME_IS_ALREADY_REGISTERED to R.string.username_is_already_registered,
    AuthError.INCORRECT_USERNAME_OR_PASSWORD to R.string.email_no_hp_atau_password_salah,
    AuthError.INVALID_TOKEN to R.string.sesi_tidak_valid_mohon_coba_lagi,
    AuthError.INVALID_REGISTER_SESSION to R.string.sesi_tidak_valid_mohon_coba_lagi
)


private val mapErrorToMapErrorToStringResource = hashMapOf(
    RootNetworkError::class to mapNetworkErrorToStringResource,
    AuthError::class to mapNetworkErrorToStringResource,
    PasswordValidator.PasswordError::class to mapAuthFieldValidationErrorToStringResource,
    UsernameValidator.UsernameError::class to mapAuthFieldValidationErrorToStringResource
)

fun Result.Error<*, RootError>.toErrorUIText(): UIText {
    val mapErrorToStringResource = mapErrorToMapErrorToStringResource[this.error::class]
        ?: return UIText.StringResource(R.string.telah_terjadi_kesalahan_mohon_coba_lagi_nanti)

    val stringResource =
        mapErrorToStringResource[error] ?: R.string.telah_terjadi_kesalahan_mohon_coba_lagi_nanti
    return UIText.StringResource(stringResource)
}


fun AnalysisSessionDui.deepCopy(): AnalysisSessionDui {
    return AnalysisSessionDui(
        id = id,
        title = title,
        imageUrlOrPath = imageUrlOrPath,
        date = date,
        predictedPrice = predictedPrice,
        detectedCocoas = detectedCocoas.map { it.deepCopy() }.toImmutableList(),
        solutionEn = solutionEn,
        preventionsEn = preventionsEn,
        solutionId = solutionId,
        preventionsId = preventionsId,
        isSolutionsFromLLM = isSolutionsFromLLM

    )
}

fun DetectedCocoa.deepCopy(): DetectedCocoa {
    return DetectedCocoa(
        id, cacaoNumber, boundingBox.deepCopy(), disease, predictedPriceInIdr, damageLevel
    )
}

fun BoundingBox.deepCopy(): BoundingBox {
    return BoundingBox(
        x1, y1, x2, y2, cx, cy, w, h, cnf, cls, label
    )
}