package io.github.composegears.valkyrie.gradle.internal.common

import io.github.composegears.valkyrie.gradle.IconPackExtension
import io.github.composegears.valkyrie.gradle.ValkyrieExtension
import org.gradle.api.GradleException

/**
 * Utility for validating Valkyrie plugin configuration.
 */
internal object ExtensionValidator {
    /**
     * Validates the complete Valkyrie extension configuration.
     * Throws [org.gradle.api.GradleException] if configuration is invalid.
     */
    fun validate(extension: ValkyrieExtension) {
        if (extension.packageName.orNull.isNullOrBlank()) {
            throw GradleException("\"packageName\" cannot be blank")
        }

        val indentSize = extension.codeStyle.indentSize.get()
        if (indentSize < 0) {
            throw GradleException("indentSize must be non-negative, but was: \"$indentSize\"")
        }

        if (extension.iconPack.isPresent) {
            validateIconPack(extension.iconPack.get())
        }
    }

    private fun validateIconPack(iconPack: IconPackExtension) {
        if (!iconPack.name.isPresent) {
            throw GradleException("iconPack \"name\" is required but not set")
        }

        val packName = iconPack.name.get()
        if (packName.isBlank()) {
            throw GradleException("iconPack \"name\" cannot be blank")
        }

        if (!iconPack.targetSourceSet.isPresent) {
            throw GradleException("iconPack \"targetSourceSet\" is required but not set")
        }

        val nestedPacks = iconPack.nestedPacks.get()
        val nestedNames = mutableSetOf<String>()
        val sourceFolders = mutableSetOf<String>()

        nestedPacks.forEach { nested ->
            val name = nested.name.orNull
            val sourceFolder = nested.sourceFolder.orNull

            when {
                name == null -> throw GradleException("nested pack \"name\" is required but not set")
                name.isBlank() -> throw GradleException("nested pack \"name\" cannot be blank")
            }

            when {
                sourceFolder == null -> throw GradleException("nested pack \"sourceFolder\" is required but not set")
                sourceFolder.isBlank() -> throw GradleException("nested pack \"sourceFolder\" cannot be blank")
            }

            if (!nestedNames.add(name)) {
                throw GradleException("Duplicate nested pack name found: \"$name\"")
            }

            if (!sourceFolders.add(sourceFolder)) {
                throw GradleException("Duplicate sourceFolder found: \"$sourceFolder\"")
            }
        }
    }
}
