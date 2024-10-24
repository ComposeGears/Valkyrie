package io.github.composegears.valkyrie.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.composegears.leviathan.Dependency
import com.composegears.leviathan.Leviathan
import com.composegears.leviathan.LeviathanDelicateApi
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.settings.InMemorySettings

@Composable
inline fun <reified T> Dependency<T>.leviathanInject() = remember { get() }

object DI {

    @OptIn(LeviathanDelicateApi::class)
    fun initLeviathan(project: Project) {
        PlatformModule.project.overrideWith { project }
    }
}

interface Platform {
    val project: Dependency<Project?>
}

private object PlatformModule : Leviathan(), Platform {
    override val project by instanceOf<Project?> { null }
}

interface Core {
    val inMemorySettings: Dependency<InMemorySettings>
}

object CoreModule : Leviathan(), Platform by PlatformModule, Core {
    override val inMemorySettings by instanceOf { InMemorySettings(project.get()!!) }
}
