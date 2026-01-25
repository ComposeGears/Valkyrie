package io.github.composegears.valkyrie.jewel.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.project.ProjectManager

@Composable
fun rememberCurrentProject() = remember { CurrentProject() }

class CurrentProject {

    val path: String?
        get() = ProjectManager.getInstance().openProjects.firstOrNull()?.basePath
}
