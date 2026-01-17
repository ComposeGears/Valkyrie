package io.github.composegears.valkyrie

import androidx.compose.runtime.CompositionLocalProvider
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
import org.jetbrains.jewel.bridge.addComposeTab

class ValkyrieToolWindow :
    ToolWindowFactory,
    DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        DI.initWith(project)

        toolWindow.setTitleActions(listOf(RefreshPluginAction()))
        toolWindow.addComposeTab(focusOnClickInside = true) {
            ValkyrieTheme(project = project) {
                CompositionLocalProvider(LocalSnackBar provides rememberSnackbarState()) {
                    ValkyriePlugin()
                }
            }
        }
    }

    companion object {
        const val ID = "Valkyrie"
    }
}
