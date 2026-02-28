package io.github.composegears.valkyrie.generator.kmp.imagevector.util

import io.github.composegears.valkyrie.generator.core.asPathDataString
import io.github.composegears.valkyrie.generator.core.asStatement
import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorRenderConfig
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

internal fun collectGroupParams(
    node: IrVectorNode.IrGroup,
    config: ImageVectorRenderConfig,
    level: Int,
    writer: CodeWriter,
): List<String> {
    return buildList {
        with(node) {
            if (name.isNotEmpty()) add("name = \"${name.escapeKotlin()}\"")
            if (rotate != 0f) add("rotate = ${rotate.formatFloat()}")
            if (pivotX != 0f) add("pivotX = ${pivotX.formatFloat()}")
            if (pivotY != 0f) add("pivotY = ${pivotY.formatFloat()}")
            if (scaleX != 1f) add("scaleX = ${scaleX.formatFloat()}")
            if (scaleY != 1f) add("scaleY = ${scaleY.formatFloat()}")
            if (translationX != 0f) add("translationX = ${translationX.formatFloat()}")
            if (translationY != 0f) add("translationY = ${translationY.formatFloat()}")
            if (clipPathData.isNotEmpty()) {
                if (config.usePathDataString) {
                    add("clipPathData = addPathNodes(\"${clipPathData.asPathDataString().escapeKotlin()}\")")
                } else {
                    add(
                        "clipPathData = PathData {\n" +
                            clipPathData.joinToString(separator = "\n") {
                                "${writer.indent(level + 2)}${it.asStatement()}"
                            } +
                            "\n${writer.indent(level + 1)}}",
                    )
                }
            }
        }
    }
}

internal fun collectPathParams(
    path: IrVectorNode.IrPath,
    config: ImageVectorRenderConfig,
): List<String> {
    val params = mutableListOf<String>()
    if (path.name.isNotEmpty()) {
        params += "name = \"${path.name.escapeKotlin()}\""
    }

    when (val fill = path.fill) {
        null -> Unit
        is IrFill.Color -> {
            if (!fill.irColor.isTransparent()) {
                params += "fill = SolidColor(${fill.irColor.renderColor(config)})"
            }
        }
        is IrFill.LinearGradient -> params += fill.renderLinearGradient(config)
        is IrFill.RadialGradient -> params += fill.renderRadialGradient(config)
    }

    if (path.fillAlpha != 1f) params += "fillAlpha = ${path.fillAlpha.formatFloat()}"

    val stroke = path.stroke
    if (stroke is IrStroke.Color && !stroke.irColor.isTransparent()) {
        params += "stroke = SolidColor(${stroke.irColor.renderColor(config)})"
    }
    with(path) {
        if (strokeAlpha != 1f) params += "strokeAlpha = ${strokeAlpha.formatFloat()}"
        if (strokeLineWidth != 0f) params += "strokeLineWidth = ${strokeLineWidth.formatFloat()}"
        if (strokeLineCap != IrStrokeLineCap.Butt) params += "strokeLineCap = StrokeCap.${strokeLineCap.name}"
        if (strokeLineJoin != IrStrokeLineJoin.Miter) params += "strokeLineJoin = StrokeJoin.${strokeLineJoin.name}"
        if (strokeLineMiter != 4f) params += "strokeLineMiter = ${strokeLineMiter.formatFloat()}"
        if (pathFillType != IrPathFillType.NonZero) params += "pathFillType = PathFillType.${pathFillType.name}"
    }
    return params
}

internal fun IrColor.renderColor(config: ImageVectorRenderConfig): String {
    val colorRef = if (config.fullQualifiedImports.color) {
        "androidx.compose.ui.graphics.Color"
    } else {
        "Color"
    }
    val named = toName()
    if (named != null && config.useComposeColors) {
        val alphaValue = alpha.toFloat() / 0xFF
        return if (alphaValue < 1f) {
            "$colorRef.$named.copy(alpha = ${alphaValue.formatFloat()})"
        } else {
            "$colorRef.$named"
        }
    }
    return "$colorRef(${toHexLiteral()})"
}

internal fun String.escapeKotlin(): String = replace("\\", "\\\\").replace("\"", "\\\"")

private fun IrFill.Gradient.renderGradientParams(
    config: ImageVectorRenderConfig,
    gradientType: String,
    extraParams: String,
): String {
    val nestedIndent = " ".repeat(config.indentSize)
    val deepNestedIndent = nestedIndent + nestedIndent
    val colorStops = colorStops.joinToString(separator = ",\n") {
        "$deepNestedIndent${it.offset.formatFloat()} to ${it.irColor.renderColor(config)}"
    }
    val brush = if (config.fullQualifiedImports.brush) {
        "androidx.compose.ui.graphics.Brush"
    } else {
        "Brush"
    }

    return "fill = $brush.$gradientType(\n" +
        "${nestedIndent}colorStops = arrayOf(\n$colorStops\n$nestedIndent),\n" +
        extraParams +
        ")"
}

private fun IrFill.LinearGradient.renderLinearGradient(config: ImageVectorRenderConfig): String {
    val offset = if (config.fullQualifiedImports.offset) "androidx.compose.ui.geometry.Offset" else "Offset"
    val nestedIndent = " ".repeat(config.indentSize)

    return renderGradientParams(
        config = config,
        gradientType = "linearGradient",
        extraParams = "${nestedIndent}start = $offset(${startX.formatFloat()}, ${startY.formatFloat()}),\n" +
            "${nestedIndent}end = $offset(${endX.formatFloat()}, ${endY.formatFloat()})\n",
    )
}

private fun IrFill.RadialGradient.renderRadialGradient(config: ImageVectorRenderConfig): String {
    val offset = if (config.fullQualifiedImports.offset) "androidx.compose.ui.geometry.Offset" else "Offset"
    val nestedIndent = " ".repeat(config.indentSize)

    return renderGradientParams(
        config = config,
        gradientType = "radialGradient",
        extraParams = "${nestedIndent}center = $offset(${centerX.formatFloat()}, ${centerY.formatFloat()}),\n" +
            "${nestedIndent}radius = ${radius.formatFloat()}\n",
    )
}
