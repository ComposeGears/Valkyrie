package io.github.composegears.valkyrie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import io.github.composegears.valkyrie.action.RefreshPluginAction
import io.github.composegears.valkyrie.jewel.banner.LocalGlobalBannerState
import io.github.composegears.valkyrie.jewel.banner.rememberBannerState
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.ui.ValkyriePlugin
import io.github.composegears.valkyrie.ui.di.DI
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.bridge.addComposeTab

class ValkyrieToolWindow :
    ToolWindowFactory,
    DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        System.setProperty("compose.interop.blending", "true")

        DI.initWith(project)

        toolWindow.setTitleActions(listOf(RefreshPluginAction()))
        toolWindow.addComposeTab(focusOnClickInside = false) {
            Valkyrie(project)
        }
    }

    companion object {
        const val ID = "Valkyrie"
    }
}

@Composable
private fun Valkyrie(project: Project) {
    CompositionLocalProvider(
        LocalGlobalBannerState provides rememberBannerState(),
        LocalProject provides project,
    ) {
        ValkyriePlugin()
    }
}

@Preview
@Composable
private fun ValkyriePluginPreview() = ProjectPreviewTheme {
    DI.initWith(it)

    Valkyrie(it)
}
