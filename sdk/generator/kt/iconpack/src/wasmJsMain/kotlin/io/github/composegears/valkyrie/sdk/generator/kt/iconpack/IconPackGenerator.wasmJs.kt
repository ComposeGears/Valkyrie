package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree

public actual object IconPackGenerator {

    public actual fun create(config: IconPackGeneratorConfig): IconPackSpecOutput {
        val content = buildString {
            if (config.license != null) {
                appendLine(config.license.asBlockComment())
                appendLine()
            }
            appendLine("package ${config.packageName}")
            appendLine()

            val iconPack = createIconPack(
                iconPackTree = config.iconPackTree,
                indentSize = config.indentSize,
                isExplicit = config.useExplicitMode,
            )
            appendLine(iconPack)
        }

        return IconPackSpecOutput(
            name = config.iconPackTree.data,
            content = content,
        )
    }

    private fun createIconPack(iconPackTree: IconPackTree, indentSize: Int, isExplicit: Boolean): String {
        val prefix = if (isExplicit) "public " else ""
        if (iconPackTree.children.isEmpty()) {
            return "${prefix}object ${iconPackTree.data}"
        }

        val indent = " ".repeat(indentSize)
        val nested = iconPackTree.children.joinToString("\n\n") { child ->
            createIconPack(
                iconPackTree = child,
                indentSize = indentSize,
                isExplicit = isExplicit,
            ).indentLines(indent)
        }
        return "${prefix}object ${iconPackTree.data} {\n$nested\n}"
    }

    // prependIndent pads blank lines with spaces; this variant leaves them empty
    private fun String.indentLines(indent: String): String = lines().joinToString("\n") { if (it.isBlank()) "" else "$indent$it" }
}
