package io.github.composegears.valkyrie.parser

import ai.grazie.utils.capitalize
import ai.grazie.utils.dropPostfix
import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import com.android.ide.common.vectordrawable.Svg2Vector
import com.squareup.kotlinpoet.ClassName
import io.github.composegears.valkyrie.generator.ImageVectorGenerator
import io.github.composegears.valkyrie.parser.IconType.SVG
import io.github.composegears.valkyrie.parser.IconType.XML
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.readText

data class Config(
    val packName: String,
    val packPackage: String
)

object IconParser {

    fun tryParse(
        file: File,
        config: Config
    ): String {
        val iconType = IconTypeParser.getIconType(file.extension) ?: return "File not SVG or XML"

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

        val vector = IconParser(icon).parse()

        val assetGenerationResult = ImageVectorGenerator(
            iconName = icon.kotlinName,
            iconGroupPackage = config.packPackage,
            vector = vector,
            generatePreview = false
        ).createFileSpec(
            ClassName(
                config.packPackage,
                config.packName
            )
        )

        return assetGenerationResult.sourceCode
    }

    private fun getFileName(file: File, iconType: IconType): String {
        var name = file.name
            .dropPostfix(".${iconType.extension}")
            .split("_")
            .joinToString("") { it.capitalize() }
            .replace("\\d".toRegex(), "")

        if (name.startsWith("ic", ignoreCase = true)) {
            name = name.drop(2).capitalize()
        }
        return name
    }
}

object SvgToXmlParser {

    fun parse(file: File, outPath: Path) {
        Svg2Vector.parseSvgToXml(file.toPath(), outPath.outputStream())
    }
}