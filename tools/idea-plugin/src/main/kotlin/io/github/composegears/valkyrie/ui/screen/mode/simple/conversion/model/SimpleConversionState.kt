package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model

import androidx.compose.runtime.Stable
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import java.nio.file.Path

@Stable
sealed interface SimpleConversionState {

    data class ConversionState(
        val iconSource: IconSource,
        val iconContent: IconContent,
    ) : SimpleConversionState

    data class Error(
        val message: String,
        val stacktrace: String?,
    ) : SimpleConversionState

    data object Loading : SimpleConversionState
}

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
