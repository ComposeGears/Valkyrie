package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.mutableOf
import com.intellij.openapi.project.Project

fun intellijPlatformModule() = IntellijPlatformModule

object IntellijPlatformModule : Leviathan {
    val project by mutableOf<Project> { error("Project is not initialized") }
}
