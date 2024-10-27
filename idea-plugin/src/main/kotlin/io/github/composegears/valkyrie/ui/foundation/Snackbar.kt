@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackBar = staticCompositionLocalOf<SnackbarHostState> { error("No LocalSnackBar provided") }

@Composable
fun rememberSnackbarState() = remember { SnackbarHostState() }

@Composable
fun rememberSnackbar(): SnackbarManager {
    if (LocalInspectionMode.current) return NoOpSnackbarManager

    val snackbar = LocalSnackBar.current
    val scope = rememberCoroutineScope()

    return remember {
        SnackbarManagerImpl(
            hostState = snackbar,
            scope = scope,
        )
    }
}

interface SnackbarManager {
    fun show(message: String)
}

private object NoOpSnackbarManager : SnackbarManager {
    override fun show(message: String) = Unit
}

private class SnackbarManagerImpl(
    private val hostState: SnackbarHostState,
    private val scope: CoroutineScope,
) : SnackbarManager {

    override fun show(message: String) {
        scope.launch {
            hostState.currentSnackbarData?.dismiss()
            hostState.showSnackbar(message)
        }
    }
}
