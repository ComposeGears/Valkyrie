package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.composegears.valkyrie.ui.foundation.theme.LocalComponent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetListener
import java.io.File

class SimpleFolderDropTargetListener(
    val onDragEnter: () -> Unit = {},
    val onDragExit: () -> Unit = {},
    val onDrop: (String) -> Unit
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

        when {
            file.isDirectory -> onDrop(file.path)
            else -> onDrop(file.parent)
        }

        event.dropComplete(true)
    }
}

@Composable
fun rememberFolderDragAndDropHandler(
    onDrop: (String) -> Unit
): DragAndDropHandlerState {
    var handlerState by remember { mutableStateOf(DragAndDropHandlerState()) }

    val localComponent = LocalComponent.current

    DisposableEffect(Unit) {
        val listener = SimpleFolderDropTargetListener(
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
