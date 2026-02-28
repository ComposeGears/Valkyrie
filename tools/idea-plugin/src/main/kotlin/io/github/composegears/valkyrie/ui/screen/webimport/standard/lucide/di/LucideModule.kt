package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data.LucideCodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data.LucideRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.domain.LucideUseCase

object LucideModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val lucideCodepointParser by factoryOf { LucideCodepointParser() }

    private val lucideRepository by instanceOf {
        LucideRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
            codepointParser = inject(lucideCodepointParser),
        )
    }

    val lucideUseCase by instanceOf {
        LucideUseCase(
            repository = inject(lucideRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
