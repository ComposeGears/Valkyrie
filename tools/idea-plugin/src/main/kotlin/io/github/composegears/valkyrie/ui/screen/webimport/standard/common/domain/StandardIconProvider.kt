package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import androidx.compose.ui.text.font.FontWeight
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgImportSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgCustomizationCapabilities

interface StandardIconProvider {
    val providerName: String
    val stateKey: String
    val fontAlias: String
    val persistentSize: Int
    val persistentLastCustomColor: String?
    val persistentRecentColors: List<String>
    val customizationCapabilities: SvgCustomizationCapabilities
        get() = SvgCustomizationCapabilities()

    fun updatePersistentSize(value: Int)
    fun updatePersistentCustomColors(lastCustomColor: String?, recentColors: List<String>)
    fun resolveFontWeight(style: IconStyle?): FontWeight = FontWeight.W400

    suspend fun loadConfig(): StandardIconConfig
    suspend fun loadFontBytes(style: IconStyle? = null): FontByteArray
    suspend fun loadSvg(icon: StandardIcon): String

    fun applySettings(svgContent: String, settings: SvgImportSettings): String =
        SvgImportCustomizer.applySettings(svgContent, settings)

    suspend fun downloadSvg(icon: StandardIcon, settings: SvgImportSettings): String =
        applySettings(loadSvg(icon), settings)
}
