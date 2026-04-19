package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data.TablerRepository

internal val TABLER_OUTLINE_STYLE = IconStyle(id = "outline", name = "Outline")
internal val TABLER_FILLED_STYLE = IconStyle(id = "filled", name = "Filled")

class TablerUseCase(
    private val repository: TablerRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Tabler"
    override val stateKey: String = "tabler"
    override val fontAlias: String = "tabler-icons"
    override val persistentSize: Int = inMemorySettings.readState { tablerSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            tablerSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val icons = buildTablerIcons(
            outlineCodepoints = repository.loadOutlineCodepoints(),
            filledCodepoints = repository.loadFilledCodepoints(),
        )
        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray = FontByteArray(repository.loadFontBytes(style))

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String = repository.downloadSvg(
        iconName = icon.name,
        style = icon.style ?: TABLER_OUTLINE_STYLE,
    )
}

internal fun buildTablerIcons(
    outlineCodepoints: Map<String, Int>,
    filledCodepoints: Map<String, Int>,
): List<StandardIcon> = (outlineCodepoints.keys + filledCodepoints.keys)
    .distinct()
    .sorted()
    .flatMap { name ->
        val styles = buildList {
            outlineCodepoints[name]?.let { add(TABLER_OUTLINE_STYLE to it) }
            filledCodepoints[name]?.let { add(TABLER_FILLED_STYLE to it) }
        }
        val hasMultipleStyles = styles.size > 1
        styles.map { (style, codepoint) ->
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                exportName = if (hasMultipleStyles) "$name-${style.id}" else name,
                codepoint = codepoint,
                tags = emptyList(),
                category = inferCategoryFromTags(name, emptyList()),
                style = style,
            )
        }
    }
