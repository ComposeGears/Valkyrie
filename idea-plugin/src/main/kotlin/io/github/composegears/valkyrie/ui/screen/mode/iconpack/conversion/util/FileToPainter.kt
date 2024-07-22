package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.util

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import com.android.ide.common.vectordrawable.VdPreview
import io.github.composegears.valkyrie.parser.SvgToXmlParser
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.readText

fun Path.toPainterOrNull(): Painter? = when (extension) {
    "svg" -> svgToPainter()
    "xml" -> xmlToPainter()
    else -> error("Unsupported file type: $extension")
}

private fun Path.svgToPainter(): Painter? {
    return runCatching {
        val outPath = createTempFile(name, extension)
        SvgToXmlParser.parse(this, outPath)

        VdPreview.getPreviewFromVectorXml(
            VdPreview.TargetSize.createFromScale(5.0),
            outPath.readText(),
            StringBuilder(),
        ).toPainter()
    }.getOrElse { null }
}

private fun Path.xmlToPainter(): Painter? = runCatching {
    VdPreview.getPreviewFromVectorXml(
        VdPreview.TargetSize.createFromScale(5.0),
        this.readText(),
        StringBuilder(),
    ).toPainter()
}.getOrElse { null }
