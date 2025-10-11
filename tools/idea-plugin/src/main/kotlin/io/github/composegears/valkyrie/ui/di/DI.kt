package io.github.composegears.valkyrie.ui.di

import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.sdk.core.extensions.cast

object DI {
    private val platformModule = intellijPlatformModule()

    val core = coreModule()

    fun initWith(project: Project) {
        platformModule.project.provides { project }
    }
}
