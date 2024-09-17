package io.github.composegears.valkyrie.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import io.github.composegears.valkyrie.parser.svgxml.PackageExtractor
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
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabled = file?.isDirectory == true && event.project != null
    }
}
