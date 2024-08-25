package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.util

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import com.android.ide.common.vectordrawable.VdPreview
import io.github.composegears.valkyrie.extensions.isSvg
import io.github.composegears.valkyrie.extensions.isXml
import io.github.composegears.valkyrie.parser.svgxml.SvgToXmlParser
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.readText

fun Path.toPainterOrNull(imageScale: Double = 5.0): Painter? = when {
    isSvg -> svgToPainter(imageScale)
    isXml -> xmlToPainter(imageScale)
    else -> null
}

private fun Path.svgToPainter(imageScale: Double): Painter? {
    return runCatching {
        val outPath = createTempFile(name, extension)
        SvgToXmlParser.parse(this, outPath)

        VdPreview.getPreviewFromVectorXml(
            VdPreview.TargetSize.createFromScale(imageScale),
            outPath.readText(),
            StringBuilder(),
        ).toPainter()
    }.getOrElse { null }
}

private fun Path.xmlToPainter(imageScale: Double): Painter? = runCatching {
    VdPreview.getPreviewFromVectorXml(
        VdPreview.TargetSize.createFromScale(imageScale),
        this.readText(),
        StringBuilder(),
    ).toPainter()
}.getOrElse { null }
