package io.github.composegears.valkyrie.jewel.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Text
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

@Preview
@Composable
private fun CurrentOsPreview() = PreviewTheme(alignment = Alignment.Center) {
    Text(text = "Current OS: ${rememberCurrentOs()}")
}