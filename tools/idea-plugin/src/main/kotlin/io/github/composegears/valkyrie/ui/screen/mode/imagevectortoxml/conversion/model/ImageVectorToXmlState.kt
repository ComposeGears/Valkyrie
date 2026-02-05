package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model

import androidx.compose.runtime.Stable
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import java.nio.file.Path

@Stable
sealed interface ImageVectorToXmlState {

    data class Content(
        val iconSource: ImageVectorSource,
        val xmlContent: XmlContent,
    ) : ImageVectorToXmlState

    data class Error(
        val message: String,
        val stacktrace: String? = null,
    ) : ImageVectorToXmlState

    data object Loading : ImageVectorToXmlState
}

@Stable
sealed interface ImageVectorSource {
    data class FileBasedIcon(val path: Path) : ImageVectorSource
    data class TextBasedIcon(val kotlinCode: String) : ImageVectorSource
}

@Stable
data class XmlContent(
    val name: String,
    val xmlCode: String,
    val irImageVector: IrImageVector,
)
