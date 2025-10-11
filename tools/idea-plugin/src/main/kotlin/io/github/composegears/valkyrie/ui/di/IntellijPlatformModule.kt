package io.github.composegears.valkyrie.ui.di

import com.composegears.leviathan.LateInitDependency
import com.composegears.leviathan.Leviathan
import com.intellij.openapi.project.Project

fun intellijPlatformModule(): IntellijPlatformModule = IntellijPlatformModuleImpl

interface IntellijPlatformModule {
    val project: LateInitDependency<Project>
}

private object IntellijPlatformModuleImpl : Leviathan(), IntellijPlatformModule {

    override val project by lateInitInstance<Project>()
}
