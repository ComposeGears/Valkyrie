package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.Dependency
import com.composegears.leviathan.Leviathan
import com.intellij.openapi.project.Project

fun intellijPlatformModule(): IntellijPlatformModule = IntellijPlatformModuleImpl

interface IntellijPlatformModule {
    val project: Dependency<Project>
}

private object IntellijPlatformModuleImpl : Leviathan(), IntellijPlatformModule {

    override val project by providableOf<Project> { error("Not initialized") }
}
