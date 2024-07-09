package io.github.composegears.valkyrie.ui.di

import com.composegears.tiamat.koin.tiamatViewModelOf
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionViewModel
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationViewModel
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination.IconPackDestinationViewModel
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionViewModel
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupViewModel
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object Koin {

    fun start() {
        startKoin {
            modules(appModule)
        }
    }
}

private val appModule = module {
    tiamatViewModelOf(::IconPackDestinationViewModel)
    tiamatViewModelOf(::IconPackCreationViewModel)
    tiamatViewModelOf(::IconPackConversionViewModel)

    tiamatViewModelOf(::SimpleModeSetupViewModel)
    tiamatViewModelOf(::SimpleConversionViewModel)

    tiamatViewModelOf(::SettingsViewModel)

    singleOf(::InMemorySettings)
}
