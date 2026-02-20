package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model

import androidx.compose.runtime.Stable
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import java.nio.file.Path

@Stable
sealed interface ImageVectorXmlState {

    data class Content(
        val iconSource: ImageVectorSource,
        val xmlContent: XmlContent,
    ) : ImageVectorXmlState

    data class Error(
        val message: String,
        val stacktrace: String? = null,
    ) : ImageVectorXmlState

    data object Loading : ImageVectorXmlState
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
