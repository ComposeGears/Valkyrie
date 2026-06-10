package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.composegears.valkyrie.jewel.MoreHorizontalAction
import io.github.composegears.valkyrie.jewel.button.TooltipIconButton
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.core.tree.TreeNode
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.core.tree.contains
import io.github.composegears.valkyrie.sdk.core.tree.copy
import io.github.composegears.valkyrie.sdk.core.tree.find
import java.awt.Cursor
import kotlin.math.abs
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

enum class DropPosition { Before, After, Inside }

internal enum class DropIndicatorStyle { Sibling, NestedAppend }

private val DragPointerIcon = PointerIcon(Cursor(Cursor.MOVE_CURSOR))

internal data class NodeDropLayout(
    val rowLeft: Float,
    val rowTop: Float,
    val rowCenterY: Float,
    val subtreeBottom: Float,
)

internal data class ResolvedDropTarget<T>(
    val target: T,
    val position: DropPosition,
    val isInvalid: Boolean,
    val indicatorStyle: DropIndicatorStyle = DropIndicatorStyle.Sibling,
)

@Stable
class TreeHistoryState<T>(
    initialTree: TreeNode<T>,
    private val maxHistorySize: Int = 100,
) {
    var tree: TreeNode<T> by mutableStateOf(initialTree)
        private set

    private val undoStack = ArrayDeque<TreeNode<T>>()
    private val redoStack = ArrayDeque<TreeNode<T>>()

    val canUndo: Boolean
        get() = undoStack.isNotEmpty()

    val canRedo: Boolean
        get() = redoStack.isNotEmpty()

    fun move(from: T, to: T, position: DropPosition) {
        update { currentTree ->
            currentTree.moveNode(
                from = from,
                to = to,
                position = position,
            )
        }
    }

    fun update(transform: (TreeNode<T>) -> TreeNode<T>) {
        val updatedTree = transform(tree)
        if (updatedTree == tree) return

        undoStack.addLast(tree)
        while (undoStack.size > maxHistorySize) {
            undoStack.removeFirst()
        }
        redoStack.clear()
        tree = updatedTree
    }

    fun undo() {
        val previousTree = undoStack.removeLastOrNull() ?: return
        redoStack.addLast(tree)
        tree = previousTree
    }

    fun redo() {
        val nextTree = redoStack.removeLastOrNull() ?: return
        undoStack.addLast(tree)
        tree = nextTree
    }

    fun reset(newTree: TreeNode<T>) {
        tree = newTree
        undoStack.clear()
        redoStack.clear()
    }
}

@Composable
fun <T> rememberTreeHistoryState(
    initialTree: TreeNode<T>,
    maxHistorySize: Int = 100,
): TreeHistoryState<T> = remember(initialTree, maxHistorySize) {
    TreeHistoryState(
        initialTree = initialTree,
        maxHistorySize = maxHistorySize,
    )
}

@Stable
private class DragState<T> {
    var dragging: T? by mutableStateOf(null)
    var dropTarget: T? by mutableStateOf(null)
    var dropPosition: DropPosition by mutableStateOf(DropPosition.Before)
    var isDropInvalid: Boolean by mutableStateOf(false)
    var dropIndicatorStyle: DropIndicatorStyle by mutableStateOf(DropIndicatorStyle.Sibling)
    var dragPosition: Offset? by mutableStateOf(null)
    var grabOffset: Offset = Offset.Zero
    val nodeLayouts = mutableStateMapOf<T, NodeDropLayout>()
    val contentWidths = mutableStateMapOf<T, Float>()

    fun updateNodeRowLayout(node: T, coords: LayoutCoordinates) {
        val bounds = coords.boundsInRoot()
        val current = nodeLayouts[node]
        nodeLayouts[node] = NodeDropLayout(
            rowLeft = bounds.left,
            rowTop = bounds.top,
            rowCenterY = bounds.center.y,
            subtreeBottom = current?.subtreeBottom ?: bounds.bottom,
        )
    }

