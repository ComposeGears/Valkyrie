package io.github.composegears.valkyrie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize
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
                Compose17IJSizeBugWorkaround {
                    ValkyrieTheme(
                        project = project,
                        currentComponent = this,
                    ) {
                        CompositionLocalProvider(LocalSnackBar provides rememberSnackbarState()) {
                            ValkyriePlugin()
                        }
                    }
                }
            }
        }
    }

    /**
     * Workaround until the issue with Compose 1.7 + fillMax__ + IntelliJ Panels is fixed:
     * https://youtrack.jetbrains.com/issue/CMP-5856
     */
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun Compose17IJSizeBugWorkaround(content: @Composable () -> Unit) {
        with(LocalDensity.current) {
            Box(modifier = Modifier.requiredSize(LocalWindowInfo.current.containerSize.toSize().toDpSize())) {
                content()
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
