package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import io.github.composegears.valkyrie.generator.core.asPathDataString
import io.github.composegears.valkyrie.generator.core.asStatement
import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.generator.jvm.ext.builderBlock
import io.github.composegears.valkyrie.generator.jvm.ext.newLine
import io.github.composegears.valkyrie.generator.jvm.ext.trailingComma
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.ClipPathParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.NameParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.PivotXParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.PivotYParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.RotateParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.ScaleXParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.ScaleYParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.TranslationXParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.GroupParams.TranslationYParam
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

context(config: ImageVectorSpecConfig)
internal fun CodeBlock.Builder.addGroup(
    path: IrVectorNode.IrGroup,
    groupBody: CodeBlock.Builder.() -> Unit,
) {
    val groupParams = path.buildGroupParams()

    when {
        groupParams.isEmpty() -> {
            beginControlFlow("%M", MemberNames.Group)
            groupBody()
            endControlFlow()
        }
        groupParams.size == 1 -> {
            add(
                codeBlock = buildCodeBlock {
                    add("%M(", MemberNames.Group)
                    fillGroupArgs(param = groupParams.first())
                    beginControlFlow(")")
                    groupBody()
                    endControlFlow()
                },
            )
        }
        else -> {
            add(
                codeBlock = buildCodeBlock {
                    add("%M(\n", MemberNames.Group)
                    withIndent {
                        groupParams.forEachIndexed { index, param ->
                            fillGroupArgs(param)
                            if (index == groupParams.lastIndex) {
                                if (config.addTrailingComma) {
                                    trailingComma()
                                } else {
                                    newLine()
                                }
                            } else {
                                trailingComma()
                            }
                        }
                    }
                    add(")")
                    beginControlFlow("")
                    groupBody()
                    endControlFlow()
                },
            )
        }
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.fillGroupArgs(param: GroupParams) {
    when (param) {
        is NameParam -> nameArg(param)
        is RotateParam -> rotateArg(param)
        is PivotXParam -> pivotXArg(param)
        is PivotYParam -> pivotYArg(param)
        is ScaleXParam -> scaleXArg(param)
        is ScaleYParam -> scaleYArg(param)
        is TranslationXParam -> translationXArg(param)
        is TranslationYParam -> translationYArg(param)
        is ClipPathParam -> clipPathArg(param)
    }
}

private fun CodeBlock.Builder.nameArg(param: NameParam) {
    add("name = %S", param.name)
}

private fun CodeBlock.Builder.rotateArg(param: RotateParam) {
    add("rotate = %L", param.rotate.formatFloat())
}

private fun CodeBlock.Builder.pivotXArg(param: PivotXParam) {
    add("pivotX = %L", param.pivotX.formatFloat())
}

private fun CodeBlock.Builder.pivotYArg(param: PivotYParam) {
    add("pivotY = %L", param.pivotY.formatFloat())
}

private fun CodeBlock.Builder.scaleXArg(param: ScaleXParam) {
    add("scaleX = %L", param.scaleX.formatFloat())
}

private fun CodeBlock.Builder.scaleYArg(param: ScaleYParam) {
    add("scaleY = %L", param.scaleY.formatFloat())
}

private fun CodeBlock.Builder.translationXArg(param: TranslationXParam) {
    add("translationX = %L", param.translationX.formatFloat())
}

private fun CodeBlock.Builder.translationYArg(param: TranslationYParam) {
    add("translationY = %L", param.translationY.formatFloat())
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.clipPathArg(param: ClipPathParam) {
    newLine()
    withIndent {
        if (config.usePathDataString) {
            val pathData = param.clipPath.asPathDataString()
            add("clipPathData = %M(%S)", MemberNames.AddPathNodes, pathData)
        } else {
            builderBlock("clipPathData = %M {", MemberNames.PathData) {
                param.clipPath.forEach { pathNode ->
                    addStatement("%L", pathNode.asStatement().replace(' ', '·'))
                }
            }
        }
    }
    newLine()
}

private fun IrVectorNode.IrGroup.buildGroupParams() = buildList {
    if (name.isNotEmpty()) {
        add(NameParam(name))
    }
    if (rotate != 0f) {
        add(RotateParam(rotate))
    }
    if (pivotX != 0f) {
        add(PivotXParam(pivotX))
    }
    if (pivotY != 0f) {
        add(PivotYParam(pivotY))
    }
    if (scaleX != 1f) {
        add(ScaleXParam(scaleX))
    }
    if (scaleY != 1f) {
        add(ScaleYParam(scaleY))
    }
    if (translationX != 0f) {
        add(TranslationXParam(translationX))
    }
    if (translationY != 0f) {
        add(TranslationYParam(translationY))
    }
    if (clipPathData.isNotEmpty()) {
        add(ClipPathParam(clipPathData))
    }
}

private sealed interface GroupParams {
    data class NameParam(val name: String) : GroupParams
    data class RotateParam(val rotate: Float) : GroupParams
    data class PivotXParam(val pivotX: Float) : GroupParams
    data class PivotYParam(val pivotY: Float) : GroupParams
    data class ScaleXParam(val scaleX: Float) : GroupParams
    data class ScaleYParam(val scaleY: Float) : GroupParams
    data class TranslationXParam(val translationX: Float) : GroupParams
    data class TranslationYParam(val translationY: Float) : GroupParams
    data class ClipPathParam(val clipPath: List<IrPathNode>) : GroupParams
}
