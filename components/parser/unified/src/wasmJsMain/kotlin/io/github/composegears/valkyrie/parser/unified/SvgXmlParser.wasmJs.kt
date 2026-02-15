package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.parser.kmp.svg.SVGParser
import io.github.composegears.valkyrie.parser.kmp.xml.XmlToImageVectorParser
import io.github.composegears.valkyrie.parser.unified.ext.readText
import io.github.composegears.valkyrie.parser.unified.model.IconParserOutput
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.parser.unified.model.IconType.SVG
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import kotlinx.io.files.Path

actual object SvgXmlParser {

    actual val availableParsers: List<ParserType> = listOf(ParserType.Kmp)

    actual fun toIrImageVector(
        parser: ParserType,
        path: Path,
    ): IconParserOutput {
        val iconType = IconType.from(path) ?: error("$path must be an SVG or XML file.")
        val fileName = IconNameFormatter.format(name = path.name)

        val irImageVector = when (parser) {
            ParserType.Jvm -> error("Jvm parser not supported on WASM")
            ParserType.Kmp -> {
                val content = path.readText()

                when (iconType) {
                    SVG -> SVGParser.parse(content)
                    XML -> XmlToImageVectorParser.parse(content)
                }
            }
        }

        return IconParserOutput(
            iconType = iconType,
            irImageVector = irImageVector,
            iconName = fileName,
        )
    }

    actual fun toIrImageVector(
        parser: ParserType,
        value: String,
        iconName: String,
    ): IconParserOutput {
        val iconType = IconType.from(value) ?: error("Unsupported icon type")

        val irImageVector = when (parser) {
            ParserType.Jvm -> error("Jvm parser not supported on WASM")
            ParserType.Kmp -> {
                when (iconType) {
                    SVG -> SVGParser.parse(value)
                    XML -> XmlToImageVectorParser.parse(value)
                }
            }
        }

        return IconParserOutput(
            irImageVector = irImageVector,
            iconName = iconName,
            iconType = iconType,
        )
    }

    actual fun svgToXml(parser: ParserType, path: Path): String {
        error("not supported on WASM")
    }

    actual fun svgToXml(parser: ParserType, text: String): String {
        error("not supported on WASM")
    }
}
