package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data.LucideRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.toStandardIconConfig

class LucideUseCase(
    private val repository: LucideRepository,
) : StandardIconProvider {

    override val providerName: String = "Lucide"
    override val stateKey: String = "lucide"
    override val fontAlias: String = "lucide"

    override suspend fun loadConfig(): StandardIconConfig {
        val iconMetadataList = repository.loadIconList()
        val codepoints = repository.loadCodepoints()

        val icons: List<StandardIcon> = iconMetadataList.mapNotNull { (name, metadata) ->
            val codepoint = codepoints[name] ?: return@mapNotNull null
            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = codepoint,
                tags = metadata.tags,
                category = inferCategoryFromTags(name, metadata.tags),
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
