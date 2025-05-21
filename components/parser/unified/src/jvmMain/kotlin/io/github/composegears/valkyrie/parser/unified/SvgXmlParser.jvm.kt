package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.parser.jvm.svg.SvgToXmlParser
import io.github.composegears.valkyrie.parser.jvm.xml.XmlToImageVectorParser as XmlToImageVectorParserJvm
import io.github.composegears.valkyrie.parser.kmp.svg.SVGParser
import io.github.composegears.valkyrie.parser.kmp.xml.XmlToImageVectorParser as XmlToImageVectorParserKmp
import io.github.composegears.valkyrie.parser.unified.ext.readText
import io.github.composegears.valkyrie.parser.unified.ext.toJvmPath
import io.github.composegears.valkyrie.parser.unified.model.IconParserOutput
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.parser.unified.model.IconType.SVG
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import kotlinx.io.files.Path

actual object SvgXmlParser {

    actual val availableParsers: List<ParserType> = listOf(ParserType.Jvm, ParserType.Kmp)

    @Throws(IllegalStateException::class)
    actual fun toIrImageVector(
        parser: ParserType,
        path: Path,
    ): IconParserOutput {
        val iconType = IconType.from(path) ?: error("$path must be an SVG or XML file.")
        val fileName = IconNameFormatter.format(name = path.name)

        val irImageVector = when (parser) {
            ParserType.Jvm -> {
                val text = when (iconType) {
                    SVG -> SvgToXmlParser.parse(path.toJvmPath())
                    XML -> path.readText()
                }
                XmlToImageVectorParserJvm.parse(text)
            }
            ParserType.Kmp -> {
                val content = path.readText()
                when (iconType) {
                    SVG -> SVGParser.parse(content)
                    XML -> XmlToImageVectorParserKmp.parse(content)
                }
            }
        }

        return IconParserOutput(
            iconType = iconType,
            irImageVector = irImageVector,
            iconName = fileName,
        )
    }

    @Throws(IllegalStateException::class)
    actual fun toIrImageVector(
        parser: ParserType,
        value: String,
        iconName: String,
    ): IconParserOutput {
        val iconType = IconType.from(value) ?: error("Unsupported icon type")

        val irImageVector = when (parser) {
            ParserType.Jvm -> {
                val text = when (iconType) {
                    SVG -> SvgToXmlParser.parse(value)
                    XML -> value
                }
                XmlToImageVectorParserJvm.parse(text)
            }
            ParserType.Kmp -> {
                when (iconType) {
                    SVG -> SVGParser.parse(value)
                    XML -> XmlToImageVectorParserKmp.parse(value)
                }
            }
        }

        return IconParserOutput(
            irImageVector = irImageVector,
            iconName = iconName,
            iconType = iconType,
        )
    }
}
