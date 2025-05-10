package io.github.composegears.valkyrie.generator.jvm.ext

import com.squareup.kotlinpoet.CodeBlock

fun CodeBlock.Builder.argumentBlock(
    argumentFlow: String,
    vararg args: Any?,
    isNested: Boolean = false,
    block: CodeBlock.Builder.() -> Unit,
) {
    add("$argumentFlow\n", *args)
    indention(block)
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
    indention(block)
    add("}")
}

fun CodeBlock.Builder.indention(block: CodeBlock.Builder.() -> Unit) {
    indent()
    block()
    unindent()
}

fun CodeBlock.Builder.newLine() {
    add("\n")
}

fun CodeBlock.Builder.trailingComma() {
    add(",")
    newLine()
}
