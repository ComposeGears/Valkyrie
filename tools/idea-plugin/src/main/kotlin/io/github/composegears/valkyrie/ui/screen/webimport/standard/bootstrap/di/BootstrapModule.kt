package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data.BootstrapRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.domain.BootstrapUseCase

object BootstrapModule : Leviathan() {
    private val network = NetworkModule

    private val bootstrapRepository by instanceOf {
        BootstrapRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }

    val bootstrapUseCase by instanceOf {
        BootstrapUseCase(
            repository = inject(bootstrapRepository),
        )
    }
}
