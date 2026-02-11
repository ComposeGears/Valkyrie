package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.common

import io.github.composegears.valkyrie.parser.common.PathParser
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import org.jetbrains.kotlin.psi.KtCallExpression

internal fun KtCallExpression.extractPathFillType(): IrPathFillType {
    val argument = valueArguments.find { it.getArgumentName()?.asName?.identifier == "pathFillType" }

    return when (argument?.getArgumentExpression()?.text) {
        "PathFillType.NonZero", "NonZero" -> IrPathFillType.NonZero
        "PathFillType.EvenOdd", "EvenOdd" -> IrPathFillType.EvenOdd
        else -> IrPathFillType.NonZero
    }
}

internal fun KtCallExpression.extractStrokeJoin(): IrStrokeLineJoin {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "strokeLineJoin" }

    return when (arg?.getArgumentExpression()?.text) {
        "StrokeJoin.Miter", "Miter" -> IrStrokeLineJoin.Miter
        "StrokeJoin.Round", "Round" -> IrStrokeLineJoin.Round
        "StrokeJoin.Bevel", "Bevel" -> IrStrokeLineJoin.Bevel
        else -> IrStrokeLineJoin.Miter
    }
}

internal fun KtCallExpression.extractStrokeCap(): IrStrokeLineCap {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "strokeLineCap" }

    return when (arg?.getArgumentExpression()?.text) {
        "StrokeCap.Butt", "Butt" -> IrStrokeLineCap.Butt
        "StrokeCap.Round", "Round" -> IrStrokeLineCap.Round
        "StrokeCap.Square", "Square" -> IrStrokeLineCap.Square
        else -> IrStrokeLineCap.Butt
    }
}

internal fun KtCallExpression.parseFloatArg(name: String): Float? {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == name }

    return arg?.getArgumentExpression()?.text?.toFloatOrNull()
}

internal fun KtCallExpression.parseStringArg(name: String): String? {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == name }

    return arg?.getArgumentExpression()?.text?.removeSurrounding("\"")
}

internal fun KtCallExpression.parseStroke(): IrStroke? {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "stroke" }
    val colorCall = arg?.getArgumentExpression().safeAs<KtCallExpression>()

    return when (val color = colorCall?.parseColor()) {
        null -> null
        else -> IrStroke.Color(irColor = color.irColor)
    }
}

internal fun KtCallExpression.parseColor(): IrFill.Color? {
    val colorFunction = valueArguments.firstOrNull()?.getArgumentExpression()?.text ?: return null
    val colorArg = if (colorFunction.startsWith("Color.")) {
        getIrColor(colorFunction.removePrefix("Color.")) ?: return null
    } else {
        IrColor(colorFunction.removeSurrounding(prefix = "Color(", suffix = ")"))
    }
    return IrFill.Color(irColor = colorArg)
}

internal fun KtCallExpression.parsePathDataArg(): List<IrPathNode> {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "pathData" } ?: return emptyList()
    val call = arg.getArgumentExpression().safeAs<KtCallExpression>() ?: return emptyList()
    if (call.calleeExpression?.text != "addPathNodes") return emptyList()

    val raw = call.valueArguments.firstOrNull()?.getArgumentExpression()?.text ?: return emptyList()
    val pathData = raw.stripQuotes()

    return runCatching { PathParser.parsePathString(pathData) }.getOrElse { emptyList() }
}

private const val ALPHA_MOD = ".copy(alpha = "

// TODO: Should resolve color from PSI rather than parsing strings
internal fun getIrColor(colorName: String): IrColor? {
    val color = IrColor.nameToArgb[colorName]
    if (color != null) return color
    if (colorName.contains(ALPHA_MOD)) return parseColorWithAlpha(colorName)
    return null
}

private fun parseColorWithAlpha(value: String): IrColor? {
    val parts = value.split(ALPHA_MOD)
    val baseColorArgb = IrColor.nameToArgb[parts.first()] ?: return null
    val alpha = parts.getOrNull(1)?.removeSuffix("f)")?.toFloatOrNull() ?: return null
    val argb = ((alpha * 0xFF).toInt() shl 24) or (baseColorArgb.argb and 0xFFFFFF)
    return IrColor(argb = argb)
}
