package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data.BootstrapCodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data.BootstrapRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.domain.BootstrapUseCase

object BootstrapModule : Leviathan() {
    private val network = NetworkModule
    private val coreModule = coreModule()

    private val bootstrapCodepointParser by factoryOf { BootstrapCodepointParser() }

    private val bootstrapRepository by instanceOf {
        BootstrapRepository(
            httpClient = inject(network.httpClient),
            codepointParser = inject(bootstrapCodepointParser),
        )
    }

    val bootstrapUseCase by instanceOf {
        BootstrapUseCase(
            repository = inject(bootstrapRepository),
            inMemorySettings = inject(coreModule.inMemorySettings),
        )
    }
}
