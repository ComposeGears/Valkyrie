package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data.HeroiconsIconMetadata

fun buildHeroicons(metadata: List<HeroiconsIconMetadata>): List<SvgIcon> {
    val supportedMetadata = metadata.filter { it.styleId in SUPPORTED_HEROICONS_STYLES }
    val namesWithVariants = supportedMetadata
        .groupingBy { it.name }
        .eachCount()
        .filterValues { it > 1 }
        .keys

    return supportedMetadata.map { icon ->
        val style = icon.styleId.toHeroiconsStyle()
        SvgIcon(
            name = icon.name,
            displayName = icon.name.toDisplayName(),
            exportName = if (icon.name in namesWithVariants) "${icon.name}-${style.id}" else icon.name,
            path = icon.path,
            tags = icon.tags,
            category = inferCategoryFromTags(icon.name, icon.tags),
            style = style,
        )
    }
}

private fun String.toHeroiconsStyle(): IconStyle {
    val style = substringAfter('-')
    return IconStyle(id = style, name = style.replaceFirstChar(Char::uppercase))
}

private val SUPPORTED_HEROICONS_STYLES = setOf("24-outline", "24-solid")
