@file:OptIn(ExperimentalJsExport::class)

package io.github.composegears.valkyrie.converter.figma

import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
fun convertSvg(
    svg: String,
    iconName: String,
    packageName: String,
    useComposeColors: Boolean = true,
    addTrailingComma: Boolean = false,
    useExplicitMode: Boolean = false,
    usePathDataString: Boolean = false,
    indentSize: Int = 4,
    autoMirror: String = "",
): String {
    return runCatching {
        val normalizedIconName = IconNameFormatter.format(iconName)

        val parseOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Kmp,
            value = svg,
            iconName = normalizedIconName,
        ).let {
            when (autoMirror.lowercase()) {
                "true" -> it.copy(irImageVector = it.irImageVector.copy(autoMirror = true))
                "false" -> it.copy(irImageVector = it.irImageVector.copy(autoMirror = false))
                else -> it
            }
        }

        val output = ImageVectorGenerator.convert(
            vector = parseOutput.irImageVector,
            iconName = parseOutput.iconName,
            config = ImageVectorGeneratorConfig(
                packageName = packageName,
                iconPackPackage = packageName,
                packName = "",
                nestedPackName = "",
                outputFormat = OutputFormat.BackingProperty,
                useComposeColors = useComposeColors,
                generatePreview = false,
                useFlatPackage = false,
                addTrailingComma = addTrailingComma,
                useExplicitMode = useExplicitMode,
                usePathDataString = usePathDataString,
                indentSize = indentSize,
            ),
        )

        ConverterResult(
            ok = true,
            iconName = output.name,
            fileName = "${output.name}.kt",
            content = output.content,
        )
    }.getOrElse { error ->
        ConverterResult(
            ok = false,
            iconName = iconName,
            fileName = "",
            content = "",
            error = error.message ?: "Unknown conversion error",
        )
    }.toJson()
}

@JsExport
fun normalizeIconName(iconName: String): String = IconNameFormatter.format(iconName)

private data class ConverterResult(
    val ok: Boolean,
    val iconName: String,
    val fileName: String,
    val content: String,
    val error: String? = null,
) {
    fun toJson(): String {
        return buildString {
            append('{')
            append("\"ok\":$ok")
            append(',')
            append("\"iconName\":\"")
            append(iconName.escapeJson())
            append("\"")
            append(',')
            append("\"fileName\":\"")
            append(fileName.escapeJson())
            append("\"")
            append(',')
            append("\"content\":\"")
            append(content.escapeJson())
            append("\"")
            if (error != null) {
                append(',')
                append("\"error\":\"")
                append(error.escapeJson())
                append("\"")
            }
            append('}')
        }
    }
}

private fun String.escapeJson(): String {
    return buildString(length) {
        for (ch in this@escapeJson) {
            when (ch) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(ch)
            }
        }
    }
}
