package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.di.coreModule
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data.OcticonsMetadataParser
import io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data.OcticonsRepository
import io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.domain.OcticonsUseCase

object OcticonsModule : Leviathan() {
    private val network = NetworkModule
    private val core = coreModule()

    private val octiconsMetadataParser by factoryOf { OcticonsMetadataParser(json = inject(network.json)) }

    private val octiconsRepository by instanceOf {
        OcticonsRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
            metadataParser = inject(octiconsMetadataParser),
        )
    }

    val octiconsUseCase by instanceOf {
        OcticonsUseCase(
            repository = inject(octiconsRepository),
            inMemorySettings = inject(core.inMemorySettings),
        )
    }
}
