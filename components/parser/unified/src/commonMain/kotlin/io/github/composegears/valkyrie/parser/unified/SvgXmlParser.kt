package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.parser.unified.model.IconParserOutput
import kotlinx.io.files.Path

expect object SvgXmlParser {

    val availableParsers: List<ParserType>

    @Throws(IllegalStateException::class)
    fun toIrImageVector(
        parser: ParserType,
        path: Path,
    ): IconParserOutput

    @Throws(IllegalStateException::class)
    fun toIrImageVector(
        parser: ParserType,
        value: String,
        iconName: String,
    ): IconParserOutput

    @Throws(IllegalStateException::class)
    fun svgToXml(parser: ParserType, path: Path): String

    @Throws(IllegalStateException::class)
    fun svgToXml(parser: ParserType, text: String): String
}
