package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data.FeatherGlyphMapParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data.FeatherRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.domain.FeatherUseCase

object FeatherModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val featherGlyphMapParser by factoryOf { FeatherGlyphMapParser() }

    private val featherRepository by instanceOf {
        FeatherRepository(
            httpClient = inject(network.httpClient),
            glyphMapParser = inject(featherGlyphMapParser),
        )
    }

    val featherUseCase by instanceOf {
        FeatherUseCase(
            repository = inject(featherRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
