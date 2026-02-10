package io.github.composegears.valkyrie.sdk.compose.foundation

import androidx.compose.ui.graphics.Color

fun Color.dim(): Color = copy(alpha = 0.6f)

fun Color.disabled(): Color = copy(alpha = 0.38f)
