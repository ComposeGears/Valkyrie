package io.github.composegears.valkyrie.ui.components

import androidx.compose.runtime.*
import io.github.composegears.valkyrie.theme.LocalComponent
import java.awt.dnd.*
import java.io.File

class SimpleDropTargetListener(
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
fun rememberDragAndDropHandler(
    onDrop: (File) -> Unit
): DragAndDropHandlerState {
    var handlerState by remember { mutableStateOf(DragAndDropHandlerState()) }

    val localComponent = LocalComponent.current

    DisposableEffect(Unit) {
        val listener = SimpleDropTargetListener(
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
