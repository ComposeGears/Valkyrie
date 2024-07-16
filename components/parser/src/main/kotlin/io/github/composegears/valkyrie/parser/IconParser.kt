package io.github.composegears.valkyrie.parser

import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import androidx.compose.material.icons.generator.vector.Vector
import io.github.composegears.valkyrie.parser.IconType.SVG
import io.github.composegears.valkyrie.parser.IconType.XML
import java.io.File
import java.util.*
import kotlin.io.path.createTempFile
import kotlin.io.path.readText

data class IconParserOutput(
    val vector: Vector,
    val kotlinName: String
)

object IconParser {

    @Throws(IllegalStateException::class)
    fun toVector(file: File): IconParserOutput {
        val iconType = IconTypeParser.getIconType(file.extension) ?: error("File not SVG or XML")

        val fileName = getIconName(fileName = file.name)
        val icon = when (iconType) {
            SVG -> {
                val tmpFile = createTempFile(suffix = "valkyrie/")
                SvgToXmlParser.parse(file, tmpFile)

                Icon(fileContent = tmpFile.readText())
            }
            XML -> Icon(fileContent = file.readText())
        }

        return IconParserOutput(
            vector = IconParser(icon).parse(),
            kotlinName = fileName
        )
    }

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