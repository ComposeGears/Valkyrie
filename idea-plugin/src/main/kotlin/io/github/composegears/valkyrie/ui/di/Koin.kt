package io.github.composegears.valkyrie.ui.di

import com.composegears.tiamat.koin.tiamatViewModelOf
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionViewModel
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.viewmodel.ExistingPackViewModel
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel.NewPackViewModel
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionViewModel
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupViewModel
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object Koin {

    fun start(project: Project) {
        startKoin {
            modules(
                appModule,
                provideProjectModule(project),
            )
        }
    }
}

private val appModule = module {
    tiamatViewModelOf(::IconPackConversionViewModel)

    tiamatViewModelOf(::SimpleModeSetupViewModel)
    tiamatViewModelOf(::SimpleConversionViewModel)

    tiamatViewModelOf(::ExistingPackViewModel)
    tiamatViewModelOf(::NewPackViewModel)

    tiamatViewModelOf(::SettingsViewModel)

    singleOf(::InMemorySettings)
}

private fun provideProjectModule(project: Project) = module {
    single { project }
}
