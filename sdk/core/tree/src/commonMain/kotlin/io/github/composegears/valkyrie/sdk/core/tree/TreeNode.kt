package io.github.composegears.valkyrie.sdk.core.tree

public interface TreeNode<T> {
    public val data: T
    public val children: List<TreeNode<T>>
}
