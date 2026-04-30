package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsGlyphMapParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsMetadataParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.domain.IoniconsUseCase

object IoniconsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val ioniconsGlyphMapParser by factoryOf { IoniconsGlyphMapParser(json = inject(network.json)) }
    private val ioniconsMetadataParser by factoryOf { IoniconsMetadataParser(json = inject(network.json)) }

    private val ioniconsRepository by instanceOf {
        IoniconsRepository(
            httpClient = inject(network.httpClient),
            glyphMapParser = inject(ioniconsGlyphMapParser),
            metadataParser = inject(ioniconsMetadataParser),
        )
    }

    val ioniconsUseCase by instanceOf {
        IoniconsUseCase(
            repository = inject(ioniconsRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
