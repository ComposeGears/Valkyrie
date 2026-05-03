package io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain

import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontVariation.grade
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.Codepoint
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.VariableFontConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.model.MaterialFontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.model.MaterialIconFontFamily
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MaterialSymbolsConfigUseCase(
    private val configRepository: MaterialSymbolsConfigRepository,
    private val fontRepository: MaterialFontRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    private var currentFontFamily: MaterialIconFontFamily = MaterialIconFontFamily.Outlined

    private val _fontSettingsFlow = MutableStateFlow(
        inMemorySettings.readState {
            MaterialFontSettings(
                fill = materialFontFill,
                weight = materialFontWeight,
                grade = materialFontGrade,
                opticalSize = materialFontOpticalSize,
            )
        },
    )
    val fontSettingsFlow = _fontSettingsFlow.asStateFlow()

    private val _variableFontConfig = MutableStateFlow<VariableFontConfig?>(
        _fontSettingsFlow.value.toVariableFontConfig(),
    )

    override val providerName: String = "Material Symbols"
    override val stateKey: String = "material_symbols"
    override val fontAlias: String = "materialsymbols"
    override val persistentSize: Int = SizeSettings.DEFAULT_SIZE
    override val variableFontConfig = _variableFontConfig.asStateFlow()

    override fun resolveFontWeight(style: IconStyle?) = FontWeight(_fontSettingsFlow.value.weight)

    override fun onStyleChanged(style: IconStyle?) {
        currentFontFamily = FAMILY_BY_STYLE_ID[style?.id] ?: MaterialIconFontFamily.Outlined
    }

    override suspend fun loadConfig(): StandardIconConfig {
        val result = configRepository.load()

        // Filter only Material Symbols icons (exclude older Material Icons families)
        val filteredIcons = result.icons.filterNot { icon ->
            icon.unsupportedFamilies.any { it.startsWith("Material Icons") }
        }

        val categories = filteredIcons
            .flatMap { it.categories }
            .distinct()
            .sorted()
            .map { InferredCategory(id = it.lowercase().replace(" ", "_"), name = it.toCategoryName()) }

        val icons = filteredIcons.map { icon ->
            StandardIcon(
                name = icon.name,
                displayName = icon.name.toIconName(),
                exportName = icon.name.toIconName(),
                codepoint = Codepoint(icon.codepoint),
                tags = emptyList(),
                category = InferredCategory(
                    id = (icon.categories.firstOrNull() ?: "uncategorized").lowercase().replace(" ", "_"),
                    name = (icon.categories.firstOrNull() ?: "uncategorized").toCategoryName(),
                ),
                style = null,
            )
        }

        val allCategory = InferredCategory(id = "all", name = "All")
        val groupedIcons = icons.groupBy { it.category }
        val styles = MaterialIconFontFamily.entries.map { it.toIconStyle() }

        return StandardIconConfig(
            gridItems = groupedIcons,
            categories = listOf(allCategory) + categories,
            styles = styles,
        )
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        val fontFamily = FAMILY_BY_STYLE_ID[style?.id] ?: MaterialIconFontFamily.Outlined
        return FontByteArray(bytes = fontRepository.loadFont(fontFamily))
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String = fontRepository.downloadSvg(
        name = icon.name,
        fontFamily = currentFontFamily.fontFamily,
        fontSettings = _fontSettingsFlow.value,
    )

    fun updateFontSettings(settings: MaterialFontSettings) {
        _fontSettingsFlow.value = settings
        _variableFontConfig.value = settings.toVariableFontConfig()
        inMemorySettings.update {
            materialFontFill = settings.fill
            materialFontWeight = settings.weight
            materialFontGrade = settings.grade
            materialFontOpticalSize = settings.opticalSize
        }
    }

    private fun MaterialFontSettings.toVariableFontConfig() = VariableFontConfig(
        variation = FontVariation.Settings(
            FontVariation.Setting("FILL", if (fill) 1f else 0f),
            grade(grade),
            FontVariation.opticalSizing(TextUnit(opticalSize, TextUnitType.Sp)),
        ),
        weights = MATERIAL_FONT_WEIGHTS,
        weight = FontWeight(weight),
        opticalSize = opticalSize,
    )

    private fun MaterialIconFontFamily.toIconStyle() = IconStyle(
        id = name.lowercase(),
        name = displayName,
    )

    companion object {
        private val MATERIAL_FONT_WEIGHTS = listOf(
            FontWeight.W100,
            FontWeight.W200,
            FontWeight.W300,
            FontWeight.W400,
            FontWeight.W500,
            FontWeight.W600,
            FontWeight.W700,
        )

        private val FAMILY_BY_STYLE_ID = MaterialIconFontFamily.entries.associateBy { it.name.lowercase() }
    }
}

private fun String.toIconName(): String = replace('_', ' ')
    .split(' ')
    .joinToString(" ") { it.capitalized() }

private fun String.toCategoryName(): String = when (lowercase()) {
    "av" -> "Audio & Video"
    else -> capitalized()
}
