package io.github.composegears.valkyrie.generator.imagevector.util

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.Fill
import androidx.compose.material.icons.generator.vector.FillType
import androidx.compose.material.icons.generator.vector.StrokeCap
import androidx.compose.material.icons.generator.vector.StrokeJoin
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.formatFloat
import io.github.composegears.valkyrie.generator.ext.indention
import io.github.composegears.valkyrie.generator.ext.toColorHex
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillTypeParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeColorHexParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineCapParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineJoinParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineMiterParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineWidthParam

internal fun CodeBlock.Builder.addPath(
    path: VectorNode.Path,
    pathBody: CodeBlock.Builder.() -> Unit
) {
    val pathParams = path.buildPathParams()

    when {
        pathParams.isEmpty() -> {
            beginControlFlow("%M", MemberNames.Path)
            pathBody()
            endControlFlow()
        }
        pathParams.size == 1 -> {
            add(
                codeBlock = buildCodeBlock {
                    add("%M(", MemberNames.Path)
                    fillPathArgs(pathParams.first())
                    beginControlFlow(")")
                    pathBody()
                    endControlFlow()
                }
            )
        }
        else -> {
            add(
                codeBlock = buildCodeBlock {
                    add("%M(\n", MemberNames.Path)
                    indent()
                    pathParams.forEachIndexed { index, param ->
                        fillPathArgs(param)
                        if (index == pathParams.lastIndex) {
                            add("\n")
                        } else {
                            add(",\n")
                        }
                    }
                    unindent()
                    add(")")
                    beginControlFlow("")
                    pathBody()
                    endControlFlow()
                }
            )
        }
    }
}

private fun CodeBlock.Builder.fillPathArgs(param: PathParams) {
    // TODO: arg "name" missing
    when (param) {
        is FillParam -> fillArg(param)
        is FillAlphaParam -> fillAlphaArg(param)
        is FillTypeParam -> pathFillTypeArg(param)
        is StrokeAlphaParam -> strokeAlphaArg(param)
        is StrokeColorHexParam -> strokeArg(param)
        is StrokeLineCapParam -> strokeLineCapArg(param)
        is StrokeLineJoinParam -> strokeLineJoinArg(param)
        is StrokeLineMiterParam -> strokeLineMiterArg(param)
        is StrokeLineWidthParam -> strokeLineWidthArg(param)
    }
}

private fun CodeBlock.Builder.fillArg(path: FillParam) {
    when (val fill = path.fill) {
        is Fill.Color -> {
            add("fill = %M(%M(${fill.colorHex.toColorHex()}))", MemberNames.SolidColor, MemberNames.Color)
        }
        is Fill.LinearGradient -> {
            add("\n")
            indention {
                add("fill = %T.linearGradient(\n", ClassNames.Brush)
                indention {
                    add("colorStops = arrayOf(\n")
                    indention {
                        add(
                            fill.colorStops.joinToString(separator = ",\n") { stop ->
                                "${stop.first.formatFloat()} to %M(${stop.second.toColorHex()})"
                            },
                            *Array(fill.colorStops.size) { MemberNames.Color },
                        )
                    }
                    add("\n),\n")
                    add(
                        "start = %M(${fill.startX.formatFloat()}, ${fill.startY.formatFloat()}),\n",
                        MemberNames.Offset
                    )
                    add(
                        "end = %M(${fill.endX.formatFloat()}, ${fill.endY.formatFloat()})\n",
                        MemberNames.Offset
                    )
                }
                add(")\n")
            }
        }
        is Fill.RadialGradient -> {
            add("\n")
            indention {
                add("fill = %T.radialGradient(\n", ClassNames.Brush)
                indention {
                    add("colorStops = arrayOf(\n")
                    indention {
                        add(
                            fill.colorStops.joinToString(separator = ",\n") { stop ->
                                "${stop.first.formatFloat()} to %M(${stop.second.toColorHex()})"
                            },
                            *Array(fill.colorStops.size) { MemberNames.Color },
                        )
                    }
                    add("\n),\n")
                    add(
                        "center = %M(${fill.centerX.formatFloat()}, ${fill.centerY.formatFloat()}),\n",
                        MemberNames.Offset
                    )
                    add("radius = ${fill.gradientRadius.formatFloat()}\n")
                }
                add(")\n")
            }
        }
    }
}

