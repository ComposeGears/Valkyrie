package io.github.composegears.valkyrie.generator.ext

import com.squareup.kotlinpoet.CodeBlock

fun CodeBlock.Builder.indention(block: CodeBlock.Builder.() -> Unit) {
    indent()
    block()
    unindent()
}
