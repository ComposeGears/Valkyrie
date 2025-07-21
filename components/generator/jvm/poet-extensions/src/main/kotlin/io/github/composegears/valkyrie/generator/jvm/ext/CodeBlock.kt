package io.github.composegears.valkyrie.generator.jvm.ext

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent

fun CodeBlock.Builder.argumentBlock(
    argumentFlow: String,
    vararg args: Any?,
    isNested: Boolean = false,
    block: CodeBlock.Builder.() -> Unit,
) {
    add("$argumentFlow\n", *args)
    withIndent(block)
    newLine()
    add(")")
    if (isNested) {
        add(",")
        newLine()
    }
}

fun CodeBlock.Builder.builderBlock(
    argumentFlow: String,
    vararg args: Any?,
    block: CodeBlock.Builder.() -> Unit,
) {
    add("$argumentFlow\n", *args)
    withIndent(block)
    add("}")
}

fun CodeBlock.Builder.newLine() {
    add("\n")
}

fun CodeBlock.Builder.trailingComma() {
    add(",")
    newLine()
}