private fun CodeBlock.Builder.fillAlphaArg(param: FillAlphaParam) {
    add("fillAlpha = ${param.fillAlpha.formatFloat()}")
}

private fun CodeBlock.Builder.strokeArg(param: StrokeColorHexParam) {
    add("stroke = %M(%M(${param.strokeColorHex.toColorHex()}))", MemberNames.SolidColor, MemberNames.Color)
}

private fun CodeBlock.Builder.strokeAlphaArg(param: StrokeAlphaParam) {
    add("strokeAlpha = ${param.strokeAlpha.formatFloat()}")
}

private fun CodeBlock.Builder.strokeLineWidthArg(param: StrokeLineWidthParam) {
    add("strokeLineWidth = ${param.strokeLineWidth.formatFloat()}")
}

private fun CodeBlock.Builder.strokeLineCapArg(param: StrokeLineCapParam) {
    add("strokeLineCap = %T.%L", ClassNames.StrokeCap, param.strokeLineCap.name)
}

private fun CodeBlock.Builder.strokeLineJoinArg(param: StrokeLineJoinParam) {
    add("strokeLineJoin = %T.%L", ClassNames.StrokeJoin, param.strokeLineJoin.name)
}

private fun CodeBlock.Builder.strokeLineMiterArg(param: StrokeLineMiterParam) {
    add("strokeLineMiter = ${param.strokeLineMiter.formatFloat()}")
}

private fun CodeBlock.Builder.pathFillTypeArg(param: FillTypeParam) {
    add("pathFillType = %T.%L", ClassNames.PathFillType, param.fillType.name)
}

private fun VectorNode.Path.buildPathParams() = buildList {
    fill?.takeUnless { it is Fill.Color && it.isTransparent() }?.let {
        add(FillParam(it))
    }
    if (fillAlpha != 1f) {
        add(FillAlphaParam(fillAlpha))
    }
    strokeColorHex?.let {
        add(StrokeColorHexParam(it))
    }
    if (strokeAlpha != 1f) {
        add(StrokeAlphaParam(strokeAlpha))
    }
    if (strokeLineWidth.value != 0f) {
        add(StrokeLineWidthParam(strokeLineWidth.value))
    }
    if (strokeLineCap != StrokeCap.Butt) {
        add(StrokeLineCapParam(strokeLineCap))
    }
    if (strokeLineJoin != StrokeJoin.Miter) {
        add(StrokeLineJoinParam(strokeLineJoin))
    }
    if (strokeLineMiter != 4f) {
        add(StrokeLineMiterParam(strokeLineMiter))
    }
    if (fillType != FillType.NonZero) {
        add(FillTypeParam(fillType))
    }
}

private fun Fill.Color.isTransparent() = colorHex == "00000000" || colorHex == "0000"

internal sealed interface PathParams {
    data class FillParam(val fill: Fill) : PathParams
    data class FillAlphaParam(val fillAlpha: Float) : PathParams
    data class StrokeColorHexParam(val strokeColorHex: String) : PathParams
    data class StrokeAlphaParam(val strokeAlpha: Float) : PathParams
    data class StrokeLineWidthParam(val strokeLineWidth: Float) : PathParams
    data class StrokeLineCapParam(val strokeLineCap: StrokeCap) : PathParams
    data class StrokeLineJoinParam(val strokeLineJoin: StrokeJoin) : PathParams
    data class StrokeLineMiterParam(val strokeLineMiter: Float) : PathParams
    data class FillTypeParam(val fillType: FillType) : PathParams
}