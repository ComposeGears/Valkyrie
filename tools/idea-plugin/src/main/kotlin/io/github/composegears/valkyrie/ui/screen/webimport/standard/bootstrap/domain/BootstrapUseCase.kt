package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data.BootstrapRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.toStandardIconConfig

class BootstrapUseCase(
    private val repository: BootstrapRepository,
) : StandardIconProvider {

    override val providerName: String = "Bootstrap"
    override val stateKey: String = "bootstrap"
    override val fontAlias: String = "bootstrap-icons"

    override suspend fun loadConfig(): StandardIconConfig {
        val codepoints = repository.loadIconList()

        val icons: List<StandardIcon> = codepoints.map { (name, codepoint) ->
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = codepoint,
                tags = emptyList(),
                category = inferCategoryFromTags(name, emptyList()),
            )
        }

        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(iconName: String, settings: SizeSettings): String {
        val rawSvg = repository.downloadSvg(iconName)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
