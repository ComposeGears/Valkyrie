@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.jewel.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text

val LocalProject = staticCompositionLocalOf<Project> { error("Not found Project in composition") }

val currentProject: Project?
    get() = ProjectManager.getInstance().openProjects.firstOrNull()

val currentProjectPath: String?
    get() = currentProject?.basePath

@Preview
@Composable
private fun ProjectPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column {
        CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Project:")
            InfoText(text = currentProject?.name.orEmpty())
        }
        Spacer(8.dp)
        CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Path:")
            InfoText(text = currentProjectPath.orEmpty())
        }
    }
}
