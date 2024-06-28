package io.github.composegears.valkyrie.ui.foundation.dnd

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.LocalComponent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetListener
import java.io.File

@Composable
fun rememberDragAndDropFolderHandler(
    onDrop: (String) -> Unit
) = rememberDragAndDropHandler { file ->
    val destination = when {
        file.isDirectory -> file.path
        else -> file.parent
    }
    onDrop(destination)
}

@Composable
fun rememberDragAndDropHandler(
    onDrop: (File) -> Unit
): DragAndDropHandlerState {
    var handlerState by rememberMutableState { DragAndDropHandlerState() }

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

data class DragAndDropHandlerState(val isDragging: Boolean = false)

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
