package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data.RemixRepository

class RemixUseCase(
    private val repository: RemixRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Remix"
    override val stateKey: String = "remix"
    override val fontAlias: String = "remixicon"
    override val persistentSize: Int = inMemorySettings.readState { remixSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            remixSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val iconMetadata = repository.loadIconList()

        val icons: List<StandardIcon> = iconMetadata.map { (name, metadata) ->
            val categoryName = metadata.categoryName
            val category = if (categoryName.isNullOrBlank()) {
                inferCategoryFromTags(name, emptyList())
            } else {
                InferredCategory(
                    id = categoryName.lowercase().replace(' ', '-'),
                    name = categoryName,
                )
            }

            StandardIcon(
                name = name,
                displayName = name.toDisplayName(),
                codepoint = Codepoint(metadata.codepoint),
                tags = emptyList(),
                category = category,
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
