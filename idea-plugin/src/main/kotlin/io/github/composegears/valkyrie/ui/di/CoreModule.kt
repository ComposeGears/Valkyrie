package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.Dependency
import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.settings.InMemorySettings

fun coreModule(): CoreModule = CoreModuleImpl

interface CoreModule {
    val inMemorySettings: Dependency<InMemorySettings>
}

private object CoreModuleImpl : Leviathan(), CoreModule {

    private val platformModule = intellijPlatformModule()

    override val inMemorySettings by instanceOf { InMemorySettings(platformModule.project.get()) }
}
