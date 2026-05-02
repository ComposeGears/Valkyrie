package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIconConfig

interface SvgIconProvider {
    val providerName: String
    val stateKey: String
    val persistentSize: Int

    fun updatePersistentSize(value: Int)

    suspend fun loadConfig(): SvgIconConfig
    suspend fun loadPreviewSvg(icon: SvgIcon): String
    suspend fun downloadSvg(icon: SvgIcon, settings: SizeSettings): String
}
