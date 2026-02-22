package io.github.composegears.valkyrie.sdk.compose.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> ObserveEvent(flow: Flow<T>, onEach: suspend (T) -> Unit) {
    val latestOnEach by rememberUpdatedState(onEach)

    LaunchedEffect(flow) {
        flow.collect(latestOnEach)
    }
}
