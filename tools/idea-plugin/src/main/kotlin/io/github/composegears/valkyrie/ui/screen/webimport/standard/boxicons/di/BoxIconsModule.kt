package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data.BoxCodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data.BoxIconsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.domain.BoxIconsUseCase

object BoxIconsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val boxCodepointParser by factoryOf { BoxCodepointParser() }

    private val boxIconsRepository by instanceOf {
        BoxIconsRepository(
            httpClient = inject(network.httpClient),
            codepointParser = inject(boxCodepointParser),
        )
    }

    val boxIconsUseCase by instanceOf {
        BoxIconsUseCase(
            repository = inject(boxIconsRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
