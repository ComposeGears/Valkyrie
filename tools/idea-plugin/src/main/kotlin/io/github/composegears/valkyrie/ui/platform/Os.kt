package io.github.composegears.valkyrie.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.skiko.OS.Linux
import org.jetbrains.skiko.OS.MacOS
import org.jetbrains.skiko.OS.Windows
import org.jetbrains.skiko.hostOs

@Composable
fun rememberCurrentOs(): Os = remember {
    when (hostOs) {
        MacOS -> Os.MacOS
        Windows -> Os.Windows
        Linux -> Os.Linux
        else -> Os.Unsupported
    }
}

enum class Os {
    MacOS,
    Windows,
    Linux,
    Unsupported,
}
