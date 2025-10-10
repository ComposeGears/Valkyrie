package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.ProvidableDependency
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.sdk.core.extensions.cast

object DI {
    private val platformModule = intellijPlatformModule()

    val core = coreModule()

    fun initWith(project: Project) {
        (platformModule.project.cast<ProvidableDependency<Project>>()).provides { project }
    }
}