    fun updateNodeSubtreeLayout(node: T, coords: LayoutCoordinates) {
        val bounds = coords.boundsInRoot()
        val current = nodeLayouts[node]
        nodeLayouts[node] = NodeDropLayout(
            rowLeft = current?.rowLeft ?: bounds.left,
            rowTop = current?.rowTop ?: bounds.top,
            rowCenterY = current?.rowCenterY ?: bounds.center.y,
            subtreeBottom = bounds.bottom,
        )
    }

    fun removeNode(node: T) {
        nodeLayouts.remove(node)
        contentWidths.remove(node)
        if (dropTarget == node) {
            dropTarget = null
            dropPosition = DropPosition.Before
            isDropInvalid = false
            dropIndicatorStyle = DropIndicatorStyle.Sibling
        }
        if (dragging == node) {
            reset()
        }
    }

    fun updateDrop(
        rootPosition: Offset,
        indentPx: Float,
        insideHalfZonePx: Float,
        isValidTarget: (T) -> Boolean = { true },
    ) {
        val resolved = resolveDropTarget(
            rootPosition = rootPosition,
            indentPx = indentPx,
            insideHalfZonePx = insideHalfZonePx,
            dragging = dragging,
            layouts = nodeLayouts,
            isValidTarget = isValidTarget,
        )

        if (resolved == null) {
            dropTarget = null
            dropPosition = DropPosition.Before
            isDropInvalid = false
            dropIndicatorStyle = DropIndicatorStyle.Sibling
            return
        }

        dropTarget = resolved.target
        dropPosition = resolved.position
        isDropInvalid = resolved.isInvalid
        dropIndicatorStyle = resolved.indicatorStyle
    }

    fun commit(onMove: (T, T, DropPosition) -> Unit) {
        val from = dragging
        val to = dropTarget
        if (from != null && to != null && from != to && !isDropInvalid) {
            onMove(from, to, dropPosition)
        }
        reset()
    }

    fun reset() {
        dragging = null
        dropTarget = null
        dropPosition = DropPosition.Before
        isDropInvalid = false
        dropIndicatorStyle = DropIndicatorStyle.Sibling
        dragPosition = null
        grabOffset = Offset.Zero
    }
}

internal fun <T> resolveDropTarget(
    rootPosition: Offset,
    indentPx: Float,
    insideHalfZonePx: Float,
    dragging: T?,
    layouts: Map<T, NodeDropLayout>,
    isValidTarget: (T) -> Boolean = { true },
): ResolvedDropTarget<T>? {
    return layouts.entries
        .asSequence()
        .filter { it.key != dragging }
        .map { (target, layout) ->
            val rootY = rootPosition.y
            val position = when {
                abs(rootY - layout.rowCenterY) <= insideHalfZonePx -> DropPosition.Inside
                rootY < layout.rowCenterY -> DropPosition.Before
                else -> DropPosition.After
            }
            val anchorX = when (position) {
                DropPosition.Before,
                DropPosition.After,
                -> layout.rowLeft + indentPx / 2f
                DropPosition.Inside -> layout.rowLeft + indentPx
            }
            val anchorY = when (position) {
                DropPosition.Before -> layout.rowTop
                DropPosition.Inside -> layout.rowCenterY
                DropPosition.After -> layout.subtreeBottom
            }
            Triple(
                ResolvedDropTarget(
                    target = target,
                    position = position,
                    isInvalid = !isValidTarget(target),
                    indicatorStyle = when {
                        position == DropPosition.After && layout.rowLeft > 0f -> DropIndicatorStyle.NestedAppend
                        else -> DropIndicatorStyle.Sibling
                    },
                ),
                abs(rootPosition.x - anchorX) + abs(rootY - anchorY),
                layout.subtreeBottom,
            )
        }
        .minWithOrNull(compareBy<Triple<ResolvedDropTarget<T>, Float, Float>> { it.second }.thenByDescending { it.third })
        ?.first
}

