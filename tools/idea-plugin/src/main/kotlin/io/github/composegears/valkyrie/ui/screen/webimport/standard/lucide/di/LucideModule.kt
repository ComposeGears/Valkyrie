package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.screen.webimport.common.di.NetworkModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data.LucideRepository
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.domain.LucideUseCase

object LucideModule : Leviathan() {
    private val network = NetworkModule

    private val lucideRepository by instanceOf {
        LucideRepository(
            httpClient = inject(network.httpClient),
            json = inject(network.json),
        )
    }

    val lucideUseCase by instanceOf {
        LucideUseCase(
            repository = inject(lucideRepository),
        )
    }
}
