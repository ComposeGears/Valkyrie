package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.domain

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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data.BootstrapRepository

class BootstrapUseCase(
    private val repository: BootstrapRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Bootstrap"
    override val stateKey: String = "bootstrap"
    override val fontAlias: String = "bootstrap-icons"
    override val persistentSize: Int = inMemorySettings.readState { bootstrapSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            bootstrapSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val codepoints = repository.loadCodepoints()

        val icons: List<StandardIcon> = codepoints.map { (name, codepoint) ->
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = Codepoint(codepoint),
                tags = emptyList(),
                category = inferCategoryFromTags(name, emptyList()),
            )
        }

        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