@Suppress("ktlint:compose:content-slot-reused")
@Composable
fun <T> TreeView(
    root: TreeNode<T>,
    modifier: Modifier = Modifier,
    indent: Dp = 24.dp,
    lineColor: Color = JewelTheme.globalColors.borders.normal,
    lineStrokeWidth: Dp = 1.dp,
    onMove: ((from: T, to: T, position: DropPosition) -> Unit)? = null,
    content: @Composable (data: T, isDragging: Boolean) -> Unit,
) {
    val dragState = if (onMove != null) remember { DragState<T>() } else null
    val descendantsByNode = remember(root) { root.descendantsByNode() }

    Column(modifier = modifier.width(IntrinsicSize.Max)) {
        content(root.data, dragState?.dragging != null)
        TreeNodes(
            nodes = root.children,
            descendantsByNode = descendantsByNode,
            indent = indent,
            lineColor = lineColor,
            lineStrokeWidth = lineStrokeWidth,
            dragState = dragState,
            onMove = onMove,
            content = content,
        )
    }

    val draggingData = dragState?.dragging
    val dragPos = dragState?.dragPosition
    if (draggingData != null && dragPos != null) {
        val grabOffset = dragState.grabOffset
        val positionProvider = remember(dragPos) {
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: androidx.compose.ui.unit.IntRect,
                    windowSize: androidx.compose.ui.unit.IntSize,
                    layoutDirection: androidx.compose.ui.unit.LayoutDirection,
                    popupContentSize: androidx.compose.ui.unit.IntSize,
                ): IntOffset = IntOffset(
                    x = (dragPos.x - grabOffset.x).roundToInt(),
                    y = (dragPos.y - grabOffset.y).roundToInt(),
                )
            }
        }
        Popup(
            popupPositionProvider = positionProvider,
            properties = PopupProperties(focusable = false),
        ) {
            Box(
                modifier = Modifier
                    .pointerHoverIcon(DragPointerIcon)
                    .background(
                        color = JewelTheme.globalColors.panelBackground,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .border(
                        width = 1.dp,
                        color = JewelTheme.globalColors.borders.normal,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .alpha(0.92f),
            ) {
                content(draggingData, true)
            }
        }
    }
}

@Composable
private fun <T> TreeNodes(
    nodes: List<TreeNode<T>>,
    descendantsByNode: Map<T, Set<T>>,
    indent: Dp,
    lineColor: Color,
    lineStrokeWidth: Dp,
    dragState: DragState<T>?,
    onMove: ((T, T, DropPosition) -> Unit)?,
    content: @Composable (T, Boolean) -> Unit,
) {
    Column {
        nodes.forEachIndexed { index, node ->
            TreeNodeRow(
                node = node,
                isLast = index == nodes.lastIndex,
                descendantsByNode = descendantsByNode,
                indent = indent,
                lineColor = lineColor,
                lineStrokeWidth = lineStrokeWidth,
                dragState = dragState,
                onMove = onMove,
                content = content,
            )
        }
    }
}

