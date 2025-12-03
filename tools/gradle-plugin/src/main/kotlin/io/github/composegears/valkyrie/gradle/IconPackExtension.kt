package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.dsl.listProperty
import io.github.composegears.valkyrie.gradle.dsl.newInstance
import io.github.composegears.valkyrie.gradle.dsl.property
import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.declarative.dsl.model.annotations.Configuring

abstract class IconPackExtension @Inject constructor(
    private val objects: ObjectFactory,
) {
    /**
     * Icon pack name
     *
     * Option required.
     */
    @Input
    val name: Property<String> = objects.property<String>()

    /**
     * The source set folder where the generated icon pack object will be stored.
     * (e.g., `commonMain`, `androidMain`, `iosMain`, etc.)
     */
    @Input
    val targetSourceSet: Property<String> = objects.property<String>()

    /**
     * Generate all icons into a single package without dividing by nested pack folders.
     * Only applies when nested packs are configured
     *
     * Default: `false`
     */
    @Input
    val useFlatPackage: Property<Boolean> = objects
        .property<Boolean>()
        .convention(false)

    @get:Nested
    internal val nestedPacks: ListProperty<NestedPack> = objects
        .listProperty<NestedPack>()
        .convention(emptyList())

    @Suppress("unused")
    @Configuring
    fun nested(action: NestedPack.() -> Unit) {
        val config = objects.newInstance<NestedPack>().apply(action)
        nestedPacks.add(config)
    }
}

abstract class NestedPack @Inject constructor(objects: ObjectFactory) {
    /**
     * Nested icon pack name
     *
     * Option required.
     */
    @get:Input
    val name: Property<String> = objects.property<String>()

    /**
     * The source folder path containing icons for this nested pack, relative to the `resourceDirectoryName`.
     *
     * The path is relative to the configured resources directory (typically `valkyrieResources`).
     *
     * Example configuration:
     * ```
     * nested {
     *     name = "Outlined"
     *     sourceFolder = "outlined" // Icons located at valkyrieResources/outlined/
     * }
     * ```
     *
     * If your project structure is:
     * ```
     * valkyrieResources/
     *   ├── outlined/
     *   │   ├── icon1.svg
     *   │   └── icon2.svg
     *   └── filled/
     *       ├── icon1.svg
     *       └── icon2.svg
     * ```
     *
     * Then for the "Outlined" nested pack, set `sourceFolder.set("outlined")`.
     *
     * This property is required and must be set for each nested pack.
     */
    @get:Input
    val sourceFolder: Property<String> = objects.property<String>()
}
