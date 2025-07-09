package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

abstract class ValkyrieExtension {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     */
    abstract val packageName: Property<String>

    /**
     * If `true`, accessor generation will be re-run when clicking Sync in the IntelliJ IDE UI. Disabled by default.
     */
    abstract val generateAtSync: Property<Boolean>

    /**
     * Output location of the generated files. Defaults to `<project-dir>/build/generated/sources/valkyrie`.
     */
    abstract val outputDirectory: DirectoryProperty

    /**
     * Defaults to none
     */
    abstract val iconPackName: Property<String>

    /**
     * Defaults to none
     */
    abstract val nestedPackName: Property<String>

    /**
     * Defaults to [OutputFormat.BackingProperty]
     */
    abstract val outputFormat: Property<OutputFormat>

    /**
     * Defaults to `true`
     */
    abstract val useComposeColors: Property<Boolean>

    /**
     * Defaults to `false`
     */
    abstract val generatePreview: Property<Boolean>

    /**
     * Defaults to [PreviewAnnotationType.AndroidX]
     */
    abstract val previewAnnotationType: Property<PreviewAnnotationType>

    /**
     * Defaults to `false`
     */
    abstract val useFlatPackage: Property<Boolean>

    /**
     * Defaults to `false`
     */
    abstract val useExplicitMode: Property<Boolean>

    /**
     * Defaults to `false`
     */
    abstract val addTrailingComma: Property<Boolean>

    /**
     * Defaults to `4`
     */
    abstract val indentSize: Property<Int>
}
