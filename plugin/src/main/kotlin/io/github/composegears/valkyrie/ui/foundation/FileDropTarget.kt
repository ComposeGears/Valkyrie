package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.github.composegears.valkyrie.ui.foundation.theme.LocalComponent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetListener
import java.io.File

class SimpleFileDropTargetListener(
    val onDragEnter: () -> Unit = {},
    val onDragExit: () -> Unit = {},
    val onDrop: (File) -> Unit = {}
) : DropTargetListener {
    override fun dragEnter(dtde: DropTargetDragEvent?) = onDragEnter()

    override fun dragOver(dtde: DropTargetDragEvent?) = Unit

    override fun dropActionChanged(dtde: DropTargetDragEvent?) = Unit

    override fun dragExit(dte: DropTargetEvent?) = onDragExit()

    override fun drop(event: DropTargetDropEvent) {
        event.acceptDrop(DnDConstants.ACTION_COPY)
        val transferable = event.transferable

        val file = transferable.transferDataFlavors
            .filter { it.isFlavorJavaFileListType }
            .mapNotNull { transferable.getTransferData(it) as? List<*> }
            .flatten()
            .firstNotNullOf { it as? File }

        onDrop(file)
        event.dropComplete(true)
    }
}

data class DragAndDropHandlerState(
    val isDragging: Boolean = false
)

@Composable
fun rememberFileDragAndDropHandler(
    onDrop: (File) -> Unit
): DragAndDropHandlerState {
    var handlerState by rememberMutableState { DragAndDropHandlerState() }

    val localComponent = LocalComponent.current

    DisposableEffect(Unit) {
        val listener = SimpleFileDropTargetListener(
            onDrop = onDrop,
            onDragEnter = { handlerState = handlerState.copy(isDragging = true) },
            onDragExit = { handlerState = handlerState.copy(isDragging = false) })
        val dropTarget = DropTarget(localComponent, listener)

        onDispose {
            dropTarget.removeDropTargetListener(listener)
        }
    }

    return handlerState
}
