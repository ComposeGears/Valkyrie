package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data.FeatherRepository

class FeatherUseCase(
    private val repository: FeatherRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Feather"
    override val stateKey: String = "feather"
    override val fontAlias: String = "Feather"
    override val persistentSize: Int = inMemorySettings.readState { featherSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            featherSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        return buildFeatherIcons(repository.loadCodepoints()).toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings, style: IconStyle?): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}

internal fun buildFeatherIcons(codepoints: Map<String, Int>): List<StandardIcon> {
    return codepoints.entries
        .sortedBy { it.key }
        .map { (name, codepoint) ->
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = Codepoint(codepoint),
                tags = emptyList(),
                category = inferCategoryFromTags(name, emptyList()),
            )
        }
}
