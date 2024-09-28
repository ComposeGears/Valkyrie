package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.nio.file.Path
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GlobalEventsHandler {

    private val _events = MutableSharedFlow<PluginEvents>(replay = 1)
    val events = _events.asSharedFlow()

    fun send(event: PluginEvents) {
        _events.tryEmit(event)
    }

    sealed interface PluginEvents {
        data class ImportIcons(val paths: List<Path>) : PluginEvents
    }

    companion object {
        @JvmStatic
        val Project.globalEventsHandler: GlobalEventsHandler
            get() = service<GlobalEventsHandler>()
    }
}