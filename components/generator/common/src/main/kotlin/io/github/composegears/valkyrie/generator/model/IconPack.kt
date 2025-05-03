package io.github.composegears.valkyrie.generator.model

data class IconPack(
    val name: String,
    val nested: List<IconPack> = emptyList(),
) {

    override fun toString(): String {
        fun StringBuilder.buildNestedTree(prefix: String, items: List<IconPack>) {
            items.forEachIndexed { index, iconPack ->
                val isLastChild = index == items.lastIndex
                val branchChar = if (isLastChild) "└── " else "├── "
                val nextPrefix = prefix + if (isLastChild) "\t" else "│\t"

                appendLine()
                append("$prefix$branchChar${iconPack.name}")

                if (iconPack.nested.isNotEmpty()) {
                    buildNestedTree(prefix = nextPrefix, items = iconPack.nested)
                }
            }
        }

        return buildString {
            if (name.isNotEmpty()) {
                appendLine()
                append(name)
                append(":")
                buildNestedTree(prefix = "", items = nested)
                appendLine()
            }
        }
    }

    companion object {
        fun fromString(input: String): IconPack {
            fun buildHierarchy(paths: List<List<String>>): List<IconPack> {
                if (paths.all { it.isEmpty() }) return emptyList()

                return paths
                    .groupBy { it.first() }
                    .map { (name, nestedPaths) ->
                        IconPack(
                            name = name,
                            nested = buildHierarchy(nestedPaths.mapNotNull { it.drop(1).takeIf { it.isNotEmpty() } }),
                        )
                    }
            }

            if (input.isEmpty()) {
                return IconPack(name = "")
            }

            val paths = input.split(',').map { it.split('.') }

            val rootNames = paths.map { it.first() }.distinct()
            if (rootNames.size != 1) {
                error("Invalid icon pack structure: expected a single root, but found ${rootNames.size} roots")
            }

            return IconPack(
                name = rootNames.first(),
                nested = buildHierarchy(paths.map { it.drop(1) }),
            )
        }

        fun toRawString(iconPack: IconPack): String {
            if (iconPack.name.isEmpty()) {
                return ""
            }

            val paths = mutableListOf<String>()

            fun traverse(node: IconPack, parentPath: String = "") {
                val currentPath = if (parentPath.isEmpty()) node.name else "$parentPath.${node.name}"

                if (node.nested.isEmpty()) {
                    paths.add(currentPath)
                } else {
                    node.nested.forEach { child ->
                        traverse(child, currentPath)
                    }
                }
            }

            traverse(iconPack)
            return paths.joinToString(separator = ",")
        }
    }
}
