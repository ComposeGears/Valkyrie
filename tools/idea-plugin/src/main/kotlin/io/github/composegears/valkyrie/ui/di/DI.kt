package io.github.composegears.valkyrie.ui.di

import com.intellij.openapi.project.Project

object DI {
    private val platformModule = intellijPlatformModule()

    val core = coreModule()

    fun initWith(project: Project) {
        platformModule.project.provides { project }
    }
}
