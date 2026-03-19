package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.domain

import androidx.compose.ui.text.font.FontWeight
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data.FontAwesomeIconMetadata
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data.FontAwesomeRepository

class FontAwesomeUseCase(
    private val repository: FontAwesomeRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Font Awesome"
    override val stateKey: String = "fontawesome"
    override val fontAlias: String = "fontawesome"
    override val persistentSize: Int = inMemorySettings.readState { fontAwesomeSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            fontAwesomeSize = value
        }
    }

    override fun resolveFontWeight(style: IconStyle?): FontWeight {
        return if (style?.id == SOLID_STYLE_ID) FontWeight.W900 else FontWeight.W400
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val metadata = repository.loadIconsMetadata()
        val categoriesByIcon = repository.loadCategoriesByIcon()

        val icons = metadata.flatMap { icon ->
            val styles = icon.styles.mapNotNull(STYLE_BY_ID::get)
            val category = categoriesByIcon[icon.name] ?: inferCategoryFromTags(icon.name, icon.searchTerms)

            val codepoint = icon.unicodeHex.toIntOrNull(16) ?: return@flatMap emptyList()

            styles.map { style ->
                StandardIcon(
                    name = icon.name,
                    displayName = icon.label.ifBlank { icon.name.toDisplayName() },
                    exportName = icon.toExportName(style = style, stylesCount = styles.size),
                    codepoint = codepoint,
                    tags = icon.searchTerms,
                    category = category,
                    style = style,
                )
            }
        }

        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        val styleId = style?.id ?: SOLID_STYLE_ID
        return FontByteArray(repository.loadFontBytes(styleId = styleId))
    }

    override suspend fun loadSvg(icon: StandardIcon): String {
        val styleId = icon.style?.id ?: SOLID_STYLE_ID
        return repository.downloadSvg(iconName = icon.name, styleId = styleId)
    }

    private fun FontAwesomeIconMetadata.toExportName(style: IconStyle, stylesCount: Int): String {
        return if (stylesCount > 1) {
            "$name-${style.id}"
        } else {
            name
        }
    }

    private companion object {
        private const val SOLID_STYLE_ID = "solid"
        private const val REGULAR_STYLE_ID = "regular"
        private const val BRANDS_STYLE_ID = "brands"

        private val STYLE_BY_ID = mapOf(
            SOLID_STYLE_ID to IconStyle(id = SOLID_STYLE_ID, name = "Solid"),
            REGULAR_STYLE_ID to IconStyle(id = REGULAR_STYLE_ID, name = "Regular"),
            BRANDS_STYLE_ID to IconStyle(id = BRANDS_STYLE_ID, name = "Brands"),
        )
    }
}
