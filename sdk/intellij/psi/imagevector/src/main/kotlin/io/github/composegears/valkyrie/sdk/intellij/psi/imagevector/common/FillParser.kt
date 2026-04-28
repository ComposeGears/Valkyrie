package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.common

import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.util.childOfType
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression

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
                val startExpr = argValue?.resolveCallExpression()
                startX = startExpr?.valueArguments?.get(0)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
                startY = startExpr?.valueArguments?.get(1)?.getArgumentExpression()?.text?.toFloatOrNull() ?: 0f
            }
            "end" -> {
                val endExpr = argValue?.resolveCallExpression()
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
                val centerExpr = argValue?.resolveCallExpression()
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
        val color = binaryExpression.right?.parseColorExpression() ?: return@forEach

        colorStops.add(
            IrFill.ColorStop(
                offset = offset,
                irColor = color.irColor,
            ),
        )
    }

    return colorStops
}

/**
 * Parses a color from any expression:
 * - `Color(0xFF...)` → [KtCallExpression]
 * - `Color.White` → [KtDotQualifiedExpression] (property reference)
 * - `Color.White.copy(alpha = 0.75f)` → [KtDotQualifiedExpression] (method call on named color)
 */
private fun KtExpression.parseColorExpression(): IrFill.Color? = when (this) {
    is KtCallExpression -> parseColor()
    is KtDotQualifiedExpression -> {
        val text = this.text
        if (text.startsWith("Color.")) {
            val irColor = getIrColor(text.removePrefix("Color.")) ?: return null
            IrFill.Color(irColor = irColor)
        } else {
            null
        }
    }
    else -> null
}

/**
 * Resolves a [KtCallExpression] from either a direct call (`Offset(...)`)
 * or a fully-qualified call (`androidx.compose.ui.geometry.Offset(...)`).
 */
private fun KtExpression.resolveCallExpression(): KtCallExpression? = when (this) {
    is KtCallExpression -> this
    is KtDotQualifiedExpression -> childOfType<KtCallExpression>()
    else -> null
}
