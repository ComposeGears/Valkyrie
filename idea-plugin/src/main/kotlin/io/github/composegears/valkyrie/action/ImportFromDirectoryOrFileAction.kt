package io.github.composegears.valkyrie.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import io.github.composegears.valkyrie.ValkyrieToolWindow
import io.github.composegears.valkyrie.action.dialog.RequiredIconPackModeDialog
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.ImportIcons
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.SetupIconPackMode
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings
import io.github.composegears.valkyrie.util.extension.isSvg
import io.github.composegears.valkyrie.util.extension.isXml

class ImportFromDirectoryOrFileAction : AnAction() {

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        val toolWindow = ToolWindowManager
            .getInstance(project)
            .getToolWindow(ValkyrieToolWindow.ID) ?: return

        val paths = event
            .getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.filterNotNull()
            ?.map(VirtualFile::toNioPath) ?: return

        val settings = project.persistentSettings.state
        val eventsHandler = project.globalEventsHandler

        if (settings.isIconPackRequired) {
            RequiredIconPackModeDialog(
                message = "This action requires an IconPack mode to be setup",
                onContinue = {
                    toolWindow.show()
                    eventsHandler.send(SetupIconPackMode(pathData = PendingPathData(paths)))
                },
            ).showAndGet()
        } else {
            toolWindow.show()
            eventsHandler.send(ImportIcons(pathData = PendingPathData(paths)))
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
                    file.isDirectory -> file.children.any { it.isSvg || it.isXml }
                    else -> file.isSvg || file.isXml
                }
            } else {
                event.presentation.text = "Import All"
                event.presentation.isEnabled = files.all { it.isSvg || it.isXml }
            }
        }
    }
}
