package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig
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

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String {
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
