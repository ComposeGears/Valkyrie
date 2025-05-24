package io.github.composegears.valkyrie.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun rememberCurrentOs(): Os {
    if (LocalInspectionMode.current) return Os.MacOS

    val osProperty = System.getProperty("os.name")

    return when {
        osProperty.contains("mac", ignoreCase = true) -> Os.MacOS
        osProperty.contains("win", ignoreCase = true) -> Os.Windows
        osProperty.contains("nux", ignoreCase = true) -> Os.Linux
        else -> Os.Unknown
    }
}

enum class Os {
    MacOS,
    Windows,
    Linux,
    Unknown,
}
