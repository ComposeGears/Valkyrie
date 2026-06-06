package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class TreeViewMoveNodeTest {

    private val sampleTree = TreeNode(
        data = "root",
        children = listOf(
            TreeNode(
                data = "parent",
                children = listOf(
                    TreeNode("child"),
                ),
            ),
            TreeNode("sibling"),
        ),
    )

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
            TreeNode(
                data = "root",
                children = listOf(
                    TreeNode("parent"),
                    TreeNode("sibling"),
                    TreeNode("child"),
                ),
            ),
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
}
