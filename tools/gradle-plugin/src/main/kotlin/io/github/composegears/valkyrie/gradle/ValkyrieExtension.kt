package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.dsl.newInstance
import io.github.composegears.valkyrie.gradle.dsl.property
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_RESOURCE_DIRECTORY
import javax.inject.Inject
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.declarative.dsl.model.annotations.Configuring

abstract class ValkyrieExtension @Inject constructor(objects: ObjectFactory) {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     */
    val packageName: Property<String> = objects.property<String>()

    /**
     * Automatically trigger accessor generation when clicking Sync in the IntelliJ IDEA.
     *
     * Default: `false`
     */
    val generateAtSync: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Output location of the generated files.
     *
     * Default: `<project-dir>/build/generated/sources/valkyrie`.
     */
    val outputDirectory: DirectoryProperty = objects.directoryProperty()

    /**
     * The name of the resource directory that contains icon files.
     * The plugin will look for icons in `<sourceSet>/valkyrieResources` for all project types.
     *
     * Default: `valkyrieResources`
     */
    val resourceDirectoryName: Property<String> = objects
        .property<String>()
        .convention(DEFAULT_RESOURCE_DIRECTORY)

    /**
     * Icon pack name for generated icons
     *
     * Default: `unspecified`
     */
    @Optional
    val iconPackName: Property<String> = objects.property<String>()

    /**
     * Nested package name for generated icons inside the icon pack
     *
     * Default: `unspecified`
     */
    @Optional
    val nestedPackName: Property<String> = objects.property<String>()

    /**
     * Generate all icons into a single package without dividing by nested pack folders
     *
     * Default: `false`
     */
    val useFlatPackage: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    /**
     * Code style configuration for generated code.
     */
    @get:Nested
    internal val codeStyle: CodeStyleConfigExtension = objects.newInstance<CodeStyleConfigExtension>()

    /**
     * Configures code style options for generated code
     */
    @Suppress("unused")
    @Configuring
    fun codeStyle(action: CodeStyleConfigExtension.() -> Unit) = action.invoke(codeStyle)

    /**
     * ImageVector generation configuration.
     */
    @get:Nested
    internal val imageVector: ImageVectorConfigExtension = objects.newInstance<ImageVectorConfigExtension>()

    /**
     * Configures ImageVector generation options
     */
    @Suppress("unused")
    @Configuring
    fun imageVector(action: ImageVectorConfigExtension.() -> Unit) = action.invoke(imageVector)
}
