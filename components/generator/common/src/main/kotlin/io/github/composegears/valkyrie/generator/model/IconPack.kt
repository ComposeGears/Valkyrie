package io.github.composegears.valkyrie.generator.model

data class IconPack(
    val name: String,
    val nested: List<IconPack> = emptyList(),
) {

    override fun toString(): String = buildString {
        appendLine()
        append(name)
        if (name.isNotEmpty()) {
            append(":")
        }
        buildNestedTree(prefix = "", items = nested)
    }

    private fun StringBuilder.buildNestedTree(prefix: String, items: List<IconPack>) {
        items.forEachIndexed { index, iconPack ->
            val isLastChild = index == items.lastIndex
            val branchChar = if (isLastChild) "└── " else "├── "
            val nextPrefix = prefix + if (isLastChild) "\t" else "│\t"

            appendLine()
            append(prefix)
            append(branchChar)
            append(iconPack.name)

            if (iconPack.nested.isNotEmpty()) {
                buildNestedTree(prefix = nextPrefix, items = iconPack.nested)
            }
        }
    }
}
