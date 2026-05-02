package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data.HeroiconsIndexParser
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data.HeroiconsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.domain.HeroiconsUseCase

object HeroiconsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val heroiconsIndexParser by factoryOf { HeroiconsIndexParser(json = inject(network.json)) }

    private val heroiconsRepository by instanceOf {
        HeroiconsRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
            indexParser = inject(heroiconsIndexParser),
        )
    }

    val heroiconsUseCase by instanceOf {
        HeroiconsUseCase(
            repository = inject(heroiconsRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
