package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data.BoxIconsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.inferCategoryFromTags
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.toStandardIconConfig

class BoxIconsUseCase(
    private val repository: BoxIconsRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "BoxIcons"
    override val stateKey: String = "boxicons"
    override val fontAlias: String = "boxicons"
    override val persistentSize: Int = inMemorySettings.readState { boxiconsSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            boxiconsSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val icons = repository.loadCodepoints().map { entry ->
            val style = entry.iconName.toStyle()
            val displayName = entry.iconName.toDisplayNameWithoutPrefix(style)

            StandardIcon(
                name = entry.iconName,
                displayName = displayName,
                exportName = entry.iconName.toExportName(),
                codepoint = entry.codepoint,
                tags = emptyList(),
                category = inferCategoryFromTags(entry.iconName, emptyList()),
                style = style.value,
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

private enum class BoxIconsStyle(val prefix: String, val value: IconStyle) {
    Regular("bx-", IconStyle(id = "regular", name = "Regular")),
    Solid("bxs-", IconStyle(id = "solid", name = "Solid")),
    Logos("bxl-", IconStyle(id = "logos", name = "Logos")),
}

private fun String.toStyle(): BoxIconsStyle {
    return when {
        startsWith("bxs-") -> BoxIconsStyle.Solid
        startsWith("bxl-") -> BoxIconsStyle.Logos
        else -> BoxIconsStyle.Regular
    }
}

private fun String.toDisplayNameWithoutPrefix(style: BoxIconsStyle): String {
    return removePrefix(style.prefix).toDisplayName()
}

internal fun String.toExportName(): String {
    val style = toStyle()
    val baseName = toDisplayNameWithoutPrefix(style)
    return baseName + style.value.name
}
