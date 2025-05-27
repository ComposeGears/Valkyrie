package io.github.composegears.valkyrie.compose.ui.util

import androidx.compose.ui.graphics.Color

fun Color.disabled(): Color = copy(alpha = 0.38f)

fun Color.dim(): Color = copy(alpha = 0.6f)
