package io.github.composegears.valkyrie.generator.core

import io.github.composegears.valkyrie.sdk.core.tree.TreeNode

typealias IconPack = TreeNode<String>

fun iconPackOf(input: String): IconPack {
    fun buildHierarchy(paths: List<List<String>>): List<IconPack> {
        if (paths.all { it.isEmpty() }) return emptyList()

        return paths
            .groupBy { it.first() }
            .map { (name, nestedPaths) ->
                TreeNode(
                    data = name,
                    children = buildHierarchy(nestedPaths.mapNotNull { it.drop(1).takeIf { it.isNotEmpty() } }),
                )
            }
    }

    if (input.isEmpty()) {
        return TreeNode(data = "")
    }

    val paths = input.split(',').map { it.split('.') }

    val rootNames = paths.map { it.first() }.distinct()
    if (rootNames.size != 1) {
        error("Invalid icon pack structure: expected a single root, but found ${rootNames.size} roots")
    }

    return TreeNode(
        data = rootNames.first(),
        children = buildHierarchy(paths.map { it.drop(1) }),
    )
}

fun IconPack.encode(): String {
    if (data.isEmpty()) return ""

    val paths = mutableListOf<String>()

    fun traverse(node: IconPack, parentPath: String = "") {
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

fun IconPack.toPrettyString(): String {
    fun StringBuilder.buildNestedTree(prefix: String, items: List<IconPack>) {
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
