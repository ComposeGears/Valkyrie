package io.github.composegears.valkyrie.sdk.compose.codeviewer

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.luminance

@Stable
val ColorScheme.isLight
    @Composable
    get() = background.luminance() > 0.5f
