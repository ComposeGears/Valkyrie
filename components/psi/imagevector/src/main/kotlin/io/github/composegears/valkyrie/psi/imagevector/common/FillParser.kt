package io.github.composegears.valkyrie.psi.imagevector.common

import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.psi.extension.childOfType
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression

internal fun KtCallExpression.parseFill(): IrFill? {
    val arg = valueArguments.find { it.getArgumentName()?.asName?.identifier == "fill" }
    val fillExpression = arg?.getArgumentExpression() ?: return null

    return when (fillExpression) {
        is KtDotQualifiedExpression -> fillExpression.parseDotQualifiedExpression()
        is KtCallExpression -> fillExpression.parseCallExpression()
        else -> null
    }
}

private fun KtDotQualifiedExpression.parseDotQualifiedExpression(): IrFill? {
    val ktCallExpression = childOfType<KtCallExpression>() ?: return null

    return when (ktCallExpression.calleeExpression?.text) {
        "linearGradient" -> ktCallExpression.parseLinearGradient()
        "radialGradient" -> ktCallExpression.parseRadialGradient()
        else -> null
    }
}

private fun KtCallExpression.parseCallExpression(): IrFill? {
    val functionName = calleeExpression?.text

    return when (functionName) {
        "SolidColor" -> parseColor()
        "linearGradient" -> parseLinearGradient()
        "radialGradient" -> parseRadialGradient()
        else -> null
    }
}

private fun KtCallExpression.parseLinearGradient(): IrFill.LinearGradient? {
    var colorStops = mutableListOf<IrFill.ColorStop>()
    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f

    valueArguments.forEach { arg ->
        val argName = arg.getArgumentName()?.asName?.identifier
        val argValue = arg.getArgumentExpression()

        when (argName) {
            "colorStops" -> {
                colorStops = argValue.safeAs<KtCallExpression>()?.parseColorStops().orEmpty().toMutableList()
            }
            "start" -> {
                val startExpr = argValue.safeAs<KtCallExpression>()
                startX = startExpr?.valueArguments?.get(0)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
                startY = startExpr?.valueArguments?.get(1)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
            }
            "end" -> {
                val endExpr = argValue.safeAs<KtCallExpression>()
                endX = endExpr?.valueArguments?.get(0)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
                endY = endExpr?.valueArguments?.get(1)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
            }
        }
    }

    return when {
        colorStops.isEmpty() -> null
        else -> IrFill.LinearGradient(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            colorStops = colorStops,
        )
    }
}

private fun KtCallExpression.parseRadialGradient(): IrFill.RadialGradient? {
    var colorStops = mutableListOf<IrFill.ColorStop>()
    var centerX = 0f
    var centerY = 0f
    var gradientRadius = 0f

    valueArguments.forEach { arg ->
        val argName = arg.getArgumentName()?.asName?.identifier
        val argValue = arg.getArgumentExpression()

        when (argName) {
            "colorStops" -> {
                colorStops = argValue.safeAs<KtCallExpression>()?.parseColorStops().orEmpty().toMutableList()
            }
            "center" -> {
                val centerExpr = argValue.safeAs<KtCallExpression>()
                centerX = centerExpr?.valueArguments?.get(0)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
                centerY = centerExpr?.valueArguments?.get(1)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
            }
            "radius" -> {
                gradientRadius = argValue?.text?.toFloatOrNull() ?: 0f
            }
        }
    }

    return when {
        colorStops.isEmpty() -> return null
        else -> IrFill.RadialGradient(
            radius = gradientRadius,
            centerX = centerX,
            centerY = centerY,
            colorStops = colorStops,
        )
    }
}

private fun KtCallExpression.parseColorStops(): List<IrFill.ColorStop> {
    val colorStops = mutableListOf<IrFill.ColorStop>()

    valueArguments.forEach { arg ->
        val binaryExpression = arg.getArgumentExpression().safeAs<KtBinaryExpression>() ?: return@forEach

        val offset = binaryExpression.left?.text?.toFloatOrNull() ?: return@forEach
        val colorCallExpression = binaryExpression.right.safeAs<KtCallExpression>() ?: return@forEach
        val color = colorCallExpression.parseColor() ?: return@forEach

        colorStops.add(
            IrFill.ColorStop(
                offset = offset,
                irColor = color.irColor,
            ),
        )
    }

    return colorStops
}
