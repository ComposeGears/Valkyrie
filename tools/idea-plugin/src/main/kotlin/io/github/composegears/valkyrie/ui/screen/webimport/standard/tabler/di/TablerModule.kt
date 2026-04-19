package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data.TablerCodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data.TablerRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.domain.TablerUseCase

object TablerModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val tablerCodepointParser by factoryOf { TablerCodepointParser() }

    private val tablerRepository by instanceOf {
        TablerRepository(
            httpClient = inject(network.httpClient),
            codepointParser = inject(tablerCodepointParser),
        )
    }

    val tablerUseCase by instanceOf {
        TablerUseCase(
            repository = inject(tablerRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
