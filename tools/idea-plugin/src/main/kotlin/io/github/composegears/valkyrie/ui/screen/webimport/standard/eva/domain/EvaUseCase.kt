package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data.EvaRepository

class EvaUseCase(
    private val repository: EvaRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Eva Icons"
    override val stateKey: String = "eva"
    override val fontAlias: String = "eva-icons"
    override val persistentSize: Int = inMemorySettings.readState { evaSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            evaSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val icons = repository.loadCodepoints().map { (name, codepoint) ->
            val displayName = name.toEvaDisplayName()
            StandardIcon(
                name = name,
                displayName = displayName,
                codepoint = Codepoint(codepoint),
                tags = emptyList(),
                category = inferCategoryFromTags(displayName, emptyList()),
                style = name.toEvaStyle(),
            )
        }

        return icons.toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray = FontByteArray(repository.loadFontBytes())

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String = repository.downloadSvg(icon.name)
}

internal fun String.toEvaStyle(): IconStyle {
    return if (endsWith(EVA_OUTLINE_SUFFIX)) {
        IconStyle(id = "outline", name = "Outline")
    } else {
        IconStyle(id = "fill", name = "Fill")
    }
}

internal fun String.toEvaDisplayName(): String {
    return removeSuffix(EVA_OUTLINE_SUFFIX).toDisplayName()
}

private const val EVA_OUTLINE_SUFFIX = "-outline"
