package io.github.composegears.valkyrie.generator.util

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.MemberNames.ImageVectorBuilder
import androidx.compose.material.icons.generator.vector.Vector
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock

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
    add("viewportWidth = %Lf,\n", vector.viewportWidth.trimTrailingZero())
    add("viewportHeight = %Lf\n", vector.viewportHeight.trimTrailingZero())
    unindent()
    add(")")

    if (vector.nodes.isNotEmpty()) {
        beginControlFlow(".apply")
        path()
        endControlFlow()
    }
    addStatement(".build()")
}