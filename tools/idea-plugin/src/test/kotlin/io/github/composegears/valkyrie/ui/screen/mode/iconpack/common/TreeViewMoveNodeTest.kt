package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.ui.geometry.Offset
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import org.junit.jupiter.api.Test

class TreeViewMoveNodeTest {

    private val sampleTree = buildTree("root") {
        child("parent") {
            child("child")
        }
        child("sibling")
    }

    @Test
    fun `moving node into its own descendant keeps tree unchanged`() {
        val movedTree = sampleTree.moveNode(
            from = "parent",
            to = "child",
            position = DropPosition.Inside,
        )

        assertThat(movedTree).isEqualTo(sampleTree)
    }

    @Test
    fun `moving node next to sibling still works`() {
        val movedTree = sampleTree.moveNode(
            from = "child",
            to = "sibling",
            position = DropPosition.After,
        )

        assertThat(movedTree).isEqualTo(
            buildTree("root") {
                child("parent")
                child("sibling")
                child("child")
            },
        )
    }

    @Test
    fun `undo restores previous tree after move`() {
        val history = TreeHistoryState(initialTree = sampleTree)

        history.move(
            from = "child",
            to = "sibling",
            position = DropPosition.After,
        )

        history.undo()

        assertThat(history.tree).isEqualTo(sampleTree)
    }

    @Test
    fun `redo reapplies undone move`() {
        val history = TreeHistoryState(initialTree = sampleTree)

        history.move(
            from = "child",
            to = "sibling",
            position = DropPosition.After,
        )
        val movedTree = history.tree

        history.undo()
        history.redo()

        assertThat(history.tree).isEqualTo(movedTree)
    }

    @Test
    fun `new move clears redo history`() {
        val history = TreeHistoryState(initialTree = sampleTree)

        history.move(
            from = "child",
            to = "sibling",
            position = DropPosition.After,
        )
        history.undo()

        history.move(
            from = "parent",
            to = "sibling",
            position = DropPosition.After,
        )

        assertThat(history.canRedo).isEqualTo(false)
    }

    @Test
    fun `drop target can resolve to parent after subtree bottom`() {
        val resolved = resolveDropTarget(
            rootPosition = Offset(x = 8f, y = 88f),
            indentPx = 24f,
            insideHalfZonePx = 9f,
            dragging = "logo",
            layouts = mapOf(
                "outlined" to NodeDropLayout(
                    rowLeft = 0f,
                    rowTop = 0f,
                    rowCenterY = 10f,
                    subtreeBottom = 90f,
                ),
                "search" to NodeDropLayout(
                    rowLeft = 24f,
                    rowTop = 40f,
                    rowCenterY = 50f,
                    subtreeBottom = 60f,
                ),
                "logo" to NodeDropLayout(
                    rowLeft = 0f,
                    rowTop = 70f,
                    rowCenterY = 80f,
                    subtreeBottom = 90f,
                ),
            ),
        )

        assertThat(resolved).isEqualTo(
            ResolvedDropTarget(
                target = "outlined",
                position = DropPosition.After,
                isInvalid = false,
            ),
        )
    }

    @Test
    fun `drop target can resolve to last child after when pointer is on nested indent`() {
        val resolved = resolveDropTarget(
            rootPosition = Offset(x = 34f, y = 68f),
            indentPx = 24f,
            insideHalfZonePx = 9f,
            dragging = "logo",
            layouts = mapOf(
                "outlined" to NodeDropLayout(
                    rowLeft = 0f,
                    rowTop = 0f,
                    rowCenterY = 10f,
                    subtreeBottom = 60f,
                ),
                "search" to NodeDropLayout(
                    rowLeft = 24f,
                    rowTop = 40f,
                    rowCenterY = 50f,
                    subtreeBottom = 60f,
                ),
                "logo" to NodeDropLayout(
                    rowLeft = 0f,
                    rowTop = 70f,
                    rowCenterY = 80f,
                    subtreeBottom = 90f,
                ),
            ),
        )

        assertThat(resolved).isEqualTo(
            ResolvedDropTarget(
                target = "search",
                position = DropPosition.After,
                isInvalid = false,
                indicatorStyle = DropIndicatorStyle.NestedAppend,
            ),
        )
    }
}
