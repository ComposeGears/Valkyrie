package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import org.gradle.api.tasks.Input

/**
 * Uses the same defaults as `SvgXmlToImageVectorCommand` in tools/cli.
 */
data class ValkyrieConfig @JvmOverloads constructor(
    @get:Input var iconPackName: String = "",
    @get:Input var nestedPackName: String = "",
    @get:Input var outputFormat: OutputFormat = OutputFormat.BackingProperty,
    @get:Input var useComposeColors: Boolean = true,
    @get:Input var generatePreview: Boolean = false,
    @get:Input var previewAnnotationType: PreviewAnnotationType = PreviewAnnotationType.AndroidX,
    @get:Input var useFlatPackage: Boolean = false,
    @get:Input var useExplicitMode: Boolean = false,
    @get:Input var addTrailingComma: Boolean = false,
    @get:Input var indentSize: Int = 4,
)
