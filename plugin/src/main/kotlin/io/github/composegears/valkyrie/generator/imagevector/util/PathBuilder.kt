package io.github.composegears.valkyrie.generator.imagevector.util

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.Fill
import androidx.compose.material.icons.generator.vector.FillType
import androidx.compose.material.icons.generator.vector.StrokeCap
import androidx.compose.material.icons.generator.vector.StrokeJoin
import androidx.compose.material.icons.generator.vector.VectorNode
import androidx.compose.ui.graphics.PathFillType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.imagevector.ext.formatFloat
import io.github.composegears.valkyrie.generator.imagevector.ext.toColorHex
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.FillTypeParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeAlphaParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeColorHexParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineCapParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineJoinParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineMiterParam
import io.github.composegears.valkyrie.generator.imagevector.util.PathParams.StrokeLineWidthParam

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
            // TODO: simplify
            add(
                "fill = ${
                    "%M(" +
                            "${getGradientStops(fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                            "start = %M(${fill.startX}f,${fill.startY}f), " +
                            "end = %M(${fill.endX}f,${fill.endY}f))"
                }",
                MemberNames.LinearGradient,
                repeat(fill.colorStops.size) {
                    MemberNames.Color
                },
                MemberNames.Offset,
                MemberNames.Offset
            )
        }
        is Fill.RadialGradient -> {
            // TODO: simplify
            add(
                "fill = ${
                    "%M(${getGradientStops(fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                            "center = %M(${fill.centerX}f,${fill.centerY}f), " +
                            "radius = ${fill.gradientRadius}f)"
                }",
                MemberNames.RadialGradient,
                repeat(fill.colorStops.size) {
                    MemberNames.Color
                },
                MemberNames.Offset
            )
        }
    }
}

private fun getGradientStops(
    stops: List<Pair<Float, String>>
) = stops.map { stop ->
    "${stop.first}f to %M(${stop.second.toColorHex()})"
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
    add("strokeLineCap = %T.%L", androidx.compose.ui.graphics.StrokeCap::class, param.strokeLineCap.name)
}

private fun CodeBlock.Builder.strokeLineJoinArg(param: StrokeLineJoinParam) {
    add("strokeLineJoin = %T.%L", androidx.compose.ui.graphics.StrokeJoin::class, param.strokeLineJoin.name)
}

private fun CodeBlock.Builder.strokeLineMiterArg(param: StrokeLineMiterParam) {
    add("strokeLineMiter = ${param.strokeLineMiter.formatFloat()}")
}

private fun CodeBlock.Builder.pathFillTypeArg(param: FillTypeParam) {
    add("pathFillType = %T.%L", PathFillType::class, param.fillType.name)
}

private fun VectorNode.Path.buildPathParams() = buildList {
    fill?.let {
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