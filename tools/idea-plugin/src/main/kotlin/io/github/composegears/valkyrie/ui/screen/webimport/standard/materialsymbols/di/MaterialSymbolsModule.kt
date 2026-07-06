package io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.di

import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.factoryOf
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.MaterialSymbolsConfigUseCase

object MaterialSymbolsModule : Leviathan {
    private val network = NetworkModule
    private val core = coreModule()

    private val materialSymbolsConfigRepository by factoryOf {
        MaterialSymbolsConfigRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }
    private val materialFontRepository by factoryOf {
        MaterialFontRepository(httpClient = inject(NetworkModule.httpClient))
    }

    val materialSymbolsConfigUseCase by factoryOf {
        MaterialSymbolsConfigUseCase(
            configRepository = inject(materialSymbolsConfigRepository),
            fontRepository = inject(materialFontRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
