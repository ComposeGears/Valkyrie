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
import org.gradle.api.tasks.Optional

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

    /**
     * Force all ImageVectors in this icon pack to have a specific autoMirror value.
     *
     * When set to `true`, all icons in this pack will have `autoMirror = true`.
     * When set to `false`, all icons in this pack will have `autoMirror = false`.
     * When not specified, the autoMirror value from the root extension or the original icon file will be used.
     *
     * This can be overridden at the nested pack level.
     *
     * Default: not specified
     */
    @get:Input
    @get:Optional
    val autoMirror: Property<Boolean> = objects.property<Boolean>()

    @get:Nested
    internal val nestedPacks: ListProperty<NestedPack> = objects
        .listProperty<NestedPack>()
        .convention(emptyList())

    @Suppress("unused")
    fun nested(action: NestedPack.() -> Unit) {
        val config = objects.newInstance<NestedPack>().apply(action)
        nestedPacks.add(config)
    }
}

abstract class NestedPack @Inject constructor(private val objects: ObjectFactory) {
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
     * This property is required.
     *
     * Example configuration:
     * ```
     * nested {
     *     name = "Outlined"
     *     sourceFolder = "outlined"
     * }
     * ```
     *
     * Or with recursive nesting:
     * ```
     * nested {
     *     name = "Material"
     *     sourceFolder = "material"
     *
     *     nested {
     *         name = "Filled"
     *         sourceFolder = "filled"
     *     }
     *     nested {
     *         name = "Outlined"
     *         sourceFolder = "outlined"
     *     }
     * }
     * ```
     *
     * Folder structure:
     * ```
     * valkyrieResources/
     *    └── material/
     *        ├── outlined/
     *        │     ├── icon1.svg
     *        │     └── icon2.svg
     *        └── filled/
     *              ├── icon1.svg
     *              └── icon2.svg
     *```
     */
    @get:Input
    val sourceFolder: Property<String> = objects.property<String>()

    /**
     * Force all ImageVectors in this nested pack to have a specific autoMirror value.
     *
     * When set to `true`, all icons in this nested pack will have `autoMirror = true`.
     * When set to `false`, all icons in this nested pack will have `autoMirror = false`.
     * When not specified, the autoMirror value from the icon pack or root extension will be used.
     *
     * Default: not specified
     */
    @get:Input
    @get:Optional
    val autoMirror: Property<Boolean> = objects.property<Boolean>()

    /**
     * Nested packs within this pack for recursive hierarchy support.
     * Allows defining arbitrarily deep icon pack structures.
     */
    @get:Nested
    internal val nestedPacks: ListProperty<NestedPack> = objects
        .listProperty<NestedPack>()
        .convention(emptyList())

    /**
     * Add a nested pack within this pack.
     *
     * Example:
     * ```
     * nested {
     *     name = "Material"
     *     sourceFolder = "material"
     *
     *     nested {
     *         name = "Filled"
     *         sourceFolder = "filled"
     *     }
     * }
     * ```
     */
    @Suppress("unused")
    fun nested(action: NestedPack.() -> Unit) {
        val config = objects.newInstance<NestedPack>().apply(action)
        nestedPacks.add(config)
    }
}
