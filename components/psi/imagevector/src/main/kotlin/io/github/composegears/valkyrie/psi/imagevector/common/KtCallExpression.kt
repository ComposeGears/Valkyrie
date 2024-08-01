package io.github.composegears.valkyrie.psi.imagevector.common

import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
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

internal fun KtCallExpression.parseStroke(): IrStroke? {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "stroke" }
    val colorCall = arg?.getArgumentExpression().safeAs<KtCallExpression>()

    return when (val color = colorCall?.parseColor()) {
        null -> null
        else -> IrStroke.Color(color.colorHex)
    }
}

internal fun KtCallExpression.parseColor(): IrFill.Color? {
    val colorArg = valueArguments.firstOrNull()?.getArgumentExpression()?.text
        ?.removePrefix("Color(0x")
        ?.removePrefix("0x")
        ?.removeSuffix(")") ?: return null
    return IrFill.Color(colorHex = colorArg)
}
