package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface ValkyrieExtension {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     */
    val packageName: Property<String>

    /**
     * If `true`, accessor generation will be re-run when clicking Sync in the IntelliJ IDE UI. Disabled by default.
     */
    val generateAtSync: Property<Boolean>

    /**
     * Output location of the generated files. Defaults to `<project-dir>/build/generated/sources/valkyrie`.
     */
    val outputDirectory: DirectoryProperty

    /**
     * Name of the resource directory containing icon files. Defaults to `valkyrieResources`.
     * The plugin will look for icons in `<sourceSet>/valkyrieResources` for all project types.
     */
    val resourceDirectoryName: Property<String>

    /**
     * Unset by default
     */
    val iconPackName: Property<String>

    /**
     * Unset by default
     */
    val nestedPackName: Property<String>

    /**
     * Defaults to [OutputFormat.BackingProperty]
     */
    val outputFormat: Property<OutputFormat>

    /**
     * Defaults to `true`
     */
    val useComposeColors: Property<Boolean>

    /**
     * Defaults to `false`
     */
    val generatePreview: Property<Boolean>

    /**
     * Defaults to [PreviewAnnotationType.AndroidX]
     */
    val previewAnnotationType: Property<PreviewAnnotationType>

    /**
     * Defaults to `false`
     */
    val useFlatPackage: Property<Boolean>

    /**
     * Defaults to `false`
     */
    val useExplicitMode: Property<Boolean>

    /**
     * Defaults to `false`
     */
    val addTrailingComma: Property<Boolean>

    /**
     * Defaults to `4`
     */
    val indentSize: Property<Int>
}
