package io.github.composegears.valkyrie.psi.imagevector.parser

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.psi.extension.childOfType
import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.common.extractPathFillType
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeCap
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeJoin
import io.github.composegears.valkyrie.psi.imagevector.common.parseFill
import io.github.composegears.valkyrie.psi.imagevector.common.parseFloatArg
import io.github.composegears.valkyrie.psi.imagevector.common.parsePathNodes
import io.github.composegears.valkyrie.psi.imagevector.common.parseStroke
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

internal object RegularImageVectorPsiParser {

    fun parse(ktFile: KtFile): IrImageVector? {
        val property = ktFile.childOfType<KtProperty>() ?: return null

        val blockBody = property.getter?.bodyBlockExpression
            ?: property.delegateExpression?.childrenOfType<KtBlockExpression>()?.firstOrNull()
            ?: return null

        val ktImageVector = blockBody.parseImageVectorParams() ?: return null

        return IrImageVector(
            name = ktImageVector.name.ifEmpty { property.name.orEmpty() },
            defaultWidth = ktImageVector.defaultWidth,
            defaultHeight = ktImageVector.defaultHeight,
            viewportWidth = ktImageVector.viewportWidth,
            viewportHeight = ktImageVector.viewportHeight,
            vectorNodes = blockBody.parseApplyBlock(),
        )
    }

    private fun KtBlockExpression.parseImageVectorParams(): IrImageVector? {
        val imageVectorBuilderCall = childrenOfType<KtCallExpression>().firstOrNull {
            it.calleeExpression?.text == "Builder"
        } ?: return null

        var name = ""
        var defaultWidth = 0f
        var defaultHeight = 0f
        var viewportWidth = 0f
        var viewportHeight = 0f

        imageVectorBuilderCall.valueArguments
            .forEach { arg ->
                val argName = arg.getArgumentName()?.asName?.identifier
                val argValue = arg.getArgumentExpression()?.text

                when (argName) {
                    "name" -> name = argValue?.removeSurrounding("\"").orEmpty()
                    "defaultWidth" -> defaultWidth = parseValue(argValue)
                    "defaultHeight" -> defaultHeight = parseValue(argValue)
                    "viewportWidth" -> viewportWidth = parseValue(argValue)
                    "viewportHeight" -> viewportHeight = parseValue(argValue)
                }
            }

        return IrImageVector(
            name = name,
            defaultWidth = defaultWidth,
            defaultHeight = defaultHeight,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            vectorNodes = emptyList(),
        )
    }

    private fun parseValue(value: String?): Float {
        if (value == null) return 0f

        return value
            .replace("dp", "")
            .replace("f", "")
            .toFloatOrNull() ?: 0f
    }

    private fun KtBlockExpression.parseApplyBlock(): List<IrVectorNode> {
        val vectorNodes = mutableListOf<IrVectorNode>()

        val applyFunctionCall = childrenOfType<KtCallExpression>()
            .firstOrNull { it.calleeExpression?.text == "apply" }

        val lambdaExpression = applyFunctionCall?.lambdaArguments?.firstOrNull()?.getLambdaExpression()
        val applyBlock = lambdaExpression?.bodyExpression ?: return vectorNodes

        applyBlock.statements.filterIsInstance<KtCallExpression>().forEach { expression ->
            if (expression.calleeExpression?.text == "path") {
                vectorNodes += expression.parsePath()
            }
            if (expression.calleeExpression?.text == "group") {
                val groupLambda = expression.lambdaArguments.firstOrNull()?.getLambdaExpression()
                val groupBlock = groupLambda?.bodyExpression

                vectorNodes += IrVectorNode.IrGroup(
                    nodes = groupBlock?.statements
                        ?.filterIsInstance<KtCallExpression>()
                        ?.map { it.parsePath() }
                        .orEmpty(),
                )
            }
        }

        return vectorNodes
    }

    private fun KtCallExpression.parsePath(): IrVectorNode.IrPath {
        val pathLambda = lambdaArguments.firstOrNull()?.getLambdaExpression()
        val pathBody = pathLambda?.bodyExpression

        return IrVectorNode.IrPath(
            fill = parseFill(),
            fillAlpha = parseFloatArg("fillAlpha") ?: 1f,
            stroke = parseStroke(),
            strokeAlpha = parseFloatArg("strokeAlpha") ?: 1f,
            strokeLineWidth = parseFloatArg("strokeLineWidth") ?: 0f,
            strokeLineCap = extractStrokeCap(),
            strokeLineJoin = extractStrokeJoin(),
            strokeLineMiter = parseFloatArg("strokeLineMiter") ?: 4f,
            pathFillType = extractPathFillType(),
            nodes = pathBody?.parsePathNodes().orEmpty(),
        )
    }
}
