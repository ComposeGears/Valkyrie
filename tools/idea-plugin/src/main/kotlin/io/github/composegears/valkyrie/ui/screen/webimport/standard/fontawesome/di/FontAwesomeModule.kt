package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data.FontAwesomeCategoriesYamlParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data.FontAwesomeIconsYamlParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data.FontAwesomeRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.domain.FontAwesomeUseCase

object FontAwesomeModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()
    private val fontAwesomeIconsYamlParser by factoryOf { FontAwesomeIconsYamlParser() }
    private val fontAwesomeCategoriesYamlParser by factoryOf { FontAwesomeCategoriesYamlParser() }

    private val fontAwesomeRepository by instanceOf {
        FontAwesomeRepository(
            httpClient = inject(network.httpClient),
            iconsYamlParser = inject(fontAwesomeIconsYamlParser),
            categoriesYamlParser = inject(fontAwesomeCategoriesYamlParser),
        )
    }

    val fontAwesomeUseCase by instanceOf {
        FontAwesomeUseCase(
            repository = inject(fontAwesomeRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
