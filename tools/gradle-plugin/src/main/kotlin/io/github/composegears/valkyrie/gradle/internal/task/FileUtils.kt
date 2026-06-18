package io.github.composegears.valkyrie.gradle.internal.task

import java.io.File

/**
 * Find the common ancestor directory of multiple files.
 * Returns null if files list is empty.
 *
 * Example:
 * listOf(File("/a/b/c"), File("/a/b/d")).commonAncestor() -> File("/a/b")
 */
internal fun List<File>.commonAncestor(): File? {
    if (isEmpty()) {
        return null
    }
    if (size == 1) {
        return first()
    }

    val paths = map { it.absolutePath.split(File.separator) }
    val commonParts = mutableListOf<String>()

    val minLength = paths.minOf { it.size }
    for (i in 0 until minLength) {
        val part = paths[0][i]
        if (paths.all { it[i] == part }) {
            commonParts.add(part)
        } else {
            break
        }
    }

    return if (commonParts.isNotEmpty()) {
        File(commonParts.joinToString(File.separator))
    } else {
        null
    }
}

/**
 * Find the resources directory based on nested pack depth.
 * Goes up the directory hierarchy from file parents by maxNestingDepth levels.
 * Includes fallbacks for edge cases.
 *
 * Example:
 * - 1-level nested (files in "outlined/"): maxDepth=1 → goes up 1 level to "valkyrieResources/"
 * - 2-level nested (files in "material/filled/"): maxDepth=2 → goes up 2 levels to "valkyrieResources/"
 */
internal fun List<File>.findResourcesDirectory(maxNestingDepth: Int): File? {
    if (isEmpty()) return null

    // Start from first file's parent and go up maxNestingDepth levels
    var resourcesDir = firstOrNull()?.parentFile
    repeat(maxNestingDepth) {
        resourcesDir = resourcesDir?.parentFile
    }

    // Fallback to common ancestor logic
    if (resourcesDir == null || !resourcesDir.isDirectory) {
        resourcesDir = mapNotNull { it.parentFile }.commonAncestor()
        repeat((maxNestingDepth - 1).coerceAtLeast(0)) {
            resourcesDir = resourcesDir?.parentFile
        }
    }

    // Final fallback to original logic
    if (resourcesDir == null || !resourcesDir.isDirectory) {
        resourcesDir = mapNotNull { it.parentFile?.parentFile }.commonAncestor()
            ?: firstOrNull()?.parentFile?.parentFile
    }

    return resourcesDir
}

/**
 * Find the resources directory based on nested pack depth (Set variant).
 * Converts to List and delegates to the List extension.
 */
internal fun Set<File>.findResourcesDirectory(maxNestingDepth: Int): File? = toList().findResourcesDirectory(maxNestingDepth)
