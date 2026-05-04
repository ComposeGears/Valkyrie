package io.github.composegears.valkyrie.ui.screen.webimport.common.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StyledWebIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings

interface WebIconProvider<Icon : StyledWebIcon, Config : WebIconConfig<Icon>> {
    val providerName: String
    val stateKey: String
    val persistentSize: Int

    fun updatePersistentSize(value: Int) {}

    suspend fun loadConfig(): Config
    suspend fun downloadSvg(icon: Icon, settings: SizeSettings, style: IconStyle? = null): String
}
