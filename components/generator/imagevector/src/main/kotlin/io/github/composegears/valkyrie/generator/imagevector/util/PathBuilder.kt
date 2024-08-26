package io.github.composegears.valkyrie.generator.imagevector.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.generator.ext.argumentBlock
import io.github.composegears.valkyrie.generator.ext.formatFloat
import io.github.composegears.valkyrie.generator.ext.indention
import io.github.composegears.valkyrie.generator.ext.newLine
import io.github.composegears.valkyrie.generator.ext.toColorHex
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.PathFillTypeParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeColorHexParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineCapParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineJoinParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineMiterParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineWidthParam
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode

internal fun CodeBlock.Builder.addPath(
    path: IrVectorNode.IrPath,
    pathBody: CodeBlock.Builder.() -> Unit,
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
                    fillPathArgs(param = pathParams.first(), handleMultiline = true)
                    beginControlFlow(")")
                    pathBody()
                    endControlFlow()
                },
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
                },
            )
        }
    }
}

private fun CodeBlock.Builder.fillPathArgs(
    param: PathParams,
    handleMultiline: Boolean = false,
) {
    // TODO: arg "name" missing
    when (param) {
        is FillParam -> fillArg(param, handleMultiline)
        is FillAlphaParam -> fillAlphaArg(param)
        is PathFillTypeParam -> pathFillTypeArg(param)
        is StrokeAlphaParam -> strokeAlphaArg(param)
        is StrokeColorHexParam -> strokeArg(param)
        is StrokeLineCapParam -> strokeLineCapArg(param)
        is StrokeLineJoinParam -> strokeLineJoinArg(param)
        is StrokeLineMiterParam -> strokeLineMiterArg(param)
        is StrokeLineWidthParam -> strokeLineWidthArg(param)
    }
}

private fun CodeBlock.Builder.fillArg(
    path: FillParam,
    handleMultiline: Boolean,
) {
    when (val fill = path.fill) {
        is IrFill.Color -> {
            add("fill = %M(%M(${fill.colorHex.toColorHex()}))", MemberNames.SolidColor, MemberNames.Color)
        }
        is IrFill.LinearGradient -> {
            if (handleMultiline) {
                newLine()
                indention {
                    addLinearGradient(fill)
                }
                newLine()
            } else {
                addLinearGradient(fill)
            }
        }
        is IrFill.RadialGradient -> {
            if (handleMultiline) {
                newLine()
                indention {
                    addRadialGradient(fill)
                }
                newLine()
            } else {
                addRadialGradient(fill)
            }
        }
    }
}

private fun CodeBlock.Builder.addLinearGradient(fill: IrFill.LinearGradient) {
    argumentBlock("fill = %T.linearGradient(", ClassNames.Brush) {
        argumentBlock("colorStops = arrayOf(", isNested = true) {
            add(
                fill.colorStops.joinToString(separator = ",\n") { stop ->
                    "${stop.offset.formatFloat()} to %M(${stop.color.toColorHex()})"
                },
                *Array(fill.colorStops.size) { MemberNames.Color },
            )
        }
        add(
            "start = %M(${fill.startX.formatFloat()}, ${fill.startY.formatFloat()}),",
            MemberNames.Offset,
        )
        newLine()
        add(
            "end = %M(${fill.endX.formatFloat()}, ${fill.endY.formatFloat()})",
            MemberNames.Offset,
        )
    }
}

private fun CodeBlock.Builder.addRadialGradient(fill: IrFill.RadialGradient) {
    argumentBlock("fill = %T.radialGradient(", ClassNames.Brush) {
        argumentBlock("colorStops = arrayOf(", isNested = true) {
            add(
                fill.colorStops.joinToString(separator = ",\n") { stop ->
                    "${stop.offset.formatFloat()} to %M(${stop.color.toColorHex()})"
                },
                *Array(fill.colorStops.size) { MemberNames.Color },
            )
        }
        add(
            "center = %M(${fill.centerX.formatFloat()}, ${fill.centerY.formatFloat()}),",
            MemberNames.Offset,
        )
        newLine()
        add("radius = ${fill.radius.formatFloat()}")
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

private fun CodeBlock.Builder.pathFillTypeArg(param: PathFillTypeParam) {
    add("pathFillType = %T.%L", ClassNames.PathFillType, param.pathFillType.name)
}

private fun IrVectorNode.IrPath.buildPathParams() = buildList {
    fill?.takeUnless { it is IrFill.Color && it.isTransparent() }?.let {
        add(FillParam(it))
    }
    if (fillAlpha != 1f) {
        add(FillAlphaParam(fillAlpha))
    }
    stroke.safeAs<IrStroke.Color>()?.let {
        add(StrokeColorHexParam(it.colorHex))
    }
    if (strokeAlpha != 1f) {
        add(StrokeAlphaParam(strokeAlpha))
    }
    if (strokeLineWidth != 0f) {
        add(StrokeLineWidthParam(strokeLineWidth))
    }
    if (strokeLineCap != IrStrokeLineCap.Butt) {
        add(StrokeLineCapParam(strokeLineCap))
    }
    if (strokeLineJoin != IrStrokeLineJoin.Miter) {
        add(StrokeLineJoinParam(strokeLineJoin))
    }
    if (strokeLineMiter != 4f) {
        add(StrokeLineMiterParam(strokeLineMiter))
    }
    if (pathFillType != IrPathFillType.NonZero) {
        add(PathFillTypeParam(pathFillType))
    }
}

private fun IrFill.Color.isTransparent() = colorHex == "00000000" || colorHex == "0000"

internal sealed interface PathParams {
    data class FillParam(val fill: IrFill) : PathParams
    data class FillAlphaParam(val fillAlpha: Float) : PathParams
    data class StrokeColorHexParam(val strokeColorHex: String) : PathParams
    data class StrokeAlphaParam(val strokeAlpha: Float) : PathParams
    data class StrokeLineWidthParam(val strokeLineWidth: Float) : PathParams
    data class StrokeLineCapParam(val strokeLineCap: IrStrokeLineCap) : PathParams
    data class StrokeLineJoinParam(val strokeLineJoin: IrStrokeLineJoin) : PathParams
    data class StrokeLineMiterParam(val strokeLineMiter: Float) : PathParams
    data class PathFillTypeParam(val pathFillType: IrPathFillType) : PathParams
}
