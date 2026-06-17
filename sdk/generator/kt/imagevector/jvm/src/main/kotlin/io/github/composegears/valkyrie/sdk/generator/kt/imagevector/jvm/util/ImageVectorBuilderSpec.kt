package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.poet.newLine
import io.github.composegears.valkyrie.sdk.generator.kt.poet.trailingComma
import io.github.composegears.valkyrie.sdk.generator.kt.util.formatFloat
import io.github.composegears.valkyrie.sdk.generator.kt.util.trimTrailingZero
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

context(config: ImageVectorGeneratorConfig)
internal fun imageVectorBuilderSpecs(
    iconName: String,
    irVector: IrImageVector,
    path: CodeBlock.Builder.() -> Unit,
): CodeBlock = buildCodeBlock {
    add("%T.Builder(\n", ClassNames.ImageVector)
    withIndent {
        add("name = %S,\n", iconName)
        add("defaultWidth = %L.%M,\n", irVector.defaultWidth.trimTrailingZero(), MemberNames.Dp)
        add("defaultHeight = %L.%M,\n", irVector.defaultHeight.trimTrailingZero(), MemberNames.Dp)
        add("viewportWidth = %L,\n", irVector.viewportWidth.formatFloat())
        add("viewportHeight = %L", irVector.viewportHeight.formatFloat())
        if (irVector.autoMirror) {
            trailingComma()
            add("autoMirror = true")
        }
        if (config.imageVector.addTrailingComma) {
            trailingComma()
        } else {
            newLine()
        }
    }
    add(")")

    if (irVector.nodes.isNotEmpty()) {
        beginControlFlow(".apply")
        path()
        unindent()
        add("}")
    }
    addStatement(".build()")
}
