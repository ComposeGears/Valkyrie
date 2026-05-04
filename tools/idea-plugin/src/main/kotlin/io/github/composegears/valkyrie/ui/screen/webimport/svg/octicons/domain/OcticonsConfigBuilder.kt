package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data.OcticonsIconMetadata

fun buildOcticons(metadata: List<OcticonsIconMetadata>): List<SvgIcon> {
    val supportedMetadata = metadata.filter { it.size in SUPPORTED_OCTICONS_SIZES && !it.name.hasRestrictedBrandMark() }
    val namesWithVariants = supportedMetadata
        .groupingBy { it.name }
        .eachCount()
        .filterValues { it > 1 }
        .keys

    return supportedMetadata.map { icon ->
        val style = icon.size.toOcticonsStyle()
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

private fun String.toOcticonsStyle(): IconStyle {
    return IconStyle(id = this, name = "${this}px")
}

private fun String.hasRestrictedBrandMark(): Boolean {
    return startsWith("logo-") || startsWith("lockup-") || startsWith("mark-")
}

private val SUPPORTED_OCTICONS_SIZES = setOf("16", "24")
