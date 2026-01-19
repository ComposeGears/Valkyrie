package io.github.composegears.valkyrie.jewel.banner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val LocalGlobalBannerState = staticCompositionLocalOf<BannerState> { error("No LocalGlobalBannerState provided") }

@Composable
fun rememberBannerState() = remember { BannerState() }

@Composable
fun rememberBannerManager(): BannerManager {
    val state = LocalGlobalBannerState.current
    val scope = rememberCoroutineScope()

    return remember(state, scope)  {
        BannerManagerImpl(
            scope = scope,
            bannerState = state
        )
    }
}

interface BannerManager {

    fun show(message: BannerMessage)
}

private class BannerManagerImpl(
    private val bannerState: BannerState,
    private val scope: CoroutineScope,
) : BannerManager {

    override fun show(message: BannerMessage) {
        scope.launch {
            bannerState.currentBannerData?.dismiss()
            bannerState.show(message)
        }
    }
}

@Stable
class BannerState {

    var currentBannerData by mutableStateOf<BannerData?>(null)
        private set

    private val mutex = Mutex()

    suspend fun show(message: BannerMessage) {
        mutex.withLock {
            try {
                suspendCancellableCoroutine { continuation ->
                    currentBannerData = BannerDataImpl(continuation, message)
                }
            } finally {
                currentBannerData = null
            }
        }
    }
}