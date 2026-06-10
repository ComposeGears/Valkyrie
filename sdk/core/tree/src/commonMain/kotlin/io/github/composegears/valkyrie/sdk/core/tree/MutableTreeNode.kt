package io.github.composegears.valkyrie.sdk.core.tree

public class MutableTreeNode<T>(override val data: T) : TreeNode<T> {
    private var parent: MutableTreeNode<T>? = null

    private val _children: MutableList<MutableTreeNode<T>> = mutableListOf()
    override val children: List<TreeNode<T>>
        get() = _children.toList()

    public fun addChild(data: T, init: MutableTreeNode<T>.() -> Unit = {}) {
        addChild(MutableTreeNode(data).apply(init))
    }

    public fun addChild(child: MutableTreeNode<T>) {
        require(child !== this) { "A node cannot be added as a child of itself" }
        require(child.parent == null) { "A node that already has a parent cannot be added again" }
        child.parent = this
        _children += child
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MutableTreeNode<*>) return false
        return data == other.data && _children == other._children
    }

    override fun hashCode(): Int = 31 * (data?.hashCode() ?: 0) + _children.hashCode()

    override fun toString(): String = "MutableTreeNode($data, $_children)"
}
