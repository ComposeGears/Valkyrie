package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data.CssGgIndexParser
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data.CssGgRepository
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.domain.CssGgUseCase

object CssGgModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val cssGgIndexParser by factoryOf { CssGgIndexParser(json = inject(network.json)) }

    private val cssGgRepository by instanceOf {
        CssGgRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
            indexParser = inject(cssGgIndexParser),
        )
    }

    val cssGgUseCase by instanceOf {
        CssGgUseCase(
            repository = inject(cssGgRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
