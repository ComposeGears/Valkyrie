package io.github.composegears.valkyrie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import io.github.composegears.valkyrie.theme.WidgetTheme
import io.github.composegears.valkyrie.ui.ValkyriePlugin

class AppToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        ApplicationManager.getApplication().invokeLater {
            toolWindow.contentManager.addContent(
                ContentFactory.getInstance().createContent(
                    ComposePanel().apply {
                        setBounds(0, 0, 800, 600)
                        setContent {
                            WidgetTheme(darkTheme = true) {
                                Surface(modifier = Modifier.fillMaxSize()) {
                                    ValkyriePlugin()
                                }
                            }
                        }
                    },
                    "Valkyrie SVG to ImageVector",
                    false
                )
            )
        }
    }
}