package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain.SvgIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.toSvgIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data.HeroiconsRepository

class HeroiconsUseCase(
    private val repository: HeroiconsRepository,
    private val inMemorySettings: InMemorySettings,
) : SvgIconProvider {

    override val providerName: String = "Heroicons"
    override val stateKey: String = "heroicons"
    override val persistentSize: Int = inMemorySettings.readState { heroiconsSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            heroiconsSize = value
        }
    }

    override suspend fun loadConfig(): SvgIconConfig {
        return buildHeroicons(repository.loadMetadata()).toSvgIconConfig()
    }

    override suspend fun loadPreviewSvg(icon: SvgIcon): String {
        return repository.downloadSvg(icon.path)
    }

    override suspend fun downloadSvg(icon: SvgIcon, settings: SizeSettings): String {
        val rawSvg = repository.downloadSvg(icon.path)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