@Composable
private fun <T> TreeNodeRow(
    node: TreeNode<T>,
    isLast: Boolean,
    descendantsByNode: Map<T, Set<T>>,
    indent: Dp,
    lineColor: Color,
    lineStrokeWidth: Dp,
    dragState: DragState<T>?,
    onMove: ((T, T, DropPosition) -> Unit)?,
    content: @Composable (T, Boolean) -> Unit,
) {
    val isDraggingThis = dragState?.dragging == node.data
    val isDropTarget = dragState?.dropTarget == node.data
    val isInvalidDropTarget = dragState != null && isDropTarget && dragState.isDropInvalid
    val isAnyDragging = dragState?.dragging != null
    val draggablePointerIcon = when {
        isDraggingThis -> DragPointerIcon
        isAnyDragging -> PointerIcon.Default
        else -> PointerIcon.Hand
    }
    val insideColor = JewelTheme.globalColors.outlines.focused
    val invalidDropColor = JewelTheme.globalColors.outlines.error
    var outerCoords by remember(node.data) { mutableStateOf<LayoutCoordinates?>(null) }

    DisposableEffect(dragState, node.data) {
        onDispose {
            dragState?.removeNode(node.data)
        }
    }

    Column(
        modifier = Modifier
            .onGloballyPositioned { coords ->
                outerCoords = coords
                dragState?.updateNodeSubtreeLayout(node.data, coords)
            }
            .let { mod ->
                if (dragState != null && onMove != null) {
                    mod
                        .pointerHoverIcon(draggablePointerIcon)
                        .pointerInput(node.data) {
                            detectDragGestures(
                                onDragStart = { startOffset ->
                                    dragState.dragging = node.data
                                    dragState.grabOffset = startOffset
                                    dragState.dragPosition = outerCoords?.positionInRoot()?.plus(startOffset)
                                },
                                onDrag = { change, _ ->
                                    val rootPos = outerCoords?.positionInRoot()?.plus(change.position)
                                    dragState.dragPosition = rootPos
                                    rootPos?.let {
                                        val invalidTargets = descendantsByNode[dragState.dragging].orEmpty()
                                        dragState.updateDrop(
                                            rootPosition = it,
                                            indentPx = indent.toPx(),
                                            insideHalfZonePx = 9.dp.toPx(),
                                            isValidTarget = { candidate -> candidate !in invalidTargets },
                                        )
                                    }
                                },
                                onDragEnd = { dragState.commit(onMove) },
                                onDragCancel = { dragState.reset() },
                            )
                        }
                } else {
                    mod
                }
            },
    ) {
        if (isDropTarget && dragState.dropPosition == DropPosition.Before) {
            DropIndicator(
                color = if (isInvalidDropTarget) invalidDropColor else lineColor,
                indent = indent,
                isInvalid = isInvalidDropTarget,
                style = DropIndicatorStyle.Sibling,
            )
        }

        val isInsideTarget = isDropTarget && dragState.dropPosition == DropPosition.Inside
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .onGloballyPositioned { dragState?.updateNodeRowLayout(node.data, it) }
                .drawWithContent {
                    drawContent()
                    if (isInsideTarget) {
                        val targetColor = if (isInvalidDropTarget) invalidDropColor else insideColor
                        val accentW = 3.dp.toPx()
                        val radius = 4.dp.toPx()
                        val indentPx = indent.toPx()
                        val lineXPx = indentPx / 2
                        val innerWidth = dragState.contentWidths[node.data]
                            ?: (size.width - indentPx)
                        val contentRight = indentPx + innerWidth + 8.dp.toPx()
                        drawRoundRect(
                            color = targetColor.copy(alpha = if (isInvalidDropTarget) 0.14f else 0.10f),
                            topLeft = Offset(indentPx, 0f),
                            size = Size(contentRight - indentPx, size.height),
                            cornerRadius = CornerRadius(radius),
                        )
                        drawRoundRect(
                            color = targetColor.copy(alpha = 0.95f),
                            topLeft = Offset(lineXPx - accentW / 2, 0f),
                            size = Size(accentW, size.height),
                            cornerRadius = CornerRadius(accentW / 2),
                        )
                    }
                }
                .alpha(if (isDraggingThis) 0.35f else 1f),
        ) {
            TreeBranch(
                isLast = isLast,
                indent = indent,
                color = lineColor,
                strokeWidth = lineStrokeWidth,
                modifier = Modifier.fillMaxHeight(),
            )
            Box(modifier = Modifier.weight(1f).padding(vertical = 4.dp)) {
                Box(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        dragState?.contentWidths?.set(node.data, coords.size.width.toFloat())
                    },
                ) {
                    content(node.data, isAnyDragging)
                }
            }
        }

        if (node.children.isNotEmpty()) {
            Row(modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(indent)
                        .fillMaxHeight()
                        .drawBehind {
                            if (!isLast) {
                                drawLine(
                                    color = lineColor,
                                    start = Offset(size.width / 2, 0f),
                                    end = Offset(size.width / 2, size.height),
                                    strokeWidth = lineStrokeWidth.toPx(),
                                )
                            }
                        },
                )
                Column(modifier = Modifier.weight(1f)) {
                    TreeNodes(
                        nodes = node.children,
                        descendantsByNode = descendantsByNode,
                        indent = indent,
                        lineColor = lineColor,
                        lineStrokeWidth = lineStrokeWidth,
                        dragState = dragState,
                        onMove = onMove,
                        content = content,
                    )
                }
            }
        }

        if (isDropTarget && dragState.dropPosition == DropPosition.After) {
            DropIndicator(
                color = if (isInvalidDropTarget) invalidDropColor else lineColor,
                indent = indent,
                isInvalid = isInvalidDropTarget,
                style = dragState.dropIndicatorStyle,
            )
        }
    }
}

