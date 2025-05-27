@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

val LocalTheme = staticCompositionLocalOf { LocalThemeConfig() }

class LocalThemeConfig {
    var isDarkTheme by mutableStateOf(false)

    fun toggle() {
        isDarkTheme = !isDarkTheme
    }
}
