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

abstract class ValkyrieExtension @Inject constructor(private val objects: ObjectFactory) {
    /**
     * Package name of the generated accessors. If you have any Android gradle plugin applied, this will default to
     * the [com.android.build.api.dsl.CommonExtension.namespace] property - or fail otherwise.
     *
     * Example: `"com.example.app.icons"`
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
     * The plugin automatically processes files with `.svg` and `.xml` extensions.
     *
     * Example directory structure:
     * ```
     * src/
     *   commonMain/
     *     valkyrieResources/
     *       icon1.svg
     *       icon2.xml
     * ```
     *
     * Default: `valkyrieResources`
     */
    val resourceDirectoryName: Property<String> = objects
        .property<String>()
        .convention(DEFAULT_RESOURCE_DIRECTORY)

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
     * Icon Pack generation configuration (optional).
     */
    @get:Nested
    @get:Optional
    internal val iconPack: Property<IconPackExtension> = objects.property<IconPackExtension>()

    /**
     * Configures ImageVector generation options
     */
    @Suppress("unused")
    @Configuring
    fun imageVector(action: ImageVectorConfigExtension.() -> Unit) = action.invoke(imageVector)

    /**
     * Configures Icon Pack options
     */
    @Suppress("unused")
    @Configuring
    fun iconPack(action: IconPackExtension.() -> Unit) {
        val spec = iconPack.getOrElse(objects.newInstance<IconPackExtension>()).apply(action)
        iconPack.set(spec)
    }
}
