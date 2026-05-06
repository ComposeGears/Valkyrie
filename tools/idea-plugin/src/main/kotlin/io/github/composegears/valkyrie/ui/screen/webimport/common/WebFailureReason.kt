package io.github.composegears.valkyrie.ui.screen.webimport.common

import java.io.IOException

enum class WebFailureReason(val bundleKey: String) {
    Network("web.import.error.reason.network"),
    Server("web.import.error.reason.server"),
    Unknown("web.import.error.reason.unknown"),
}

fun Throwable.toWebFailureReason(): WebFailureReason = when (this) {
    is IOException -> WebFailureReason.Network
    is IllegalStateException -> WebFailureReason.Server
    else -> WebFailureReason.Unknown
}
