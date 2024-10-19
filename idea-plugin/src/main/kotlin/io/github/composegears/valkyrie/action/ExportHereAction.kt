package io.github.composegears.valkyrie.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.ToolWindowManager
import io.github.composegears.valkyrie.ValkyrieToolWindow
import io.github.composegears.valkyrie.action.dialog.RequiredIconPackModeDialog
import io.github.composegears.valkyrie.parser.svgxml.PackageExtractor
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.ImportIcons
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.SetupIconPackMode
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings

class ExportHereAction : AnAction() {

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = event.project ?: return
        if (file.isDirectory) {
            val predictedPackage = PackageExtractor.getFrom(path = file.path)

            project.persistentSettings.stateWithNotify {
                if (predictedPackage != null) {
                    packageName = predictedPackage
                }
                iconPackDestination = file.path
            }
        }

        val toolWindow = ToolWindowManager
            .getInstance(project)
            .getToolWindow(ValkyrieToolWindow.ID) ?: return

        val settings = project.persistentSettings.state
        val eventsHandler = project.globalEventsHandler

        if (settings.isIconPackRequired) {
            RequiredIconPackModeDialog(
                message = "Export folder updated.\nYou can setup IconPack mode now or later to start processing icons",
                onContinue = {
                    toolWindow.show()
                    eventsHandler.send(SetupIconPackMode(pathData = PendingPathData()))
                },
            ).showAndGet()
        } else {
            toolWindow.show()
            eventsHandler.send(ImportIcons(pathData = PendingPathData()))
        }
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabled = file?.isDirectory == true && event.project != null
    }
}
