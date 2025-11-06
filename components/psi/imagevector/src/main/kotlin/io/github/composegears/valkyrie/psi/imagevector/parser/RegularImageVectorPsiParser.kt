package io.github.composegears.valkyrie.psi.imagevector.parser

import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.common.autoMirror
import io.github.composegears.valkyrie.psi.imagevector.common.builderExpression
import io.github.composegears.valkyrie.psi.imagevector.common.defaultHeight
import io.github.composegears.valkyrie.psi.imagevector.common.defaultWidth
import io.github.composegears.valkyrie.psi.imagevector.common.extractPathFillType
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeCap
import io.github.composegears.valkyrie.psi.imagevector.common.extractStrokeJoin
import io.github.composegears.valkyrie.psi.imagevector.common.name
import io.github.composegears.valkyrie.psi.imagevector.common.parseClipPath
import io.github.composegears.valkyrie.psi.imagevector.common.parseFill
import io.github.composegears.valkyrie.psi.imagevector.common.parseFloatArg
import io.github.composegears.valkyrie.psi.imagevector.common.parsePath
import io.github.composegears.valkyrie.psi.imagevector.common.parseStringArg
import io.github.composegears.valkyrie.psi.imagevector.common.parseStroke
import io.github.composegears.valkyrie.psi.imagevector.common.viewportHeight
import io.github.composegears.valkyrie.psi.imagevector.common.viewportWidth
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

internal object RegularImageVectorPsiParser {

    fun parse(ktFile: KtFile): IrImageVector? {
        val property = ktFile.childrenOfType<KtProperty>()
            .firstOrNull { it.typeReference?.text == "ImageVector" }
            ?: return null

        val blockBody = property.getter?.bodyBlockExpression
            ?: property.delegateExpression?.childrenOfType<KtBlockExpression>()?.firstOrNull()
            ?: return null

        val builder = blockBody.builderExpression() ?: return null

        return IrImageVector(
            name = builder.name().ifEmpty { property.name.orEmpty() },
            defaultWidth = builder.defaultWidth(0f),
            defaultHeight = builder.defaultHeight(0f),
            viewportWidth = builder.viewportWidth(0f),
            viewportHeight = builder.viewportHeight(0f),
            autoMirror = builder.autoMirror(),
            nodes = blockBody.parseApplyBlock(),
        )
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
