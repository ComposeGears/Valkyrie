package io.github.composegears.valkyrie.ui.screen.webimport.material.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule.httpClient
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.MaterialSymbolsConfigUseCase

object MaterialSymbolsModule : Leviathan() {
    private val network = NetworkModule

    private val materialSymbolsConfigRepository by instanceOf {
        MaterialSymbolsConfigRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }
    private val materialFontRepository by instanceOf {
        MaterialFontRepository(httpClient = inject(httpClient))
    }
    val materialSymbolsConfigUseCase by instanceOf {
        MaterialSymbolsConfigUseCase(
            configRepository = inject(materialSymbolsConfigRepository),
            fontRepository = inject(materialFontRepository),
        )
    }
}
