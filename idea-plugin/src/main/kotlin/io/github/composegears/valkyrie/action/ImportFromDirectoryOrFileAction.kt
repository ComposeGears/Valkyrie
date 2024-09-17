package io.github.composegears.valkyrie.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import io.github.composegears.valkyrie.ValkyrieToolWindow
import io.github.composegears.valkyrie.action.dialog.RequiredIconPackModeDialog
import io.github.composegears.valkyrie.parser.svgxml.util.isSvg
import io.github.composegears.valkyrie.parser.svgxml.util.isXml
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.ImportIcons
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode

class ImportFromDirectoryOrFileAction : AnAction() {

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val toolWindow = ToolWindowManager
            .getInstance(project)
            .getToolWindow(ValkyrieToolWindow.ID) ?: return

        val files = event
            .getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.filterNotNull() ?: return

        val settings = project.persistentSettings.state
        val eventsHandler = project.globalEventsHandler

        if (settings.mode == Mode.Unspecified.name || settings.mode == Mode.Simple.name) {
            RequiredIconPackModeDialog(onOk = toolWindow::show).showAndGet()
        } else {
            toolWindow.show()
            eventsHandler.send(ImportIcons(paths = files.map(VirtualFile::toNioPath)))
        }
    }

    override fun update(event: AnActionEvent) {
        val files = event
            .getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.filterNotNull()

        if (files.isNullOrEmpty() || event.project == null) {
            event.presentation.isVisible = false
        } else {
            if (files.size == 1) {
                val file = files.first()

                when {
                    file.isDirectory -> event.presentation.text = "Import All"
                    else -> event.presentation.text = "Import Icon"
                }
                event.presentation.isEnabled = when {
                    file.isDirectory -> file.children.any { it.extension.isXml || it.extension.isSvg }
                    else -> file.extension.isXml || file.extension.isSvg
                }
            } else {
                event.presentation.text = "Import All"
                event.presentation.isEnabled = files.all { it.extension.isXml || it.extension.isSvg }
            }
        }
    }
}
