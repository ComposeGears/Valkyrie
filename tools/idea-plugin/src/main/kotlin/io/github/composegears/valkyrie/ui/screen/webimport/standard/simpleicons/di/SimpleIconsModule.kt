package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data.SimpleIconsMetadataParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data.SimpleIconsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.domain.SimpleIconsUseCase

object SimpleIconsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val simpleIconsMetadataParser by factoryOf { SimpleIconsMetadataParser(json = inject(network.json)) }

    private val simpleIconsRepository by instanceOf {
        SimpleIconsRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
            metadataParser = inject(simpleIconsMetadataParser),
        )
    }

    val simpleIconsUseCase by instanceOf {
        SimpleIconsUseCase(
            repository = inject(simpleIconsRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
