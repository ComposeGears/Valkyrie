package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import androidx.compose.ui.text.font.FontWeight
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig

interface StandardIconProvider {
    val providerName: String
    val stateKey: String
    val fontAlias: String
    val persistentSize: Int

    fun updatePersistentSize(value: Int)
    fun resolveFontWeight(style: IconStyle?): FontWeight = FontWeight.W400

    suspend fun loadConfig(): StandardIconConfig
    suspend fun loadFontBytes(style: IconStyle? = null): FontByteArray
    suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String
}
