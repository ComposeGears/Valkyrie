package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data.CssGgIconMetadata

fun buildCssGgIcons(metadata: List<CssGgIconMetadata>): List<SvgIcon> {
    return metadata.map { icon ->
        SvgIcon(
            name = icon.name,
            displayName = icon.name.toDisplayName(),
            path = icon.path,
            tags = icon.tags,
            category = inferCategoryFromTags(icon.name, icon.tags),
        )
    }
}
