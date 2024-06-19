package io.github.composegears.valkyrie.generator.util

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.*
import androidx.compose.ui.graphics.PathFillType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
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
                    fillPathArgs(pathParams.first(), path)
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
                        fillPathArgs(param, path)
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

private fun CodeBlock.Builder.fillPathArgs(
    param: PathParams,
    path: VectorNode.Path
) {
    // TODO: arg "name" missing
    when (param) {
        is FillParam -> fillArg(path)
        is FillAlphaParam -> fillAlphaArg(path)
        is FillTypeParam -> pathFillTypeArg(path)
        is StrokeAlphaParam -> strokeAlphaArg(path)
        is StrokeColorHexParam -> strokeArg(path)
        is StrokeLineCapParam -> strokeLineCapArg(path)
        is StrokeLineJoinParam -> strokeLineJoinArg(path)
        is StrokeLineMiterParam -> strokeLineMiterArg(path)
        is StrokeLineWidthParam -> strokeLineWidthArg(path)
    }
}

private fun CodeBlock.Builder.fillArg(path: VectorNode.Path) {
    when (val fill = path.fill) {
        is Fill.Color -> {
            add("fill = %M(%M(0x${fill.colorHex}))", MemberNames.SolidColor, MemberNames.Color)
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
        null -> {}
    }
}

private fun getGradientStops(
    stops: List<Pair<Float, String>>
) = stops.map { stop ->
    "${stop.first}f to %M(0x${stop.second})"
}

private fun CodeBlock.Builder.fillAlphaArg(path: VectorNode.Path) {
    add("fillAlpha = ${path.fillAlpha.formatFloat()}")
}

private fun CodeBlock.Builder.strokeArg(path: VectorNode.Path) {
    add("stroke = %M(%M(0x${path.strokeColorHex}))", MemberNames.SolidColor, MemberNames.Color)
}

private fun CodeBlock.Builder.strokeAlphaArg(path: VectorNode.Path) {
    add("strokeAlpha = ${path.strokeAlpha.formatFloat()}")
}

private fun CodeBlock.Builder.strokeLineWidthArg(path: VectorNode.Path) {
    add("strokeLineWidth = ${path.strokeLineWidth.value.formatFloat()}")
}

private fun CodeBlock.Builder.strokeLineCapArg(path: VectorNode.Path) {
    add("strokeLineCap = %T.%L", androidx.compose.ui.graphics.StrokeCap::class, path.strokeLineCap.name)
}

private fun CodeBlock.Builder.strokeLineJoinArg(path: VectorNode.Path) {
    add("strokeLineJoin = %T.%L", androidx.compose.ui.graphics.StrokeJoin::class, path.strokeLineJoin.name)
}

private fun CodeBlock.Builder.strokeLineMiterArg(path: VectorNode.Path) {
    add("strokeLineMiter = ${path.strokeLineMiter.formatFloat()}")
}

private fun CodeBlock.Builder.pathFillTypeArg(path: VectorNode.Path) {
    add("pathFillType = %T.%L", PathFillType::class, path.fillType.name)
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