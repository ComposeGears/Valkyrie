package io.github.composegears.valkyrie.generator.util

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.*
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.addIf
import io.github.composegears.valkyrie.generator.ext.formatFloat
import io.github.composegears.valkyrie.generator.util.PathParams.*

fun CodeBlock.Builder.addPath(
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
                    when (pathParams.first()) {
                        is FillParam -> fillArg(path)
                        is FillAlphaParam -> TODO()
                        is FillTypeParam -> TODO()
                        is StrokeAlphaParam -> TODO()
                        is StrokeColorHexParam -> TODO()
                        is StrokeLineCapParam -> TODO()
                        is StrokeLineJoinParam -> TODO()
                        is StrokeLineMiterParam -> TODO()
                        is StrokeLineWidthParam -> TODO()
                    }
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
                    // TODO: arg "name" missing
                    fillArg(path)
                    fillAlphaArg(path)
                    strokeArg(path)
                    strokeAlphaArg(path)
                    strokeLineWidthArg(path)
                    strokeLineCapArg(path)
                    strokeLineJoinArg(path)
                    strokeLineMiterArg(path)
                    pathFillTypeArg(path)
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

private fun CodeBlock.Builder.fillArg(path: VectorNode.Path) {
    when (path.fill) {
        is Fill.Color -> {
            add("fill = %M(%M(0x${path.fill.colorHex}))", MemberNames.SolidColor, MemberNames.Color)
        }
        is Fill.LinearGradient -> {
            // TODO: simplify
            add(
                "fill = ${
                    with(path.fill) {
                        "%M(" +
                                "${getGradientStops(path.fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                                "start = %M(${startX}f,${startY}f), " +
                                "end = %M(${endX}f,${endY}f))"
                    }
                }",
                MemberNames.LinearGradient,
                repeat(path.fill.colorStops.size) {
                    MemberNames.Color
                },
                MemberNames.Offset,
                MemberNames.Offset
            )
        }
        is Fill.RadialGradient -> {
            add(
                "fill = ${
                    with(path.fill) {
                        "%M(${getGradientStops(path.fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                                "center = %M(${centerX}f,${centerY}f), " +
                                "radius = ${gradientRadius}f)"
                    }
                }",
                MemberNames.RadialGradient,
                repeat(path.fill.colorStops.size) {
                    MemberNames.Color
                },
                MemberNames.Offset
            )
        }
        null -> {}
    }
}

private fun getGradientStops(
    stops: List<Pair<Float, String>>
) = stops.map { stop ->
    "${stop.first}f to %M(0x${stop.second})"
}

private fun CodeBlock.Builder.fillAlphaArg(path: VectorNode.Path) {
    val fillAlpha = path.fillAlpha

    addIf(fillAlpha != 1f) {
        add("fillAlpha = ${fillAlpha.formatFloat()},\n")
    }
}

private fun CodeBlock.Builder.strokeArg(path: VectorNode.Path) {
    val strokeColorHex = path.strokeColorHex

    addIf(strokeColorHex != null) {
        add("stroke = %M(%M(0x$strokeColorHex)),\n", MemberNames.SolidColor, MemberNames.Color)
    }
}

private fun CodeBlock.Builder.strokeAlphaArg(path: VectorNode.Path) {
    val strokeAlpha = path.strokeAlpha

    addIf(strokeAlpha != 1f) {
        add("strokeAlpha = ${strokeAlpha.formatFloat()},\n")
    }
}

private fun CodeBlock.Builder.strokeLineWidthArg(path: VectorNode.Path) {
    val strokeLineWidth = path.strokeLineWidth.value

    addIf(strokeLineWidth != 0f) {
        add("strokeLineWidth = ${strokeLineWidth.formatFloat()},\n")
    }
}

private fun CodeBlock.Builder.strokeLineCapArg(path: VectorNode.Path) {
    val strokeLineCap = path.strokeLineCap

    addIf(strokeLineCap != StrokeCap.Butt) {
        add("strokeLineCap = %M,\n", strokeLineCap.memberName)
    }
}

private fun CodeBlock.Builder.strokeLineJoinArg(path: VectorNode.Path) {
    val strokeLineJoin = path.strokeLineJoin

    addIf(strokeLineJoin != StrokeJoin.Miter) {
        add("strokeLineJoin = %M,\n", strokeLineJoin.memberName)
    }
}

private fun CodeBlock.Builder.strokeLineMiterArg(path: VectorNode.Path) {
    val strokeLineMiter = path.strokeLineMiter

    addIf(strokeLineMiter != 4f) {
        add("strokeLineMiter = " + strokeLineMiter.formatFloat() + ",\n")
    }
}

private fun CodeBlock.Builder.pathFillTypeArg(path: VectorNode.Path) {
    val fillType = path.fillType

    addIf(fillType != FillType.NonZero) {
        add("pathFillType = %M,\n", fillType.memberName)
    }
}

private fun VectorNode.Path.buildPathParams() = buildList {
    if (fill != null) {
        add(FillParam(fill))
    }
    if (fillAlpha != 1f) {
        add(FillAlphaParam(fillAlpha))
    }
    if (strokeColorHex != null) {
        add(StrokeColorHexParam(strokeColorHex))
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

sealed interface PathParams {
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