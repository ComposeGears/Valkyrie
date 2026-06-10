package io.github.composegears.valkyrie.sdk.core.tree

data class TreeNode<T>(
    val data: T,
    val children: List<TreeNode<T>> = emptyList(),
)

fun <T> TreeNode<T>.find(data: T): TreeNode<T>? {
    if (this.data == data) return this
    return children.firstNotNullOfOrNull { it.find(data) }
}

operator fun <T> TreeNode<T>.get(data: T): TreeNode<T> = find(data) ?: error("Node '$data' not found in tree")

operator fun <T> TreeNode<T>.contains(data: T): Boolean = find(data) != null

fun <T, R> TreeNode<T>.map(transform: (T) -> R): TreeNode<R> = TreeNode(
    data = transform(data),
    children = children.map { it.map(transform) },
)

fun <T> TreeNode<T>.flatten(): List<TreeNode<T>> = listOf(this) + children.flatMap { it.flatten() }
