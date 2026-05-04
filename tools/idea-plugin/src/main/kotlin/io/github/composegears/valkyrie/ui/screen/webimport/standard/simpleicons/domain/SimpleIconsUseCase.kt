package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data.SimpleIconMetadata
import io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data.SimpleIconsRepository

class SimpleIconsUseCase(
    private val repository: SimpleIconsRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Simple Icons"
    override val stateKey: String = "simple-icons"
    override val fontAlias: String = "simple-icons"
    override val persistentSize: Int = inMemorySettings.readState { simpleIconsSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            simpleIconsSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val icons = repository.loadMetadata().map { it.toStandardIcon() }
        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings, style: IconStyle?): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }

    private fun SimpleIconMetadata.toStandardIcon(): StandardIcon {
        return StandardIcon(
            name = slug,
            displayName = title,
            exportName = slug.toExportName(),
            codepoint = Codepoint(codepoint),
            tags = buildList {
                addAll(slug.split('-'))
                add(title)
                addAll(aliases)
                if (hex.isNotBlank()) add(hex)
            }.distinct(),
            category = BRAND_CATEGORY,
        )
    }

    private companion object {
        private val BRAND_CATEGORY = InferredCategory(id = "brands", name = "Brands")
    }
}

internal fun String.toExportName(): String {
    return if (firstOrNull()?.isDigit() == true) "icon-$this" else this
}
