package io.github.composegears.valkyrie.parser

import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import androidx.compose.material.icons.generator.vector.Vector
import io.github.composegears.valkyrie.parser.IconType.SVG
import io.github.composegears.valkyrie.parser.IconType.XML
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.readText

data class IconParserOutput(
  val vector: Vector,
  val kotlinName: String,
)

object IconParser {

  @Throws(IllegalStateException::class)
  fun toVector(path: Path): IconParserOutput {
    val iconType = IconType.from(path.extension) ?: error("File not SVG or XML")

    val fileName = getIconName(fileName = path.name)
    val icon = when (iconType) {
      SVG -> {
        val tmpPath = createTempFile(suffix = "valkyrie/")
        SvgToXmlParser.parse(path, tmpPath)

        Icon(fileContent = tmpPath.readText())
      }
      XML -> Icon(fileContent = path.readText())
    }

    return IconParserOutput(
      vector = IconParser(icon).parse(),
      kotlinName = fileName,
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
