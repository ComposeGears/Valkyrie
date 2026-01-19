package io.github.composegears.valkyrie.jewel.banner

import androidx.compose.runtime.Stable
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation

@Stable
interface BannerData {
    val message: BannerMessage

    fun dismiss()
}

internal class BannerDataImpl(
    private val continuation: CancellableContinuation<Unit>,
    override val message: BannerMessage
) : BannerData {

    override fun dismiss() {
        if (continuation.isActive) continuation.resume(Unit)
    }
}