@Composable
private fun DropIndicator(
    color: Color,
    indent: Dp,
    isInvalid: Boolean = false,
    style: DropIndicatorStyle = DropIndicatorStyle.Sibling,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
    ) {
        val cy = size.height / 2
        val r = 3.dp.toPx()
        val strokeW = 1.5.dp.toPx()
        val lineX = indent.toPx() / 2
        val indicatorStartX = lineX + r * 2
        val nestedStartX = indent.toPx()

        when (style) {
            DropIndicatorStyle.Sibling -> {
                drawCircle(
                    color = color,
                    radius = r,
                    center = Offset(lineX, cy),
                    style = Stroke(width = strokeW),
                )
                drawLine(
                    color = color,
                    start = Offset(indicatorStartX, cy),
                    end = Offset(size.width, cy),
                    strokeWidth = strokeW,
                )
            }
            DropIndicatorStyle.NestedAppend -> {
                drawLine(
                    color = color,
                    start = Offset(lineX, 0f),
                    end = Offset(lineX, cy),
                    strokeWidth = strokeW,
                )
                drawCircle(
                    color = color,
                    radius = r,
                    center = Offset(lineX, cy),
                )
                drawLine(
                    color = color,
                    start = Offset(nestedStartX, cy),
                    end = Offset(size.width, cy),
                    strokeWidth = strokeW,
                )
            }
        }

        if (isInvalid) {
            val crossHalfSize = 3.dp.toPx()
            val crossCenter = Offset(
                x = when (style) {
                    DropIndicatorStyle.Sibling -> indicatorStartX + 8.dp.toPx()
                    DropIndicatorStyle.NestedAppend -> nestedStartX + 8.dp.toPx()
                },
                y = cy,
            )
            drawLine(
                color = color,
                start = Offset(crossCenter.x - crossHalfSize, crossCenter.y - crossHalfSize),
                end = Offset(crossCenter.x + crossHalfSize, crossCenter.y + crossHalfSize),
                strokeWidth = strokeW,
            )
            drawLine(
                color = color,
                start = Offset(crossCenter.x - crossHalfSize, crossCenter.y + crossHalfSize),
                end = Offset(crossCenter.x + crossHalfSize, crossCenter.y - crossHalfSize),
                strokeWidth = strokeW,
            )
        }
    }
}

@Composable
private fun TreeBranch(
    isLast: Boolean,
    indent: Dp,
    color: Color,
    strokeWidth: Dp,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .width(indent)
            .height(24.dp),
    ) {
        val midX = size.width / 2
        val midY = size.height / 2
        val sw = strokeWidth.toPx()

        drawLine(
            color = color,
            start = Offset(midX, 0f),
            end = Offset(midX, if (isLast) midY else size.height),
            strokeWidth = sw,
        )
        drawLine(
            color = color,
            start = Offset(midX, midY),
            end = Offset(size.width, midY),
            strokeWidth = sw,
        )
    }
}

internal fun <T> TreeNode<T>.moveNode(from: T, to: T, position: DropPosition): TreeNode<T> {
    val dragged = find(from) ?: return this
    if (to in dragged) return this
    val withoutDragged = removeNode(from)
    return withoutDragged.insertNode(dragged, to, position)
}

