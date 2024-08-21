package io.github.composegears.valkyrie.generator.ext

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

fun CodeBlock.Builder.indention(block: CodeBlock.Builder.() -> Unit) {
  indent()
  block()
  unindent()
}

fun CodeBlock.Builder.newLine() {
  add("\n")
}
