package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.generator.iconpack.IconPackSpecOutput

actual object IconPackGenerator {

    actual fun create(config: IconPackGeneratorConfig): IconPackSpecOutput {
        val content = buildString {
            appendLine("package ${config.packageName}")
            appendLine()

            val iconPack = createIconPack(
                iconPack = config.iconPack,
                indentSize = config.indentSize,
                isExplicit = config.useExplicitMode,
            ).clearBlankLines()
            appendLine(iconPack)
        }

        return IconPackSpecOutput(name = config.iconPack.name, content = content)
    }

    private fun createIconPack(iconPack: IconPack, indentSize: Int, isExplicit: Boolean): String {
        return buildString {
            if (isExplicit) append("public ")
            append("object ${iconPack.name}")

            if (iconPack.nested.isNotEmpty()) {
                appendLine(" {")
                val lastIndex = iconPack.nested.lastIndex
                iconPack.nested.forEachIndexed { i, nestedPack ->
                    val pack = createIconPack(
                        iconPack = nestedPack,
                        indentSize = indentSize,
                        isExplicit = isExplicit,
                    )
                    appendLine(pack.prependIndent(indent = " ".repeat(indentSize)))
                    if (i != lastIndex) appendLine()
                }
                append("}")
            }
        }
    }

    private fun String.clearBlankLines(): String = this.lines().joinToString("\n") { it.takeUnless { it.isBlank() }.orEmpty() }
}
