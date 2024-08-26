package io.github.composegears.valkyrie.generator.imagevector.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.formatFloat
import io.github.composegears.valkyrie.generator.ext.trimTrailingZero
import io.github.composegears.valkyrie.ir.IrImageVector

internal fun imageVectorBuilderSpecs(
    iconName: String,
    irVector: IrImageVector,
    path: CodeBlock.Builder.() -> Unit,
): CodeBlock = buildCodeBlock {
    add("%T.Builder(\n", ClassNames.ImageVector)
    indent()
    add("name = %S,\n", iconName)
    add("defaultWidth = %L.%M,\n", irVector.defaultWidth.trimTrailingZero(), MemberNames.Dp)
    add("defaultHeight = %L.%M,\n", irVector.defaultHeight.trimTrailingZero(), MemberNames.Dp)
    add("viewportWidth = %L,\n", irVector.viewportWidth.formatFloat())
    add("viewportHeight = %L\n", irVector.viewportHeight.formatFloat())
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