private fun <T> TreeNode<T>.descendantsByNode(): Map<T, Set<T>> {
    val descendants = mutableMapOf<T, Set<T>>()

    fun visit(node: TreeNode<T>): Set<T> {
        val directChildren = node.children.map { it.data }.toSet()
        val nestedChildren = node.children.flatMapTo(mutableSetOf()) { visit(it) }
        return (directChildren + nestedChildren).also { descendants[node.data] = it }
    }

    visit(this)
    return descendants
}

private fun <T> TreeNode<T>.removeNode(target: T): TreeNode<T> {
    if (data == target) return this
    return copy(children = children.filter { it.data != target }.map { it.removeNode(target) })
}

private fun <T> TreeNode<T>.insertNode(node: TreeNode<T>, nearTarget: T, position: DropPosition): TreeNode<T> {
    if (data == nearTarget && position == DropPosition.Inside) {
        return copy(children = children + node)
    }
    val idx = children.indexOfFirst { it.data == nearTarget }
    if (idx != -1) {
        if (position == DropPosition.Inside) {
            return copy(
                children = children.map {
                    if (it.data == nearTarget) it.copy(children = it.children + node) else it
                },
            )
        }
        val newChildren = children.toMutableList()
        newChildren.add(if (position == DropPosition.Before) idx else idx + 1, node)
        return copy(children = newChildren)
    }
    return copy(children = children.map { it.insertNode(node, nearTarget, position) })
}

private fun Modifier.undoRedoKeybindings(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
): Modifier = onPreviewKeyEvent { keyEvent ->
    when {
        keyEvent.isUndoShortcut() && canUndo -> {
            onUndo()
            true
        }
        keyEvent.isRedoShortcut() && canRedo -> {
            onRedo()
            true
        }
        else -> false
    }
}

private fun KeyEvent.isUndoShortcut(): Boolean {
    return type == KeyEventType.KeyDown &&
        (isCtrlPressed || isMetaPressed) &&
        !isShiftPressed &&
        key == Key.Z
}

private fun KeyEvent.isRedoShortcut(): Boolean {
    return type == KeyEventType.KeyDown && when {
        (isCtrlPressed || isMetaPressed) && isShiftPressed && key == Key.Z -> true
        isCtrlPressed && key == Key.Y -> true
        else -> false
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
private fun TreeDemoPreview() {
    val initialTree = buildTree("ValkyrieIcons") {
        child("Filled") {
            child("Add")
            child("Arrows") {
                child("ArrowUp")
                child("ArrowDown")
            }
            child("Close")
        }
        child("Outlined") {
            child("Home")
            child("Search")
        }
        child("Logo")
    }

    val history = rememberTreeHistoryState(initialTree = initialTree)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .undoRedoKeybindings(
                canUndo = history.canUndo,
                canRedo = history.canRedo,
                onUndo = history::undo,
                onRedo = history::redo,
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TooltipIconButton(
                key = AllIconsKeys.Actions.Undo,
                contentDescription = "Undo",
                enabled = history.canUndo,
                onClick = history::undo,
                tooltipText = "Undo move (Cmd/Ctrl+Z)",
            )
            TooltipIconButton(
                key = AllIconsKeys.Actions.Redo,
                contentDescription = "Redo",
                enabled = history.canRedo,
                onClick = history::redo,
                tooltipText = "Redo move (Cmd/Ctrl+Shift+Z)",
            )
        }

        TreeView(
            root = history.tree,
            onMove = history::move,
        ) { value, isDragging ->
            var isHovered by rememberMutableState { false }

            Row(
                modifier = Modifier
                    .heightIn(min = 24.dp)
                    .padding(start = 4.dp)
                    .onPointerEvent(PointerEventType.Enter) { isHovered = true }
                    .onPointerEvent(PointerEventType.Exit) { isHovered = false },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = value)
                MoreHorizontalAction(
                    modifier = Modifier.alpha(if (isHovered && !isDragging) 1f else 0f),
                    onClick = {},
                )
            }
        }
    }
}
