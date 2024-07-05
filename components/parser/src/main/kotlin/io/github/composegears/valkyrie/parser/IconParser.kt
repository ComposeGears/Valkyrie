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

        val fileName = getFileName(file, iconType)
        val icon = when (iconType) {
            SVG -> {
                val tmpFile = createTempFile(suffix = "valkyrie/")
                SvgToXmlParser.parse(file, tmpFile)

                Icon(
                    kotlinName = fileName,
                    xmlFileName = "",
                    fileContent = tmpFile.readText()
                )

            }
            XML -> Icon(
                kotlinName = fileName,
                xmlFileName = file.name,
                fileContent = file.readText()
            )
        }

        return IconParserOutput(
            vector = IconParser(icon).parse(),
            kotlinName = icon.kotlinName
        )
    }

    private fun getFileName(file: File, iconType: IconType): String {

        var name = file.name
            .removeSuffix(".${iconType.extension}")
            .split("_")
            .joinToString("") { it.capitalized() }
            .replace("\\d".toRegex(), "")

        if (name.startsWith("ic", ignoreCase = true)) {
            name = name.drop(2).capitalized()
        }
        return name
    }
}

private fun String.capitalized(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}