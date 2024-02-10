package com.sudo_pacman.contactonoff.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.launch(lifecycle: Lifecycle, lifecycleScope: LifecycleCoroutineScope, block: (T) -> Unit) {
    onEach {
        block.invoke(it)
    }
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycleScope)

}