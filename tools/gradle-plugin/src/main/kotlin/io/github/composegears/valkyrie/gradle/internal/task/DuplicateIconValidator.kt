package io.github.composegears.valkyrie.gradle.internal.task

import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import java.io.File
import org.gradle.api.GradleException

/**
 * Utility for validating icon duplicates across packs.
 */
internal object DuplicateIconValidator {
    /**
     * Validates for exact and case-insensitive duplicate icons.
     *
     * @param files List of icon files to check
     * @param iconNames Pre-computed formatted icon names
     * @param packName Name of icon pack (for error messages)
     * @param nestedPacks List of nested packs configuration
     * @param useFlatPackage Whether flat package mode is enabled
     *
     * @throws GradleException if duplicates are found
     */
    fun validateDuplicates(
        files: List<File>,
        iconNames: List<String>,
        packName: String,
        nestedPacks: List<FlattenedNestedPack>,
        useFlatPackage: Boolean,
    ) {
        if (nestedPacks.isNotEmpty()) {
            // Find the common resources directory based on max nesting depth
            val maxNestedPackDepth = nestedPacks.maxOfOrNull { it.hierarchyPath.size } ?: 1
            val resourcesDir = files.findResourcesDirectory(maxNestedPackDepth)

            // Map files to their nested pack
            val fileToNestedPack = files.associateWith { file ->
                val relativeFileDir = if (resourcesDir != null && resourcesDir.isDirectory) {
                    file.parentFile.toRelativeString(resourcesDir)
                } else {
                    file.parentFile.name
                }
                // Find matching nested pack - exact path match required
                nestedPacks.find { nestedPack ->
                    val normalizedSourceFolder = nestedPack.sourceFolder.replace('/', File.separatorChar)
                    relativeFileDir == normalizedSourceFolder
                }
            }

            // Only validate files that have matching nested packs
            val filesToValidate = fileToNestedPack.filterValues { it != null }.keys

            // Group by nested pack
            val iconsByPack = filesToValidate.groupBy { file ->
                if (useFlatPackage) {
                    packName // All in same pack when flat
                } else {
                    val nestedPackName = fileToNestedPack[file]?.displayName
                    if (nestedPackName != null) "$packName.$nestedPackName" else packName
                }
            }

            iconsByPack.forEach { (packIdentifier, filesInPack) ->
                val names = filesInPack.map { IconNameFormatter.format(name = it.name) }
                checkDuplicatesInIconNames(names, "\"$packIdentifier\"")
            }
        } else {
            // Single pack - check all files together
            checkDuplicatesInIconNames(iconNames, "\"$packName\"")
        }
    }

    /**
     * Validates icon names for duplicates (exact and case-insensitive).
     *
     * @param names List of formatted icon names
     * @param context Description of what's being checked (for error messages)
     *
     * @throws GradleException if duplicates are found
     */
    fun checkDuplicatesInIconNames(names: List<String>, context: String) {
        // Check exact duplicates
        val exactDuplicates = names
            .groupBy { it }
            .filter { it.value.size > 1 }
            .keys
            .toList()
            .sorted()

        if (exactDuplicates.isNotEmpty()) {
            throw GradleException(
                "Found duplicate icon names in $context: ${exactDuplicates.joinToString(", ")}. " +
                    "Each icon must have a unique name. " +
                    "Please rename the source files to avoid duplicates.",
            )
        }

        // Check case-insensitive duplicates
        val caseInsensitiveDuplicates = names
            .groupBy { it.lowercase() }
            .filter { it.value.size > 1 && it.value.distinct().size > 1 }
            .values
            .flatten()
            .distinct()
            .sorted()

        if (caseInsensitiveDuplicates.isNotEmpty()) {
            throw GradleException(
                "Found icon names that would collide on case-insensitive file systems (macOS/Windows) in $context: " +
                    "${caseInsensitiveDuplicates.joinToString(", ")}. " +
                    "These icons would overwrite each other during generation. " +
                    "Please rename the source files to avoid case-insensitive duplicates.",
            )
        }
    }
}
