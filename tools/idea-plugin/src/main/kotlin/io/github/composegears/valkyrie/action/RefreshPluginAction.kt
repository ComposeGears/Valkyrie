package io.github.composegears.valkyrie.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents

class RefreshPluginAction : AnAction() {

    init {
        templatePresentation.text = "Refresh Plugin"
        templatePresentation.icon = AllIcons.Actions.Refresh
        templatePresentation.description = "Refresh plugin to last used mode"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        project.globalEventsHandler.send(PluginEvents.RefreshPlugin)
    }
}
