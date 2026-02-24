package io.github.composegears.valkyrie.ui.screen.webimport.material.domain

import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialConfig
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialIconFontFamily

class MaterialSymbolsConfigUseCase(
    private val configRepository: MaterialSymbolsConfigRepository,
    private val fontRepository: MaterialFontRepository,
) {
    suspend fun loadConfig(): MaterialConfig {
        val result = configRepository.load()

        // Filter only Material Symbols icons (exclude older Material Icons families)
        val filteredIcons = result.icons.filterNot { icon ->
            icon.unsupportedFamilies.any { family -> family.startsWith("Material Icons") }
        }

        val categories = filteredIcons
            .flatMap { it.categories }
            .distinct()
            .sorted()
            .map { Category(name = it.toCategoryName()) }

        val icons = filteredIcons.map { icon ->
            IconModel(
                name = icon.name.toIconName(),
                originalName = icon.name,
                codepoint = icon.codepoint,
                category = Category(
                    name = (icon.categories.firstOrNull() ?: "uncategorized").toCategoryName(),
                ),
            )
        }

        return MaterialConfig(
            gridItems = icons.groupBy { it.category },
            categories = listOf(Category.All) + categories,
        )
    }

    suspend fun loadFont(iconFontFamily: MaterialIconFontFamily): FontByteArray {
        val bytes = fontRepository.downloadFont(iconFontFamily.cdnUrl)

        return FontByteArray(bytes = bytes)
    }

    suspend fun loadIcon(
        name: String,
        fontFamily: String,
        fontSettings: MaterialFontSettings,
    ): String {
        return fontRepository.downloadSvg(
            name = name,
            fontFamily = fontFamily,
            fontSettings = fontSettings,
        )
    }
}

private fun String.toIconName(): String {
    return replace('_', ' ')
        .split(' ')
        .joinToString(
            separator = " ",
            transform = {
                it.capitalized()
            },
        )
}

private fun String.toCategoryName(): String {
    return when (lowercase()) {
        "av" -> "Audio & Video"
        else -> capitalized()
    }
}
