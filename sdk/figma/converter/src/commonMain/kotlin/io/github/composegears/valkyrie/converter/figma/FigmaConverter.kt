@file:OptIn(ExperimentalJsExport::class)

package io.github.composegears.valkyrie.converter.figma

import io.github.composegears.valkyrie.converter.figma.ConverterResult.Error
import io.github.composegears.valkyrie.converter.figma.ConverterResult.Success
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.CodeStyleConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.ImageVectorGenerator
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parseOutput.iconName,
                packageName = packageName,
                codeStyle = CodeStyleConfig(
                    useExplicitMode = useExplicitMode,
                    indentSize = indentSize,
                ),
                imageVector = ImageVectorConfig(
                    outputFormat = OutputFormat.from(outputFormat),
                    useComposeColors = useComposeColors,
                    addTrailingComma = addTrailingComma,
                    usePathDataString = usePathDataString,
                    suppressUnusedReceiverWarning = suppressUnusedReceiverWarning,
                ),
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
