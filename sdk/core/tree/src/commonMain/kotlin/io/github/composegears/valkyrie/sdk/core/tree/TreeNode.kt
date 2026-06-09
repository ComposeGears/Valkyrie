package io.github.composegears.valkyrie.sdk.core.tree

class TreeNode<T>(
    val data: T,
    val children: List<TreeNode<T>> = emptyList(),
)
