package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.jvm.ext.formatFloat
import io.github.composegears.valkyrie.generator.jvm.ext.newLine
import io.github.composegears.valkyrie.generator.jvm.ext.trailingComma
import io.github.composegears.valkyrie.generator.jvm.ext.trimTrailingZero
import io.github.composegears.valkyrie.ir.IrImageVector

internal fun imageVectorBuilderSpecs(
    iconName: String,
    irVector: IrImageVector,
    path: CodeBlock.Builder.() -> Unit,
    addTrailingComma: Boolean,
): CodeBlock = buildCodeBlock {
    add("%T.Builder(\n", ClassNames.ImageVector)
    indent()
    add("name = %S,\n", iconName)
    add("defaultWidth = %L.%M,\n", irVector.defaultWidth.trimTrailingZero(), MemberNames.Dp)
    add("defaultHeight = %L.%M,\n", irVector.defaultHeight.trimTrailingZero(), MemberNames.Dp)
    add("viewportWidth = %L,\n", irVector.viewportWidth.formatFloat())
    add("viewportHeight = %L", irVector.viewportHeight.formatFloat())
    if (irVector.autoMirror) {
        trailingComma()
        add("autoMirror = true")
    }
    if (addTrailingComma) {
        trailingComma()
    } else {
        newLine()
    }
    unindent()
    add(")")

    if (irVector.nodes.isNotEmpty()) {
        beginControlFlow(".apply")
        path()
        unindent()
        add("}")
    }
    addStatement(".build()")
}
