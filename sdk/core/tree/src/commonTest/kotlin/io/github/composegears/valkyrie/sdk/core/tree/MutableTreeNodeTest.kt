package io.github.composegears.valkyrie.sdk.core.tree

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class MutableTreeNodeTest {

    @Test
    fun `children should return a snapshot`() {
        val root = MutableTreeNode("root")
        root.addChild("first")

        val snapshot = root.children

        root.addChild("second")

        assertEquals(listOf("first"), snapshot.map(TreeNode<String>::data))
        assertEquals(listOf("first", "second"), root.children.map(TreeNode<String>::data))
    }

    @Test
    fun `equals should not match foreign TreeNode implementation`() {
        val node = MutableTreeNode("root")
        val foreign = object : TreeNode<String> {
            override val data: String = "root"
            override val children: List<TreeNode<String>> = emptyList()
        }

        assertFalse(node == foreign)
    }

    @Test
    fun `addChild should reject itself as a child`() {
        val root = MutableTreeNode("root")

        assertFailsWith<IllegalArgumentException> {
            root.addChild(root)
        }
    }

    @Test
    fun `addChild should reject a child that already has a parent`() {
        val firstRoot = MutableTreeNode("first-root")
        val secondRoot = MutableTreeNode("second-root")
        val child = MutableTreeNode("child")

        firstRoot.addChild(child)

        assertFailsWith<IllegalArgumentException> {
            secondRoot.addChild(child)
        }
    }
}
