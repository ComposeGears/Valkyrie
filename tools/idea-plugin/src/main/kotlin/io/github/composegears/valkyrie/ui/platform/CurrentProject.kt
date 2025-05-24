package io.github.composegears.valkyrie.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.openapi.project.ProjectManager

@Composable
fun rememberCurrentProject(): CurrentProject {
    if (LocalInspectionMode.current) return NoOpCurrentProject

    return CurrentProjectImpl
}

interface CurrentProject {
    val path: String?
}

private object NoOpCurrentProject : CurrentProject {
    override val path: String? = null
}

private object CurrentProjectImpl : CurrentProject {

    override val path: String?
        get() = ProjectManager.getInstance().openProjects.firstOrNull()?.basePath
}
