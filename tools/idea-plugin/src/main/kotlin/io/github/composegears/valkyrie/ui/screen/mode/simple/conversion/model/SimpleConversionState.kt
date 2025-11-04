package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model

import androidx.compose.runtime.Stable
import io.github.composegears.valkyrie.ir.IrImageVector
import java.nio.file.Path

data class SimpleConversionState(
    val iconSource: IconSource,
    val iconContent: IconContent,
)

@Stable
sealed interface IconSource {
    data class FileBasedIcon(val path: Path) : IconSource
    data class StringBasedIcon(val text: String) : IconSource
}

@Stable
data class IconContent(
    val name: String,
    val code: String,
    val irImageVector: IrImageVector,
)
