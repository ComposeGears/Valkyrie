package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.toPersistedString
import io.github.composegears.valkyrie.settings.toStringList
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data.RemixRepository

class RemixUseCase(
    private val repository: RemixRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Remix"
    override val stateKey: String = "remix"
    override val fontAlias: String = "remixicon"
    override val persistentSize: Int = inMemorySettings.readState { remixSize }
    override val persistentLastCustomColor: String? = inMemorySettings.readState { remixLastCustomColor }?.ifBlank { null }
    override val persistentRecentColors: List<String> = inMemorySettings.readState { remixRecentColors }.toStringList()

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            remixSize = value
        }
    }

    override fun updatePersistentCustomColors(lastCustomColor: String?, recentColors: List<String>) {
        inMemorySettings.update {
            remixLastCustomColor = lastCustomColor.orEmpty()
            remixRecentColors = recentColors.toPersistedString()
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

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun loadSvg(icon: StandardIcon): String = repository.downloadSvg(icon.name)
}
