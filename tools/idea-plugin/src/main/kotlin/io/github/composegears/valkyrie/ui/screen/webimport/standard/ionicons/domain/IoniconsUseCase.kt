package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.domain

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.toStandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.SvgSizeCustomizer
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsRepository

class IoniconsUseCase(
    private val repository: IoniconsRepository,
    private val inMemorySettings: InMemorySettings,
) : StandardIconProvider {

    override val providerName: String = "Ionicons"
    override val stateKey: String = "ionicons"
    override val fontAlias: String = "ionicons"
    override val persistentSize: Int = inMemorySettings.readState { ioniconsSize }

    override fun updatePersistentSize(value: Int) {
        inMemorySettings.update {
            ioniconsSize = value
        }
    }

    override suspend fun loadConfig(): StandardIconConfig {
        return buildIonicons(
            metadata = repository.loadMetadata(),
            codepoints = repository.loadCodepoints(),
        ).toStandardIconConfig()
    }

    override suspend fun loadFontBytes(style: IconStyle?): FontByteArray {
        return FontByteArray(repository.loadFontBytes())
    }

    override suspend fun downloadSvg(icon: StandardIcon, settings: SizeSettings): String {
        val rawSvg = repository.downloadSvg(icon.name)
        return SvgSizeCustomizer.applySettings(rawSvg, settings)
    }
}
