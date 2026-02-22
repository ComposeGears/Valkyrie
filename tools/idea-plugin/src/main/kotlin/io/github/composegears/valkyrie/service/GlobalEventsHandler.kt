package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.jewel.tooling.GlobalPreviewState
import java.nio.file.Path
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class GlobalEventsHandler {

    private val _events = Channel<PluginEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun send(event: PluginEvents) {
        _events.trySend(event)
    }

    sealed interface PluginEvents {
        data object RefreshPlugin : PluginEvents
        data class ImportIcons(val pathData: PendingPathData) : PluginEvents
        data class SetupIconPackMode(val pathData: PendingPathData) : PluginEvents
    }

    data class PendingPathData(val paths: List<Path> = emptyList())

    companion object {
        @JvmStatic
        val Project.globalEventsHandler: GlobalEventsHandler
            get() {
                return if (GlobalPreviewState.isPreview) {
                    GlobalEventsHandler()
                } else {
                    service<GlobalEventsHandler>()
                }
            }
    }
}
