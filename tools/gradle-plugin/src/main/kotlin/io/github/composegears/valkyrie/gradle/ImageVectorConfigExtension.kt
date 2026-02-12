package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.gradle.dsl.property
import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

abstract class ImageVectorConfigExtension @Inject constructor(objects: ObjectFactory) {
    /**
     * Specifies the output format for the generated ImageVector.
     *
     * Available options:
     * - [OutputFormat.BackingProperty]: Uses backing property for the output.
     * - [OutputFormat.LazyProperty]: Generates a lazy property representation.
     *
     * Default: [OutputFormat.BackingProperty]
     */
    val outputFormat: Property<OutputFormat> = objects
        .property<OutputFormat>()
        .convention(OutputFormat.BackingProperty)

    /**
     * Use predefined Compose colors instead of hex color codes (e.g. Color.Black instead of Color(0xFF000000))
     *
     * Default: `true`
     */
    val useComposeColors: Property<Boolean> = objects
        .property<Boolean>()
        .convention(true)

    /**
     * Generate `@Preview` function for ImageVector.
     *
     * Default: `false`
     */
    val generatePreview: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Insert a trailing comma after the last element of ImageVector.Builder block and path params.
     *
     * Default: `false`
     */
    val addTrailingComma: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Generate addPath with pathData strings instead of path builder calls.
     *
     * Note: Using pathData strings is less optimal due to pre-rendering parsing.
     *
     * Default: `false`
     */
    val usePathDataString: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)
}
