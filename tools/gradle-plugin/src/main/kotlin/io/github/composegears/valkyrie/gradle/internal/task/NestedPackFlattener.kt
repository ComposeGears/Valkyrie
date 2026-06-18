package io.github.composegears.valkyrie.gradle.internal.task

import io.github.composegears.valkyrie.gradle.NestedPack

/**
 * Utility for flattening nested packs into a linear list.
 */
internal object NestedPackFlattener {
    /**
     * Recursively flattens nested packs into a flat list.
     * Each flattened pack contains its display name, source folder, and hierarchy path.
     *
     * Example configuration:
     * ```
     * iconPack {
     *     name = "ValkyrieIcons"
     *     nested {
     *         name = "Material"
     *         sourceFolder = "material"
     *
     *         nested {
     *             name = "Filled"
     *             sourceFolder = "filled"
     *         }
     *     }
     * }
     * ```
     *
     * Produces flattened result:
     * - FlattenedNestedPack(
     *      displayName = "Material",
     *      sourceFolder = "material",
     *      hierarchyPath = ["Material"]
     *   )
     * - FlattenedNestedPack(
     *      displayName = "Material.Filled",
     *      sourceFolder = "filled",
     *      hierarchyPath = ["Material", "Filled"]
     *   )
     *
     * Folder structure:
     * ```
     * valkyrieResources/
     *   └── material/
     *       ├── (files for "Material" pack)
     *       └── filled/
     *           └── (files for "Material.Filled" pack)
     * ```
     */
    fun flatten(
        packs: List<NestedPack>,
        parentPath: String = "",
        hierarchyPath: List<String> = emptyList(),
        parentSourceFolder: String = "",
    ): List<FlattenedNestedPack> {
        val result = mutableListOf<FlattenedNestedPack>()

        packs.forEach { pack ->
            val packName = pack.name.get()
            val displayName = if (parentPath.isEmpty()) packName else "$parentPath.$packName"
            val newHierarchyPath = hierarchyPath + packName

            // sourceFolder is now always required
            // Build full path combining parent sourceFolder with this sourceFolder
            val sourceFolder = pack.sourceFolder.get()
            val fullSourceFolder = if (parentSourceFolder.isEmpty()) {
                sourceFolder
            } else {
                "$parentSourceFolder/$sourceFolder"
            }

            result.add(
                FlattenedNestedPack(
                    displayName = displayName,
                    sourceFolder = fullSourceFolder,
                    autoMirror = pack.autoMirror.orNull,
                    hierarchyPath = newHierarchyPath,
                ),
            )

            // Recursively process nested packs
            val childPacks = pack.nestedPacks.get()
            if (childPacks.isNotEmpty()) {
                result.addAll(
                    flatten(
                        packs = childPacks,
                        parentPath = displayName,
                        hierarchyPath = newHierarchyPath,
                        parentSourceFolder = fullSourceFolder,
                    ),
                )
            }
        }

        return result
    }
}

/**
 * Represents a flattened nested pack with its full display path, source folder, and hierarchy.
 */
internal data class FlattenedNestedPack(
    val displayName: String,
    val sourceFolder: String,
    val autoMirror: Boolean? = null,
    val hierarchyPath: List<String> = emptyList(),
)
