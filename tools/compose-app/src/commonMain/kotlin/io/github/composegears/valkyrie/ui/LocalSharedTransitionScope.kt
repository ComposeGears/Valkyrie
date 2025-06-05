@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> { error("No scope provided") }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProvideSharedTransition(content: @Composable () -> Unit) {
    SharedTransitionLayout {
        CompositionLocalProvider(
            value = LocalSharedTransitionScope provides this,
            content = content,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope(content: @Composable SharedTransitionScope.() -> Unit) {
    with(LocalSharedTransitionScope.current) {
        content()
    }
}
