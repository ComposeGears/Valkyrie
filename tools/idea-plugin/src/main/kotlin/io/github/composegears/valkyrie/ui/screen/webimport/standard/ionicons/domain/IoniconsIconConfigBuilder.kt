package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsIconMetadata

fun buildIonicons(
    metadata: List<IoniconsIconMetadata>,
    codepoints: Map<String, Int>,
): List<StandardIcon> {
    val baseNamesWithVariants = metadata
        .map { it.name.removeStyleSuffix() }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys

    return metadata.mapNotNull { icon ->
        val codepoint = codepoints[icon.name] ?: return@mapNotNull null
        val baseName = icon.name.removeStyleSuffix()
        val style = icon.name.toIoniconsStyle()

        StandardIcon(
            name = icon.name,
            displayName = baseName.toDisplayName(),
            exportName = if (baseName in baseNamesWithVariants && style == FILLED_STYLE) {
                "$baseName-${style.id}"
            } else {
                icon.name
            },
            codepoint = Codepoint(codepoint),
            tags = icon.tags,
            category = inferCategoryFromTags(icon.name, icon.tags),
            style = style,
        )
    }
}

private fun String.removeStyleSuffix(): String {
    return removeSuffix("-outline").removeSuffix("-sharp")
}

private fun String.toIoniconsStyle(): IconStyle {
    return when {
        endsWith("-outline") -> OUTLINE_STYLE
        endsWith("-sharp") -> SHARP_STYLE
        else -> FILLED_STYLE
    }
}

private val FILLED_STYLE = IconStyle(id = "filled", name = "Filled")
private val OUTLINE_STYLE = IconStyle(id = "outline", name = "Outline")
private val SHARP_STYLE = IconStyle(id = "sharp", name = "Sharp")
