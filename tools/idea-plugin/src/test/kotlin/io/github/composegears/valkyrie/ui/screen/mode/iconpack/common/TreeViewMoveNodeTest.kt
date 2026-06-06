package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class TreeViewMoveNodeTest {

    @Test
    fun `moving node into its own descendant keeps tree unchanged`() {
        val tree = TreeNode(
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

        val movedTree = tree.moveNode(
            from = "parent",
            to = "child",
            position = DropPosition.Inside,
        )

        assertThat(movedTree).isEqualTo(tree)
    }

    @Test
    fun `moving node next to sibling still works`() {
        val tree = TreeNode(
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

        val movedTree = tree.moveNode(
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
}
