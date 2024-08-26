package io.github.composegears.valkyrie.parser.svgxml

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.svgxml.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import io.github.composegears.valkyrie.parser.svgxml.util.capitalized
import io.github.composegears.valkyrie.parser.svgxml.util.removePrefix
import io.github.composegears.valkyrie.parser.svgxml.xml.XmlStringParser
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.readText

data class IconParserOutput(
    val vector: IrImageVector,
    val kotlinName: String,
)

object SvgXmlParser {

    @Throws(IllegalStateException::class)
    fun toIrImageVector(path: Path): IconParserOutput {
        val iconType = IconType.from(path.extension) ?: error("File not SVG or XML")

        val fileName = getIconName(fileName = path.name)
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

    // TODO: extract into separate class
    fun getIconName(fileName: String) = fileName
        .removePrefix("-")
        .removePrefix("_")
        .removeSuffix(".svg")
        .removeSuffix(".xml")
        .removePrefix("ic_")
        .removePrefix("ic-")
        .replace("[^a-zA-Z0-9\\-_ ]".toRegex(), "_")
        .split("_", "-")
        .joinToString(separator = "") { it.lowercase().capitalized() }
}
