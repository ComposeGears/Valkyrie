package io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree

import io.github.composegears.valkyrie.sdk.core.tree.MutableTreeNode
import io.github.composegears.valkyrie.sdk.core.tree.TreeNode
import io.github.composegears.valkyrie.sdk.core.tree.buildTree

public typealias IconPackTree = TreeNode<String>

/**
 * Returns all node names from root to the deepest first child.
 * E.g. ValkyrieIcons -> Material -> Rounded → ["ValkyrieIcons", "Material", "Rounded"]
 */
public fun IconPackTree.pathSegments(): List<String> {
    val segments = mutableListOf(data)
    var current = this
    while (current.children.isNotEmpty()) {
        current = current.children.first()
        segments.add(current.data)
    }
    return segments
}

public fun iconPackOf(input: String): IconPackTree {
    fun MutableTreeNode<String>.getOrCreateChild(name: String): MutableTreeNode<String> {
        val existingChild = children.firstOrNull { it.data == name }
        if (existingChild != null) {
            return existingChild as MutableTreeNode<String>
        }

        return MutableTreeNode(name).also(::addChild)
    }

    if (input.isEmpty()) {
        return buildTree("")
    }

    val paths = input.split(',').map { it.split('.') }

    val rootNames = paths.map { it.first() }.distinct()
    if (rootNames.size != 1) {
        error("Invalid icon pack structure: expected a single root, but found ${rootNames.size} roots")
    }

    return MutableTreeNode(rootNames.first()).apply {
        paths.forEach { path ->
            var current = this
            path.drop(1).forEach { segment ->
                current = current.getOrCreateChild(segment)
            }
        }
    }
}

public fun IconPackTree.encode(): String {
    if (data.isEmpty()) return ""

    val paths = mutableListOf<String>()

    fun traverse(node: IconPackTree, parentPath: String = "") {
        val currentPath = if (parentPath.isEmpty()) node.data else "$parentPath.${node.data}"

        if (node.children.isEmpty()) {
            paths.add(currentPath)
        } else {
            node.children.forEach { child ->
                traverse(child, currentPath)
            }
        }
    }

    traverse(this)
    return paths.joinToString(separator = ",")
}

public fun IconPackTree.navigate(path: String): IconPackTree {
    return path.split('.').fold(this) { node, part -> node.children.first { it.data == part } }
}

public fun IconPackTree.toPrettyString(): String {
    fun StringBuilder.buildNestedTree(prefix: String, items: List<IconPackTree>) {
        items.forEachIndexed { index, iconPack ->
            val isLastChild = index == items.lastIndex
            val branchChar = if (isLastChild) "└── " else "├── "
            val nextPrefix = prefix + if (isLastChild) "\t" else "│\t"

            appendLine()
            append("$prefix$branchChar${iconPack.data}")

            if (iconPack.children.isNotEmpty()) {
                buildNestedTree(prefix = nextPrefix, items = iconPack.children)
            }
        }
    }

    return buildString {
        if (data.isNotEmpty()) {
            appendLine()
            append(data)
            append(":")
            buildNestedTree(prefix = "", items = children)
            appendLine()
        }
    }
}
