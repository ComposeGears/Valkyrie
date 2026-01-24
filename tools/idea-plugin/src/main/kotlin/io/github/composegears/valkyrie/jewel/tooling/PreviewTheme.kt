package io.github.composegears.valkyrie.jewel.tooling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import io.github.composegears.valkyrie.jewel.banner.LocalGlobalBannerState
import io.github.composegears.valkyrie.jewel.banner.rememberBannerState

@Composable
fun PreviewTheme(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        Box(
            modifier = modifier.fillMaxSize(),
            content = content,
            contentAlignment = alignment,
        )
    }
}

@Composable
fun BannerPreviewTheme(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    CompositionLocalProvider(LocalGlobalBannerState provides rememberBannerState()) {
        PreviewTheme(
            modifier = modifier,
            alignment = alignment,
            content = content,
        )
    }
}
