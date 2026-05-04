package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.WebIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon

interface SvgIconProvider : WebIconProvider<SvgIcon, WebIconConfig<SvgIcon>> {
    suspend fun loadPreviewSvg(icon: SvgIcon): String

    override suspend fun downloadSvg(
        icon: SvgIcon,
        settings: SizeSettings,
        style: IconStyle?,
    ): String
}
