package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.generator.jvm.ext.argumentBlock
import io.github.composegears.valkyrie.generator.jvm.ext.newLine
import io.github.composegears.valkyrie.generator.jvm.ext.trailingComma
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.FillAlphaParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.FillParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.NameParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.PathFillTypeParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeAlphaParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeColorParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeLineCapParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeLineJoinParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeLineMiterParam
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.PathParams.StrokeLineWidthParam
import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode

context(config: ImageVectorSpecConfig)
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
                    fillPathArgs(
                        param = pathParams.first(),
                        handleMultiline = true,
                    )
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
                    withIndent {
                        pathParams.forEachIndexed { index, param ->
                            fillPathArgs(param)
                            if (index == pathParams.lastIndex) {
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
                    pathBody()
                    endControlFlow()
                },
            )
        }
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.fillPathArgs(
    param: PathParams,
    handleMultiline: Boolean = false,
) {
    when (param) {
        is NameParam -> nameArg(param)
        is FillParam -> fillArg(param, handleMultiline)
        is FillAlphaParam -> fillAlphaArg(param)
        is PathFillTypeParam -> pathFillTypeArg(param)
        is StrokeAlphaParam -> strokeAlphaArg(param)
        is StrokeColorParam -> strokeArg(param)
        is StrokeLineCapParam -> strokeLineCapArg(param)
        is StrokeLineJoinParam -> strokeLineJoinArg(param)
        is StrokeLineMiterParam -> strokeLineMiterArg(param)
        is StrokeLineWidthParam -> strokeLineWidthArg(param)
    }
}

private fun CodeBlock.Builder.nameArg(param: NameParam) {
    add("name = %S", param.name)
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.fillArg(
    path: FillParam,
    handleMultiline: Boolean,
) {
    when (val fill = path.fill) {
        is IrFill.Color -> {
            add("fill = %M(", MemberNames.SolidColor)
            addColor(fill.irColor)
            add(")")
        }
        is IrFill.LinearGradient -> {
            if (handleMultiline) {
                newLine()
                withIndent {
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
                withIndent {
                    addRadialGradient(fill)
                }
                newLine()
            } else {
                addRadialGradient(fill)
            }
        }
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.addLinearGradient(fill: IrFill.LinearGradient) {
    fun gradientBody() {
        argumentBlock("colorStops = arrayOf(", isNested = true) {
            addColorStops(fill.colorStops)
        }
        if (config.fullQualifiedImports.offset) {
            add(
                "start = %L(${fill.startX.formatFloat()}, ${fill.startY.formatFloat()}),",
                MemberNames.Offset.canonicalName,
            )
        } else {
            add(
                "start = %M(${fill.startX.formatFloat()}, ${fill.startY.formatFloat()}),",
                MemberNames.Offset,
            )
        }
        newLine()
        if (config.fullQualifiedImports.offset) {
            add(
                "end = %L(${fill.endX.formatFloat()}, ${fill.endY.formatFloat()})",
                MemberNames.Offset.canonicalName,
            )
        } else {
            add(
                "end = %M(${fill.endX.formatFloat()}, ${fill.endY.formatFloat()})",
                MemberNames.Offset,
            )
        }
    }

    if (config.fullQualifiedImports.brush) {
        argumentBlock("fill = %L.linearGradient(", ClassNames.Brush.canonicalName) {
            gradientBody()
        }
    } else {
        argumentBlock("fill = %T.linearGradient(", ClassNames.Brush) {
            gradientBody()
        }
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.addRadialGradient(fill: IrFill.RadialGradient) {
    fun gradientBody() {
        argumentBlock("colorStops = arrayOf(", isNested = true) {
            addColorStops(fill.colorStops)
        }
        if (config.fullQualifiedImports.offset) {
            add(
                "center = %L(${fill.centerX.formatFloat()}, ${fill.centerY.formatFloat()}),",
                MemberNames.Offset.canonicalName,
            )
        } else {
            add(
                "center = %M(${fill.centerX.formatFloat()}, ${fill.centerY.formatFloat()}),",
                MemberNames.Offset,
            )
        }
        newLine()
        add("radius = ${fill.radius.formatFloat()}")
    }

    if (config.fullQualifiedImports.brush) {
        argumentBlock("fill = %L.radialGradient(", ClassNames.Brush.canonicalName) {
            gradientBody()
        }
    } else {
        argumentBlock("fill = %T.radialGradient(", ClassNames.Brush) {
            gradientBody()
        }
    }
}

private fun CodeBlock.Builder.fillAlphaArg(param: FillAlphaParam) {
    add("fillAlpha = ${param.fillAlpha.formatFloat()}")
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.strokeArg(param: StrokeColorParam) {
    add("stroke = %M(", MemberNames.SolidColor)
    addColor(param.strokeColor)
    add(")")
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

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.addColor(color: IrColor) {
    if (config.fullQualifiedImports.color) {
        add("%L", MemberNames.Color.canonicalName)
    } else {
        add("%M", MemberNames.Color)
    }

    val name = color.toName()
    if (name != null && config.useComposeColors) {
        val alphaValue = color.alpha.toFloat() / 0xFF
        if (alphaValue < 1f) {
            add(".$name.copy(alpha = ${alphaValue.formatFloat()})")
        } else {
            add(".$name")
        }
    } else {
        add("(${color.toHexLiteral()})")
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.addColorStops(stops: List<IrFill.ColorStop>) {
    val lastIndex = stops.lastIndex
    stops.forEachIndexed { index, stop ->
        add("${stop.offset.formatFloat()} to ")
        addColor(stop.irColor)
        if (index != lastIndex) add(",\n")
    }
}

private fun IrVectorNode.IrPath.buildPathParams() = buildList {
    if (name.isNotEmpty()) {
        add(NameParam(name))
    }
    fill?.takeUnless { it is IrFill.Color && it.irColor.isTransparent() }?.let {
        add(FillParam(it))
    }
    if (fillAlpha != 1f) {
        add(FillAlphaParam(fillAlpha))
    }
    stroke.safeAs<IrStroke.Color>()?.takeIf { !it.irColor.isTransparent() }?.let {
        add(StrokeColorParam(it.irColor))
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

private sealed interface PathParams {
    data class NameParam(val name: String) : PathParams
    data class FillParam(val fill: IrFill) : PathParams
    data class FillAlphaParam(val fillAlpha: Float) : PathParams
    data class StrokeColorParam(val strokeColor: IrColor) : PathParams
    data class StrokeAlphaParam(val strokeAlpha: Float) : PathParams
    data class StrokeLineWidthParam(val strokeLineWidth: Float) : PathParams
    data class StrokeLineCapParam(val strokeLineCap: IrStrokeLineCap) : PathParams
    data class StrokeLineJoinParam(val strokeLineJoin: IrStrokeLineJoin) : PathParams
    data class StrokeLineMiterParam(val strokeLineMiter: Float) : PathParams
    data class PathFillTypeParam(val pathFillType: IrPathFillType) : PathParams
}
