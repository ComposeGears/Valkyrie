package io.github.composegears.valkyrie.sdk.core.tree

public fun <T> TreeNode<T>.toMutableTree(): MutableTreeNode<T> {
    val result = MutableTreeNode(data)
    children.forEach { result.addChild(it.toMutableTree()) }
    return result
}

public fun <T> TreeNode<T>.find(data: T): TreeNode<T>? {
    if (this.data == data) return this
    return children.firstNotNullOfOrNull { it.find(data) }
}

public operator fun <T> TreeNode<T>.get(data: T): TreeNode<T> = find(data) ?: error("Node '$data' not found in tree")

public operator fun <T> TreeNode<T>.contains(data: T): Boolean = find(data) != null

public fun <T> TreeNode<T>.copy(children: List<TreeNode<T>> = this.children): TreeNode<T> {
    val result = MutableTreeNode(data)
    children.forEach { result.addChild(it.toMutableTree()) }
    return result
}

public fun <T, R> TreeNode<T>.map(transform: (T) -> R): TreeNode<R> {
    fun rec(node: TreeNode<T>): MutableTreeNode<R> {
        val result = MutableTreeNode(transform(node.data))
        node.children.forEach { result.addChild(rec(it)) }
        return result
    }
    return rec(this)
}

public fun <T> TreeNode<T>.flatten(): List<TreeNode<T>> = listOf(this) + children.flatMap { it.flatten() }
