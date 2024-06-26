package io.github.composegears.valkyrie.generator.imagevector.util

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.MemberNames.ImageVectorBuilder
import androidx.compose.material.icons.generator.vector.Vector
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.imagevector.ext.formatFloat
import io.github.composegears.valkyrie.generator.imagevector.ext.trimTrailingZero

fun imageVectorBuilderSpecs(
    iconName: String,
    vector: Vector,
    path: CodeBlock.Builder.() -> Unit,
): CodeBlock = buildCodeBlock {
    add("%M(\n", ImageVectorBuilder)
    indent()
    add("name = %S,\n", iconName)
    add("defaultWidth = %L.%M,\n", vector.width.value.trimTrailingZero(), MemberNames.Dp)
    add("defaultHeight = %L.%M,\n", vector.height.value.trimTrailingZero(), MemberNames.Dp)
    add("viewportWidth = %L,\n", vector.viewportWidth.formatFloat())
    add("viewportHeight = %L\n", vector.viewportHeight.formatFloat())
    unindent()
    add(")")

    if (vector.nodes.isNotEmpty()) {
        beginControlFlow(".apply")
        path()
        unindent()
        add("}")
    }
    addStatement(".build()")
}