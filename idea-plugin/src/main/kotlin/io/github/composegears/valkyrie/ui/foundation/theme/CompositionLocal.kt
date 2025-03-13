@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui.foundation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import java.awt.Component

val LocalComponent = staticCompositionLocalOf<Component> { error("LocalComponent not provided") }
