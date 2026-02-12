package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.common

import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import org.jetbrains.kotlin.psi.KtCallExpression

internal fun KtCallExpression.parsePathNode(): IrVectorNode.IrPath {
    val pathLambda = lambdaArguments.firstOrNull()?.getLambdaExpression()
    val pathBody = pathLambda?.bodyExpression

    return IrVectorNode.IrPath(
        name = parseStringArg("name").orEmpty(),
        fill = parseFill(),
        fillAlpha = parseFloatArg("fillAlpha") ?: 1f,
        stroke = parseStroke(),
        strokeAlpha = parseFloatArg("strokeAlpha") ?: 1f,
        strokeLineWidth = parseFloatArg("strokeLineWidth") ?: 0f,
        strokeLineCap = extractStrokeCap(),
        strokeLineJoin = extractStrokeJoin(),
        strokeLineMiter = parseFloatArg("strokeLineMiter") ?: 4f,
        pathFillType = extractPathFillType(),
        paths = pathBody?.parsePath().orEmpty(),
    )
}
