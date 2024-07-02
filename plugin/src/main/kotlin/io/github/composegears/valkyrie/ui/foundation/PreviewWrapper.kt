package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalInspectionMode provides true,
        content = content
    )
}
