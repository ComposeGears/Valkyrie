package io.github.composegears.valkyrie.psi.imagevector.parser

import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.common.autoMirror
import io.github.composegears.valkyrie.psi.imagevector.common.builderExpression
import io.github.composegears.valkyrie.psi.imagevector.common.defaultHeight
import io.github.composegears.valkyrie.psi.imagevector.common.defaultWidth
import io.github.composegears.valkyrie.psi.imagevector.common.extractPathFillType
import io.github.composegears.valkyrie.psi.imagevector.common.materialIconCall
import io.github.composegears.valkyrie.psi.imagevector.common.name
import io.github.composegears.valkyrie.psi.imagevector.common.parseFloatArg
import io.github.composegears.valkyrie.psi.imagevector.common.parsePath
import io.github.composegears.valkyrie.psi.imagevector.common.viewportHeight
import io.github.composegears.valkyrie.psi.imagevector.common.viewportWidth
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode.IrPath
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

internal object MaterialImageVectorPsiParser {

    fun parse(ktFile: KtFile): IrImageVector? {
        val property = ktFile.childrenOfType<KtProperty>()
            .firstOrNull { it.typeReference?.text == "ImageVector" }
            ?: return null

        val blockBody = property.getter?.bodyBlockExpression ?: return null

        val materialIconCall = blockBody.materialIconCall()

        if (materialIconCall != null) {
            return IrImageVector(
                name = materialIconCall.name(),
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                autoMirror = materialIconCall.autoMirror(),
                nodes = blockBody.parseMaterialPath(),
            )
        }

        val builder = blockBody.builderExpression() ?: return null

        return IrImageVector(
            name = builder.name().ifEmpty { property.name.orEmpty() },
            defaultWidth = builder.defaultWidth(0f),
            defaultHeight = builder.defaultHeight(0f),
            viewportWidth = builder.viewportWidth(0f),
            viewportHeight = builder.viewportHeight(0f),
            autoMirror = builder.autoMirror(),
            nodes = blockBody.parseMaterialPath(),
        )
    }

    private fun KtBlockExpression.parseMaterialPath(): List<IrVectorNode> {
        val materialPathCall = materialPathCall() ?: return emptyList()

        val pathLambda = materialPathCall.lambdaArguments.firstOrNull()?.getLambdaExpression()
        val pathBody = pathLambda?.bodyExpression ?: return emptyList()

        return listOf(
            IrPath(
                fill = IrFill.Color(IrColor("#FF000000")),
                fillAlpha = materialPathCall.parseFloatArg("fillAlpha") ?: 1f,
                strokeAlpha = materialPathCall.parseFloatArg("strokeAlpha") ?: 1f,
                strokeLineWidth = 1f,
                strokeLineJoin = IrStrokeLineJoin.Bevel,
                strokeLineMiter = 1f,
                pathFillType = materialPathCall.extractPathFillType(),
                paths = pathBody.parsePath(),
            ),
        )
    }
}

private fun KtBlockExpression.materialPathCall(): KtCallExpression? {
    val ktCallExpression = childrenOfType<KtCallExpression>().firstOrNull {
        it.calleeExpression?.text == "materialPath"
    } ?: return null

    return ktCallExpression
}
