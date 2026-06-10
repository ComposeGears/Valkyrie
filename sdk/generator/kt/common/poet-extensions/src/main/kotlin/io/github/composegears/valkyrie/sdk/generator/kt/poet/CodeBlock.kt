package io.github.composegears.valkyrie.sdk.generator.kt.poet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.withIndent

public fun CodeBlock.Builder.argumentBlock(
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

public fun CodeBlock.Builder.builderBlock(
    argumentFlow: String,
    vararg args: Any?,
    block: CodeBlock.Builder.() -> Unit,
) {
    add("$argumentFlow\n", *args)
    withIndent(block)
    add("}")
}

public fun CodeBlock.Builder.newLine() {
    add("\n")
}

public fun CodeBlock.Builder.trailingComma() {
    add(",")
    newLine()
}
