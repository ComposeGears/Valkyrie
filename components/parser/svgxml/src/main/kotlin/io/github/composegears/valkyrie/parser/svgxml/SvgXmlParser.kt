package io.github.composegears.valkyrie.parser.svgxml

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.jvm.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.jvm.xml.XmlToImageVectorParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText

data class IconParserOutput(
    val iconType: IconType,
    val irImageVector: IrImageVector,
    val iconName: String,
)

object SvgXmlParser {

    @Throws(IllegalStateException::class)
    fun toIrImageVector(path: Path): IconParserOutput {
        val iconType = IconType.from(path) ?: error("$path must be an SVG or XML file.")

        val fileName = IconNameFormatter.format(name = path.name)
        val text = when (iconType) {
            SVG -> SvgToXmlParser.parse(path)
            XML -> path.readText()
        }

        return IconParserOutput(
            iconType = iconType,
            irImageVector = XmlToImageVectorParser.parse(text),
            iconName = fileName,
        )
    }

    @Throws(IllegalStateException::class)
    fun toIrImageVector(
        value: String,
        iconName: String,
    ): IconParserOutput {
        val iconType = IconType.from(value) ?: error("Unsupported icon type")

        val text = when (iconType) {
            SVG -> SvgToXmlParser.parse(value)
            XML -> value
        }

        return IconParserOutput(
            irImageVector = XmlToImageVectorParser.parse(text),
            iconName = iconName,
            iconType = iconType,
        )
    }
}
