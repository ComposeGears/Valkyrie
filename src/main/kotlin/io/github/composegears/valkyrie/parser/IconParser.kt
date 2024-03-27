package io.github.composegears.valkyrie.parser

import ai.grazie.utils.capitalize
import ai.grazie.utils.dropPostfix
import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import androidx.compose.material.icons.generator.VectorAssetGenerator
import com.android.ide.common.vectordrawable.Svg2Vector
import com.squareup.kotlinpoet.ClassName
import io.github.composegears.valkyrie.parser.IconType.SVG
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.readText

data class Config(
    val packName: String = "ValkyrieIcons",
    val packPackage: String = "io.github.composegears.valkyrie.icons"
)

object IconParser {

    fun tryParse(
        file: File,
        config: Config = Config()
    ): String {
        val iconType = IconTypeParser.getIconType(file.extension) ?: return "File not SVG or XML"

        val fileName = getFileName(file, iconType)

        val icon = if (iconType == SVG) {
            val tmpFile = createTempFile(suffix = "valkyrie/")
            SvgToXmlParser.parse(file, tmpFile)

            Icon(
                kotlinName = fileName,
                xmlFileName = "",
                fileContent = tmpFile.readText()
            )

        } else return "XML not supported yet"

        val vector = IconParser(icon).parse()


        val assetGenerationResult = VectorAssetGenerator(
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

        return assetGenerationResult.sourceGeneration.toString()
    }

    private fun getFileName(file: File, iconType: IconType): String = file.name
        .replace("_", "")
        .dropPostfix(".${iconType.extension}")
        .replace("\\d".toRegex(), "")
        .capitalize()
}

object SvgToXmlParser {

    fun parse(file: File, outPath: Path) {
        Svg2Vector.parseSvgToXml(file, outPath.outputStream())
    }
}