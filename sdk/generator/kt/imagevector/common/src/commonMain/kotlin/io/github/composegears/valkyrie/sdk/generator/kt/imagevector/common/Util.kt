package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.pathSegments

/**
 * Resolves the output package for the generated file.
 *
 * Returns [packageName] when there is no icon-pack nesting or
 * [ImageVectorConfig.useFlatPackage] is set. Otherwise appends each nested segment (lowercased)
 * to form a sub-package, e.g. `"com.example.icons.filled"` for a `ValkyrieIcons → Filled` tree.
 */
public fun ImageVectorGeneratorConfig.resolvePackageName(): String {
    val nested = iconPackTree?.pathSegments()?.drop(1).orEmpty()
    return when {
        nested.isEmpty() -> packageName
        imageVector.useFlatPackage -> packageName
        else -> "$packageName.${nested.joinToString(".") { it.lowercase() }}"
    }
}

/**
 * Resolves the `name` argument for `ImageVector.Builder`.
 *
 * Returns [iconName] when there is no nesting or the tree has no children (root-only).
 * Otherwise prefixes with the deepest sub-pack segment,
 * e.g. `"Rounded.Add"` for a `ValkyrieIcons → Material → Rounded` tree with icon `Add`.
 */
public fun ImageVectorGeneratorConfig.resolveIconBuilderName(): String {
    val segments = iconPackTree?.pathSegments().orEmpty()
    return when {
        segments.size <= 1 -> iconName
        else -> "${segments.last()}.$iconName"
    }
}
