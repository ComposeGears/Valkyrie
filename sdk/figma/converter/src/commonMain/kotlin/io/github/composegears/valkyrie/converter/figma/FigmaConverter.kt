@file:OptIn(ExperimentalJsExport::class)

package io.github.composegears.valkyrie.converter.figma

import io.github.composegears.valkyrie.converter.figma.ConverterResult.Error
import io.github.composegears.valkyrie.converter.figma.ConverterResult.Success
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
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
    suppressUnusedReceiverWarning: Boolean = false,
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
                outputFormat = resolveOutputFormat(outputFormat),
                useComposeColors = useComposeColors,
                generatePreview = false,
                useFlatPackage = false,
                addTrailingComma = addTrailingComma,
                useExplicitMode = useExplicitMode,
                usePathDataString = usePathDataString,
                indentSize = indentSize,
                suppressUnusedReceiverWarning = suppressUnusedReceiverWarning,
            ),
        )

        Success(
            iconName = output.name,
            fileName = "${output.name}.kt",
            code = output.content,
        )
    }.getOrElse { error ->
        Error(
            iconName = iconName,
            error = error.message ?: "Unknown conversion error",
        )
    }.let { Json.encodeToString(it) }
}

private fun resolveOutputFormat(outputFormat: String): OutputFormat = when (outputFormat) {
    OutputFormat.BackingProperty.key -> OutputFormat.BackingProperty
    OutputFormat.LazyProperty.key -> OutputFormat.LazyProperty
    else -> throw IllegalArgumentException(
        "Unsupported outputFormat '$outputFormat'. Supported values: " +
            "'${OutputFormat.BackingProperty.key}', '${OutputFormat.LazyProperty.key}'.",
    )
}

/**
 * Normalizes a raw icon name into a Kotlin-friendly identifier.
 */
@JsExport
fun normalizeIconName(iconName: String): String = IconNameFormatter.format(iconName)

@Serializable
sealed interface ConverterResult {
    val iconName: String

    @Serializable
    @SerialName("success")
    data class Success(
        override val iconName: String,
        val fileName: String,
        val code: String,
    ) : ConverterResult

    @Serializable
    @SerialName("error")
    data class Error(
        override val iconName: String,
        val error: String,
    ) : ConverterResult
}
