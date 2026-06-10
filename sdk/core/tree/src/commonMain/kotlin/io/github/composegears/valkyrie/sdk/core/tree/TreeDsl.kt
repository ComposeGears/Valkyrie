package io.github.composegears.valkyrie.sdk.core.tree

@DslMarker
public annotation class TreeDsl

@TreeDsl
public fun <T> buildTree(data: T, init: MutableTreeNode<T>.() -> Unit = {}): TreeNode<T> = MutableTreeNode(data).apply(init)

@TreeDsl
public fun <T> MutableTreeNode<T>.child(data: T, init: MutableTreeNode<T>.() -> Unit = {}) {
    addChild(data, init)
}

@TreeDsl
public fun <T> MutableTreeNode<T>.child(node: MutableTreeNode<T>) {
    addChild(node)
}
