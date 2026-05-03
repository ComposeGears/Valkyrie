package io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.MaterialSymbolsConfigUseCase

object MaterialSymbolsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val materialSymbolsConfigRepository by instanceOf {
        MaterialSymbolsConfigRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }
    private val materialFontRepository by instanceOf {
        MaterialFontRepository(httpClient = inject(NetworkModule.httpClient))
    }

    val materialSymbolsConfigUseCase by instanceOf {
        MaterialSymbolsConfigUseCase(
            configRepository = inject(materialSymbolsConfigRepository),
            fontRepository = inject(materialFontRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
