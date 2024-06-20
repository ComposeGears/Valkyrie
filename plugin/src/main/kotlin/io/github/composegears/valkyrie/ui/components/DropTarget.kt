package io.github.composegears.valkyrie.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import io.github.composegears.valkyrie.theme.LocalComponent
import java.awt.dnd.*
import java.io.File

class SimpleDropTargetListener(
    val onDrop: (File) -> Unit = {}
) : DropTargetListener {
    override fun dragEnter(dtde: DropTargetDragEvent?) = Unit

    override fun dragOver(dtde: DropTargetDragEvent?) = Unit

    override fun dropActionChanged(dtde: DropTargetDragEvent?) = Unit

    override fun dragExit(dte: DropTargetEvent?) = Unit

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

@Composable
fun DragAndDropHandler(onDrop: (File) -> Unit) {
    val localComponent = LocalComponent.current

    DisposableEffect(Unit) {
        val listener = SimpleDropTargetListener(onDrop = onDrop)
        val dropTarget = DropTarget(localComponent, listener)

        onDispose {
            dropTarget.removeDropTargetListener(listener)
        }
    }
}

