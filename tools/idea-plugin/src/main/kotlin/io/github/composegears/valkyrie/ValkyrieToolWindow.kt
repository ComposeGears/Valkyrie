package io.github.composegears.valkyrie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import io.github.composegears.valkyrie.action.RefreshPluginAction
import io.github.composegears.valkyrie.ui.ValkyriePlugin
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.foundation.LocalSnackBar
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbarState
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme
import io.github.composegears.valkyrie.ui.platform.buildComposePanel

class ValkyrieToolWindow :
    ToolWindowFactory,
    DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        System.setProperty("compose.swing.render.on.graphics", "true")

        DI.initWith(project)

        toolWindow.apply {
            setTitleActions(listOf(RefreshPluginAction()))
            addComposePanel {
                ValkyrieTheme(project = project, currentComponent = this) {
                    CompositionLocalProvider(LocalSnackBar provides rememberSnackbarState()) {
                        ValkyriePlugin()
                    }
                }
            }
        }
    }

    companion object {
        const val ID = "Valkyrie"
    }
}

private fun ToolWindow.addComposePanel(
    displayName: String = "",
    isLockable: Boolean = true,
    content: @Composable ComposePanel.() -> Unit,
) = buildComposePanel(content = content)
    .also { contentManager.addContent(contentManager.factory.createContent(it, displayName, isLockable)) }
