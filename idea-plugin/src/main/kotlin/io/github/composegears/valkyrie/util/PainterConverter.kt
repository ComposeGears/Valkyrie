package io.github.composegears.valkyrie.util

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import com.android.ide.common.vectordrawable.VdPreview
import io.github.composegears.valkyrie.parser.svgxml.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import io.github.composegears.valkyrie.parser.svgxml.util.isSvg
import io.github.composegears.valkyrie.parser.svgxml.util.isXml
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.readText
import kotlin.io.path.writeText

object PainterConverter {

    fun from(text: String, iconType: IconType, imageScale: Double = 5.0): Painter? {
        return when (iconType) {
            SVG -> {
                val tmpInPath = createTempFile().apply { writeText(text) }
                val tmpOutPath = createTempFile()
                SvgToXmlParser.parse(tmpInPath, tmpOutPath)

                xmlToPainter(text = tmpOutPath.readText(), imageScale = imageScale)
            }
            XML -> xmlToPainter(text = text, imageScale = imageScale)
        }
    }

    fun from(path: Path, imageScale: Double = 5.0): Painter? {
        return when {
            path.isSvg -> {
                val outPath = createTempFile()
                SvgToXmlParser.parse(path, outPath)

                xmlToPainter(text = outPath.readText(), imageScale = imageScale)
            }
            path.isXml -> xmlToPainter(text = path.readText(), imageScale = imageScale)
            else -> return null
        }
    }

    private fun xmlToPainter(text: String, imageScale: Double): Painter? {
        return runCatching {
            VdPreview.getPreviewFromVectorXml(
                VdPreview.TargetSize.createFromScale(imageScale),
                text,
                StringBuilder(),
            ).toPainter()
        }.getOrNull()
    }
}
