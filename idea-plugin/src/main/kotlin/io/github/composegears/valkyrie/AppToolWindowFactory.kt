package io.github.composegears.valkyrie

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import io.github.composegears.valkyrie.ui.ValkyriePlugin
import io.github.composegears.valkyrie.ui.di.Koin
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme

class AppToolWindowFactory :
  ToolWindowFactory,
  DumbAware {

  init {
    Koin.start()
  }

  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    System.setProperty("compose.swing.render.on.graphics", "true")
    System.setProperty("compose.interop.blending", "true")

    toolWindow.addComposePanel {
      ValkyrieTheme(
        project = project,
        currentComponent = this,
      ) {
        ValkyriePlugin()
      }
    }
  }
}

private fun ToolWindow.addComposePanel(
  displayName: String = "",
  isLockable: Boolean = true,
  content: @Composable ComposePanel.() -> Unit,
) = PluginWindow(content = content)
  .also { contentManager.addContent(contentManager.factory.createContent(it, displayName, isLockable)) }

private class PluginWindow(
  height: Int = 800,
  width: Int = 800,
  y: Int = 0,
  x: Int = 0,
  content: @Composable ComposePanel.() -> Unit,
) : BorderLayoutPanel() {

  init {
    add(
      ComposePanel().apply {
        setBounds(x = x, y = y, width = width, height = height)
        setContent {
          content()
        }
      },
    )
  }
}
