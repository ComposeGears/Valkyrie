package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.ProvidableDependency
import com.intellij.openapi.project.Project

object DI {
    private val platformModule = intellijPlatformModule()

    val core = coreModule()

    fun initWith(project: Project) {
        (platformModule.project as ProvidableDependency<Project>).provides { project }
    }
}
