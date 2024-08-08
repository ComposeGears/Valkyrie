package io.github.composegears.valkyrie.psi.imagevector.block

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.PathNode
import io.github.composegears.valkyrie.psi.extension.childrenOfType
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.ValueArgument

fun KtBlockExpression.parseApplyBlock(): List<PathData> {
    val pathsData = mutableListOf<PathData>()

    val applyFunctionCall = childrenOfType<KtCallExpression>()
        .firstOrNull { it.calleeExpression?.text == "apply" }

    val lambdaExpression = applyFunctionCall?.lambdaArguments?.firstOrNull()?.getLambdaExpression()
    val applyBlock = lambdaExpression?.bodyExpression ?: return pathsData

    applyBlock.statements.filterIsInstance<KtCallExpression>().forEach { statement ->
        if (statement.calleeExpression?.text == "path") {
            val fillColor = parseColor(statement.valueArguments, "fill")
            val fillAlpha = parseValueArgument(statement.valueArguments, "fillAlpha") ?: 1.0f
            val strokeColor = parseColor(statement.valueArguments, "stroke")
            val strokeAlpha = parseValueArgument(statement.valueArguments, "strokeAlpha") ?: 1.0f
            val strokeLineWidth = parseValueArgument(statement.valueArguments, "strokeLineWidth") ?: 0.0f
            val strokeLineCap = parseStrokeCap(statement.valueArguments, "strokeLineCap")
            val strokeLineJoin = parseStrokeJoin(statement.valueArguments, "strokeLineJoin")
            val strokeLineMiter = parseValueArgument(statement.valueArguments, "strokeLineMiter") ?: 4.0f
            val pathFillType = parsePathFillType(statement.valueArguments, "pathFillType")

            val pathLambda = statement.lambdaArguments.firstOrNull()?.getLambdaExpression()
            val pathBody = pathLambda?.bodyExpression

            val pathInstructions = mutableListOf<PathNode>()
            pathBody?.statements?.forEach { stmt ->
                if (stmt is KtCallExpression) {
                    val args = stmt.valueArguments.mapNotNull { arg ->
                        arg.getArgumentExpression()?.text?.toFloatOrNull()
                    }
                    when (stmt.calleeExpression?.text) {
                        "moveTo" -> if (args.size == 2) pathInstructions.add(PathNode.MoveTo(args[0], args[1]))
                        "lineTo" -> if (args.size == 2) pathInstructions.add(PathNode.LineTo(args[0], args[1]))
                        "close" -> pathInstructions.add(PathNode.Close)
                    }
                }
            }

            pathsData.add(
                PathData(
                    fillColor = fillColor,
                    fillAlpha = fillAlpha,
                    strokeColor = strokeColor,
                    strokeAlpha = strokeAlpha,
                    strokeLineWidth = strokeLineWidth,
                    strokeLineCap = strokeLineCap,
                    strokeLineJoin = strokeLineJoin,
                    strokeLineMiter = strokeLineMiter,
                    pathFillType = pathFillType,
                    instructions = pathInstructions,
                ),
            )
        }
    }

    return pathsData
}

private fun parseColor(arguments: List<ValueArgument>, argumentName: String): SolidColor? {
    val arg = arguments.find { it.getArgumentName()?.asName?.identifier == argumentName }
    val colorCall = arg?.getArgumentExpression() as? KtCallExpression
    val colorArg = colorCall?.valueArguments?.firstOrNull()?.getArgumentExpression() ?: return null
    val colorValue = colorArg.text.removePrefix("Color(0x").removeSuffix(")").toLongOrNull(16) ?: return null
    return SolidColor(Color(colorValue.toInt()))
}

private fun parseValueArgument(arguments: List<ValueArgument>, argumentName: String): Float? {
    val arg = arguments.find { it.getArgumentName()?.asName?.identifier == argumentName }
    return arg?.getArgumentExpression()?.text?.toFloatOrNull()
}

private fun parseStrokeCap(arguments: List<ValueArgument>, argumentName: String): StrokeCap {
    val arg = arguments.find { it.getArgumentName()?.asName?.identifier == argumentName }
    return when (arg?.getArgumentExpression()?.text) {
        "StrokeCap.Butt" -> StrokeCap.Butt
        "StrokeCap.Round" -> StrokeCap.Round
        "StrokeCap.Square" -> StrokeCap.Square
        else -> StrokeCap.Butt
    }
}

private fun parseStrokeJoin(arguments: List<ValueArgument>, argumentName: String): StrokeJoin {
    val arg = arguments.find { it.getArgumentName()?.asName?.identifier == argumentName }
    return when (arg?.getArgumentExpression()?.text) {
        "StrokeJoin.Miter" -> StrokeJoin.Miter
        "StrokeJoin.Round" -> StrokeJoin.Round
        "StrokeJoin.Bevel" -> StrokeJoin.Bevel
        else -> StrokeJoin.Miter
    }
}

private fun parsePathFillType(arguments: List<ValueArgument>, argumentName: String): PathFillType {
    val arg = arguments.find { it.getArgumentName()?.asName?.identifier == argumentName }
    return when (arg?.getArgumentExpression()?.text) {
        "PathFillType.NonZero" -> PathFillType.NonZero
        "PathFillType.EvenOdd" -> PathFillType.EvenOdd
        else -> PathFillType.NonZero
    }
}

data class PathData(
    val fillColor: SolidColor?,
    val fillAlpha: Float,
    val strokeColor: SolidColor?,
    val strokeAlpha: Float,
    val strokeLineWidth: Float,
    val strokeLineCap: StrokeCap,
    val strokeLineJoin: StrokeJoin,
    val strokeLineMiter: Float,
    val pathFillType: PathFillType,
    val instructions: List<PathNode>,
)
