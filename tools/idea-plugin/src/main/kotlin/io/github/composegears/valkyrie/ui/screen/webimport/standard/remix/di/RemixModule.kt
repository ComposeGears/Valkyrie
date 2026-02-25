package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data.RemixRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.domain.RemixUseCase

object RemixModule : Leviathan() {
    private val network = NetworkModule
    private val coreModule = coreModule()

    private val remixRepository by instanceOf {
        RemixRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }

    val remixUseCase by instanceOf {
        RemixUseCase(
            repository = inject(remixRepository),
            inMemorySettings = inject(coreModule.inMemorySettings),
        )
    }
}
