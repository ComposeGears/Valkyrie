package io.github.composegears.valkyrie.psi.imagevector.parser

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.psi.extension.childOfType
import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.common.extractPathFillType
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeCap
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeJoin
import io.github.composegears.valkyrie.psi.imagevector.common.parseClipPath
import io.github.composegears.valkyrie.psi.imagevector.common.parseFill
import io.github.composegears.valkyrie.psi.imagevector.common.parseFloatArg
import io.github.composegears.valkyrie.psi.imagevector.common.parsePath
import io.github.composegears.valkyrie.psi.imagevector.common.parseStringArg
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
            nodes = blockBody.parseApplyBlock(),
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
            nodes = emptyList(),
        )
    }

    private fun parseValue(value: String?): Float {
        if (value == null) return 0f

        return value
            .removeSuffix(".dp")
            .removeSuffix("f")
            .removeSuffix("F")
            .toFloatOrNull() ?: 0f
    }

    private fun KtBlockExpression.parseApplyBlock(): List<IrVectorNode> {
        val vectorNodes = mutableListOf<IrVectorNode>()

        val applyFunctionCall = childrenOfType<KtCallExpression>()
            .firstOrNull { it.calleeExpression?.text == "apply" }

        val lambdaExpression = applyFunctionCall?.lambdaArguments?.firstOrNull()?.getLambdaExpression()
        val applyBlock = lambdaExpression?.bodyExpression ?: return vectorNodes

        applyBlock.statements.filterIsInstance<KtCallExpression>().forEach { expression ->
            val node = expression.parseVectorNode()
            if (node != null) {
                vectorNodes += node
            }
        }

        return vectorNodes
    }

    private fun KtCallExpression.parseVectorNode(): IrVectorNode? {
        var node: IrVectorNode? = null
        if (calleeExpression?.text == "path") {
            node = parsePath()
        }
        if (calleeExpression?.text == "group") {
            node = parseGroup()
        }
        return node
    }

    private fun KtCallExpression.parseGroup(): IrVectorNode.IrGroup {
        val groupLambda = lambdaArguments.firstOrNull()?.getLambdaExpression()
        val groupBlock = groupLambda?.bodyExpression

        return IrVectorNode.IrGroup(
            name = parseStringArg("name").orEmpty(),
            rotate = parseFloatArg("rotate") ?: 0f,
            pivotX = parseFloatArg("pivotX") ?: 0f,
            pivotY = parseFloatArg("pivotY") ?: 0f,
            scaleX = parseFloatArg("scaleX") ?: 1f,
            scaleY = parseFloatArg("scaleY") ?: 1f,
            translationX = parseFloatArg("translationX") ?: 0f,
            translationY = parseFloatArg("translationY") ?: 0f,
            clipPathData = parseClipPath().toMutableList(),
            nodes = groupBlock?.statements
                ?.filterIsInstance<KtCallExpression>()
                ?.mapNotNull { it.parseVectorNode() }
                .orEmpty()
                .toMutableList(),
        )
    }

    private fun KtCallExpression.parsePath(): IrVectorNode.IrPath {
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
}
