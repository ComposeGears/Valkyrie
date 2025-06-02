package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.FailedToParseClipboard
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.FailedToParseFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.HasDuplicates
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameContainsSpace
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameEmpty

fun List<BatchIcon>.checkExportIssues(): Map<ValidationError, List<IconName>> {
    return buildMap {
        val brokenIcons = this@checkExportIssues.filterIsInstance<BatchIcon.Broken>()

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
            icons = this@checkExportIssues
                .filter { it.iconName.name.isEmpty() }
                .map { it.iconName },
        )
        addIfNotEmpty(
            error = IconNameContainsSpace,
            icons = this@checkExportIssues
                .filter { it.iconName.name.contains(" ") }
                .map { it.iconName },
        )

        val duplicates = this@checkExportIssues
            .groupBy { it.iconName.name }
            .filter { it.value.size > 1 && it.value.any { it.iconName.name.isNotEmpty() } }
            .keys
            .map { IconName(it) }
        addIfNotEmpty(error = HasDuplicates, icons = duplicates)
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
        }
    }
}

private fun String.toQuotedString() = "\"$this\""
