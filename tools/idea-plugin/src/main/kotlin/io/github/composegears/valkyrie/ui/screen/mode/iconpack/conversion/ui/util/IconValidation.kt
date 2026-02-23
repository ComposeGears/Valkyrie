package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.FailedToParseClipboard
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.FailedToParseFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.HasCaseInsensitiveDuplicates
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.HasDuplicates
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameContainsSpace
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameEmpty

fun List<BatchIcon>.checkImportIssues(useFlatPackage: Boolean = false): Map<ValidationError, List<IconName>> {
    return buildMap {
        val brokenIcons = this@checkImportIssues.filterIsInstance<BatchIcon.Broken>()

        addIfNotEmpty(
            error = FailedToParseFile,
            icons = brokenIcons
                .filter { it.iconSource == IconSource.File }
                .map { it.iconName },
        )
        addIfNotEmpty(
            error = FailedToParseClipboard,
            icons = brokenIcons
                .filter { it.iconSource == IconSource.Clipboard }
                .map { it.iconName },
        )
        addIfNotEmpty(
            error = IconNameEmpty,
            icons = this@checkImportIssues
                .filter { it.iconName.name.isEmpty() }
                .map { it.iconName },
        )
        addIfNotEmpty(
            error = IconNameContainsSpace,
            icons = this@checkImportIssues
                .filter { it.iconName.name.contains(" ") }
                .map { it.iconName },
        )

        val duplicates = this@checkImportIssues
            .filterIsInstance<BatchIcon.Valid>()
            .groupBy {
                // When useFlatPackage is true, all icons go to the same folder regardless of nested pack
                // So we need to check for duplicates across all nested packs within the same icon pack
                val packIdentifier = when (val pack = it.iconPack) {
                    is IconPack.Single -> pack.iconPackName
                    is IconPack.Nested -> when {
                        useFlatPackage -> pack.iconPackName  // Flat package: check across all nested packs
                        else -> "${pack.iconPackName}.${pack.currentNestedPack}"  // Separate folders per nested pack
                    }
                }
                packIdentifier to it.iconName.name
            }
            .filter { it.value.size > 1 }
            .values
            .flatten()
            .map { it.iconName }
            .distinct()

        addIfNotEmpty(error = HasDuplicates, icons = duplicates)

        // Check for case-insensitive duplicates (file system collision on macOS/Windows)
        val caseInsensitiveDuplicates = this@checkImportIssues
            .filterIsInstance<BatchIcon.Valid>()
            .groupBy {
                // When useFlatPackage is true, all icons go to the same folder regardless of nested pack
                // So we need to check for duplicates across all nested packs within the same icon pack
                val packIdentifier = when (val pack = it.iconPack) {
                    is IconPack.Single -> pack.iconPackName
                    is IconPack.Nested -> when {
                        useFlatPackage -> pack.iconPackName  // Flat package: check across all nested packs
                        else -> "${pack.iconPackName}.${pack.currentNestedPack}"  // Separate folders per nested pack
                    }
                }
                packIdentifier to it.iconName.name.lowercase()
            }
            .filter {
                // Filter groups where there are multiple icons with the same lowercase name
                // but they are not already exact duplicates
                it.value.size > 1 &&
                it.value.map { icon -> icon.iconName.name }.distinct().size > 1
            }
            .values
            .flatten()
            .map { it.iconName }
            .distinct()

        addIfNotEmpty(error = HasCaseInsensitiveDuplicates, icons = caseInsensitiveDuplicates)
    }
}

private fun MutableMap<ValidationError, List<IconName>>.addIfNotEmpty(
    error: ValidationError,
    icons: List<IconName>,
) {
    if (icons.isNotEmpty()) {
        put(error, icons)
    }
}

fun Map<ValidationError, List<IconName>>.toMessageText(): String {
    return entries.joinToString("\n") { (error, icons) ->
        val value = icons.joinToString { it.name.toQuotedString() }
        val isPlural = icons.size > 1

        when (error) {
            FailedToParseFile -> "• Failed to parse: $value"
            FailedToParseClipboard -> "• Failed to parse some icon from clipboard"
            IconNameEmpty -> "• Contains icon${if (isPlural) "s" else ""} with empty name"
            IconNameContainsSpace -> "• Contains icon${if (isPlural) "s" else ""} with space in name: $value"
            HasDuplicates -> "• Contains duplicate icon${if (isPlural) "s" else ""}: $value"
            HasCaseInsensitiveDuplicates -> "• Contains icon${if (isPlural) "s" else ""} that collide on case-insensitive file systems (macOS/Windows): $value"
        }
    }
}

private fun String.toQuotedString() = "\"$this\""
