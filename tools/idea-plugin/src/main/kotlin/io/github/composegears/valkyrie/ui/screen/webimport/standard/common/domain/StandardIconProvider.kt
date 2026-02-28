package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig

interface StandardIconProvider {
    val providerName: String
    val stateKey: String
    val fontAlias: String
    val persistentSize: Int

    fun updatePersistentSize(value: Int)

    suspend fun loadConfig(): StandardIconConfig
    suspend fun loadFontBytes(): FontByteArray
    suspend fun downloadSvg(iconName: String, settings: SizeSettings): String
}
