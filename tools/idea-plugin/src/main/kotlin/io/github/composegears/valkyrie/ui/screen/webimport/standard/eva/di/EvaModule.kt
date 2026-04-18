package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data.EvaCodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data.EvaRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.domain.EvaUseCase

object EvaModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val evaCodepointParser by factoryOf { EvaCodepointParser() }

    private val evaRepository by instanceOf {
        EvaRepository(
            httpClient = inject(network.httpClient),
            codepointParser = inject(evaCodepointParser),
        )
    }

    val evaUseCase by instanceOf {
        EvaUseCase(
            repository = inject(evaRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
