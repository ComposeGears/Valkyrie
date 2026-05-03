package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.domain

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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data.LucideRepository

class LucideUseCase(
    private val repository: LucideRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Lucide"
    override val stateKey: String = "lucide"
    override val fontAlias: String = "lucide"
    override val persistentSize: Int = inMemorySettings.readState { lucideSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            lucideSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val iconMetadataList = repository.loadIconList()
        val codepoints = repository.loadCodepoints()

        val icons: List<StandardIcon> = iconMetadataList.mapNotNull { (name, metadata) ->
            val codepoint = codepoints[name] ?: return@mapNotNull null
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = Codepoint(codepoint),
                tags = metadata.tags,
                category = inferCategoryFromTags(name, metadata.tags),
            )
        }

        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings, style: IconStyle?): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
