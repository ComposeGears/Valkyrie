package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.toStandardIconConfig
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
                codepoint = metadata.codepoint,
                tags = emptyList(),
                category = category,
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
