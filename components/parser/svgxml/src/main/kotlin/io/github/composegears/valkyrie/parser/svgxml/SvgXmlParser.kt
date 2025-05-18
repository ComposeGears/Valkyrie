package io.github.composegears.valkyrie.parser.svgxml

import io.github.composegears.valkyrie.parser.jvm.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.jvm.xml.XmlToImageVectorParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.readText

object SvgXmlParser {

    @Throws(IllegalStateException::class)
    fun toIrImageVector(path: Path): IconParserOutput {
        val fileName = IconNameFormatter.format(name = path.name)
        require(path.exists()) { "$path does not exist" }
        require(path.isRegularFile()) { "$path is not a file" }
        val text = path.readText()
        return toIrImageVector(value = text, iconName = fileName)
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
