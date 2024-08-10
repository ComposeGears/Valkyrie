package io.github.composegears.valkyrie.parser.svgxml

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.svgxml.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import io.github.composegears.valkyrie.parser.svgxml.xml.XmlStringParser
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.writeText

data class IconParserOutput(
    val vector: IrImageVector,
    val kotlinName: String,
)

object SvgXmlParser {

    @Throws(IllegalStateException::class)
    fun toIrImageVector(path: Path): IconParserOutput {
        val iconType = IconType.from(path.extension) ?: error("File not SVG or XML")

        val fileName = IconNameFormatter.format(name = path.name)
        val text = when (iconType) {
            SVG -> {
                val tmpPath = createTempFile(suffix = "valkyrie/")
                SvgToXmlParser.parse(path, tmpPath)
                tmpPath.readText()
            }
            XML -> path.readText()
        }

        return IconParserOutput(
            vector = XmlStringParser.parse(text),
            kotlinName = fileName,
        )
    }

    @Throws(IllegalStateException::class)
    fun toIrImageVector(value: String, kotlinName: String = "TempIconName"): IconParserOutput {
        val text = when {
            value.isSvg() -> {
                val tmpInPath = createTempFile(suffix = "valkyrie/").apply { writeText(value) }
                val tmpOutPath = createTempFile(suffix = "valkyrie/")

                SvgToXmlParser.parse(tmpInPath, tmpOutPath)
                tmpOutPath.readText()
            }
            value.isXml() -> value
            else -> error("Unsupported icon type")
        }

        return IconParserOutput(
            vector = XmlStringParser.parse(text),
            kotlinName = kotlinName,
        )
    }

    private fun String.isSvg(): Boolean = contains("<svg")
    private fun String.isXml(): Boolean = contains("<vector")
}
