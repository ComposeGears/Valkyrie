package io.github.composegears.valkyrie.uikit.tooling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun PreviewTheme(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    PreviewWrapper {
        Box(
            modifier = modifier.fillMaxSize(),
            content = content,
            contentAlignment = alignment,
        )
    }
}

@Composable
private fun PreviewWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalInspectionMode provides true,
        content = content,
    )
}
