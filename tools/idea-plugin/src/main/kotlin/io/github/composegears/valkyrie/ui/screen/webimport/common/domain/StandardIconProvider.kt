package io.github.composegears.valkyrie.ui.screen.webimport.common.domain

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontWeight
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.VariableFontConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
interface StandardIconProvider {
    val providerName: String
    val stateKey: String
    val fontAlias: String
    val persistentSize: Int

    /**
     * Optional variable font configuration for providers that support font variation axes
     * (e.g. Material Symbols with FILL, grade, optical size axes).
     * Returns a flow of `null` for standard providers that use fixed fonts.
     */
    val variableFontConfig: StateFlow<VariableFontConfig?>
        get() = NoVariableFontConfig

    fun updatePersistentSize(value: Int) {}
    fun resolveFontWeight(style: IconStyle?): FontWeight = FontWeight.W400

    /**
     * Called by the ViewModel when the selected style changes.
     * Override in providers that need to track the current style for SVG downloads
     * (e.g. Material Symbols, where style = font family selection, not a per-icon property).
     */
    fun onStyleChanged(style: IconStyle?) {}

    suspend fun loadConfig(): StandardIconConfig
    suspend fun loadFontBytes(style: IconStyle? = null): FontByteArray
    suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings, style: IconStyle? = null): String

    companion object {
        /** Shared no-op StateFlow returned by the default [variableFontConfig] getter. */
        private val NoVariableFontConfig: StateFlow<VariableFontConfig?> = MutableStateFlow(null)
    }
}
