package io.github.composegears.valkyrie.generator.ext

import com.squareup.kotlinpoet.CodeBlock

fun CodeBlock.Builder.addIf(
    condition: Boolean,
    block: CodeBlock.Builder.() -> Unit
): CodeBlock.Builder = apply {
    if (condition) {
        block()
    }
}
