@file:Suppress("ktlint:standard:filename")

package io.github.composegears.valkyrie.jewel.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.composegears.valkyrie.jewel.platform.DragAndDropHandlerState.Companion.dragging
import io.github.composegears.valkyrie.jewel.platform.DragAndDropHandlerState.Companion.notDragging
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetListener
import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.LocalComponent

@Composable
fun rememberDragAndDropFolderHandler(onDrop: (Path) -> Unit): DragAndDropHandlerState {
    return rememberFileDragAndDropHandler { path ->
        val destination = when {
            path.isDirectory() -> path
            else -> path.parent
        }
        onDrop(destination)
    }
}

@Composable
fun rememberFileDragAndDropHandler(onDrop: (Path) -> Unit): DragAndDropHandlerState {
    return rememberMultiSelectDragAndDropHandler { fileList ->
        if (fileList.isNotEmpty()) {
            onDrop(fileList.first())
        }
    }
}

@OptIn(ExperimentalJewelApi::class)
@Composable
fun rememberMultiSelectDragAndDropHandler(onDrop: (List<Path>) -> Unit): DragAndDropHandlerState {
    val component = LocalComponent.current
    val latestOnDrop by rememberUpdatedState(onDrop)

    var state by rememberMutableState { DragAndDropHandlerState() }

    DisposableEffect(Unit) {
        val listener = SimpleDropTargetListener(
            onDrop = latestOnDrop,
            onDragEnter = { state = state.dragging() },
            onDragExit = { state = state.notDragging() },
        )
        val dropTarget = DropTarget(component, listener)

        onDispose {
            dropTarget.removeDropTargetListener(listener)
        }
    }

    return state
}

data class DragAndDropHandlerState(val isDragging: Boolean = false) {

    companion object {
        fun DragAndDropHandlerState.dragging() = copy(isDragging = true)
        fun DragAndDropHandlerState.notDragging() = copy(isDragging = false)
    }
}

private class SimpleDropTargetListener(
    val onDragEnter: () -> Unit = {},
    val onDragExit: () -> Unit = {},
    val onDrop: (List<Path>) -> Unit = {},
) : DropTargetListener {
    override fun dragEnter(dtde: DropTargetDragEvent?) = onDragEnter()

    override fun dragOver(dtde: DropTargetDragEvent?) = Unit

    override fun dropActionChanged(dtde: DropTargetDragEvent?) = Unit

    override fun dragExit(dte: DropTargetEvent?) = onDragExit()

    override fun drop(event: DropTargetDropEvent) {
        event.acceptDrop(DnDConstants.ACTION_COPY)
        val transferable = event.transferable

        val paths = transferable.transferDataFlavors
            .asSequence()
            .filter { it.isFlavorJavaFileListType }
            .mapNotNull { transferable.getTransferData(it).safeAs<List<*>>() }
            .flatten()
            .filterIsInstance<File>()
            .map(File::toPath)
            .toList()

        onDrop(paths)
        event.dropComplete(true)
        onDragExit()
    }
}
