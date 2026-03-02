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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Converts an SVG string into Kotlin ImageVector source code and returns a JSON-encoded [ConverterResult].
 *
 * This function is exported for the Figma plugin Wasm bridge.
 */
@JsExport
fun convertSvg(
    svg: String,
    iconName: String,
    packageName: String,
    outputFormat: String = OutputFormat.BackingProperty.key,
    useComposeColors: Boolean = true,
    addTrailingComma: Boolean = false,
    useExplicitMode: Boolean = false,
    usePathDataString: Boolean = false,
    indentSize: Int = 4,
    autoMirror: Boolean? = null,
): String {
    return runCatching {
        val normalizedIconName = IconNameFormatter.format(iconName)

        val parseOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Kmp,
            value = svg,
            iconName = normalizedIconName,
        ).let { output ->
            autoMirror?.let {
                output.copy(irImageVector = output.irImageVector.copy(autoMirror = it))
            } ?: output
        }

        val output = ImageVectorGenerator.convert(
            vector = parseOutput.irImageVector,
            iconName = parseOutput.iconName,
            config = ImageVectorGeneratorConfig(
                packageName = packageName,
                iconPackPackage = packageName,
                packName = "",
                nestedPackName = "",
                outputFormat = when (outputFormat) {
                    OutputFormat.LazyProperty.key -> OutputFormat.LazyProperty
                    else -> OutputFormat.BackingProperty
                },
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
            success = true,
            iconName = output.name,
            fileName = "${output.name}.kt",
            code = output.content,
        )
    }.getOrElse { error ->
        ConverterResult(
            success = false,
            iconName = iconName,
            fileName = "",
            code = "",
            error = error.message ?: "Unknown conversion error",
        )
    }.let { json.encodeToString(it) }
}

/**
 * Normalizes a raw icon name into a Kotlin-friendly identifier.
 */
@JsExport
fun normalizeIconName(iconName: String): String = IconNameFormatter.format(iconName)

@Serializable
private data class ConverterResult(
    val success: Boolean,
    val iconName: String,
    val fileName: String,
    val code: String,
    val error: String? = null,
)

private val json = Json {
    encodeDefaults = true
}
