package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.toWebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain.SvgIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data.CssGgRepository

class CssGgUseCase(
    private val repository: CssGgRepository,
    private val inMemorySettings: InMemorySettings,
) : SvgIconProvider {

    override val providerName: String = "css.gg"
    override val stateKey: String = "cssgg"
    override val persistentSize: Int = inMemorySettings.readState { cssGgSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            cssGgSize = value
        }
    }

    override suspend fun loadConfig(): WebIconConfig<SvgIcon> {
        return buildCssGgIcons(repository.loadMetadata()).toWebIconConfig()
    }

    override suspend fun loadPreviewSvg(icon: SvgIcon): String {
        return repository.downloadSvg(icon.path)
    }

    override suspend fun downloadSvg(icon: SvgIcon, settings: SizeSettings, style: IconStyle?): String {
        val rawSvg = repository.downloadSvg(icon.path)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
