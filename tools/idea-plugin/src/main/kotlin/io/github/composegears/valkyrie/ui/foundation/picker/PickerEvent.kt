package io.github.composegears.valkyrie.ui.foundation.picker

import java.nio.file.Path

sealed interface PickerEvent {
    data class PickDirectory(val path: Path) : PickerEvent
    data class PickFiles(val paths: List<Path>) : PickerEvent
    data class ClipboardText(val text: String) : PickerEvent
}
