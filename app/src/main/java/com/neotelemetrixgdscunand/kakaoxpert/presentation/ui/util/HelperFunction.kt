package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.util

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dicoding.asclepius.domain.common.StringRes
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

fun StringRes.getValue(context: Context): String {
    return when (this) {
        is StringRes.Static -> {
            context.getString(resId, args)
        }

        is StringRes.Dynamic -> {
            value
        }
    }
}

