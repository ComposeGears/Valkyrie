package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.nio.file.Path
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GlobalEventsHandler {

    private val _events = MutableSharedFlow<PluginEvents>(replay = 1)
    val events = _events.asSharedFlow()

    fun send(event: PluginEvents) {
        _events.tryEmit(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun resetCache() {
        _events.resetReplayCache()
    }

    sealed interface PluginEvents {
        data class ImportIcons(val pathData: PendingPathData) : PluginEvents
        data class SetupIconPackMode(val pathData: PendingPathData) : PluginEvents
    }

    data class PendingPathData(val paths: List<Path> = emptyList())

    companion object {
        @JvmStatic
        val Project.globalEventsHandler: GlobalEventsHandler
            get() = service<GlobalEventsHandler>()
    }
}
