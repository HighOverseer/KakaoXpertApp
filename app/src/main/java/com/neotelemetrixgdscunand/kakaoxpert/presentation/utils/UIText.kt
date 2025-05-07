package com.neotelemetrixgdscunand.kamekapp.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext

@Immutable
sealed class UIText {

    class StringResource(
        @StringRes
        val resId: Int,
        val args: Array<Any> = arrayOf()
    ) : UIText()

    class DynamicString(val value: String) : UIText()

    @Composable
    fun getValue(): String {
        return when (this) {
            is StringResource -> LocalContext.current.getString(resId, *args)
            is DynamicString -> value
        }
    }

    fun getValue(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId, *args)
            is DynamicString -> value
        }
    }
}