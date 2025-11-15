package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model

import androidx.compose.runtime.Stable
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import java.nio.file.Path

data class ImageVectorToXmlState(
    val iconSource: ImageVectorSource,
    val xmlContent: XmlContent,
)

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
