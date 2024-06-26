package io.github.composegears.valkyrie.ui.di

import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionViewModel
import io.github.composegears.valkyrie.ui.screen.intro.IntroViewModel
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
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
    factoryOf(::IntroViewModel)
    factoryOf(::ConversionViewModel)
    factoryOf(::SettingsViewModel)

    singleOf(::InMemorySettings)
}
