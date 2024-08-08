package io.github.composegears.valkyrie.psi.imagevector

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.psi.extension.childOfType
import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.block.parseApplyBlock
import io.github.composegears.valkyrie.psi.imagevector.block.parseImageVectorParams
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtProperty

object ImageVectorPsiParser {

    fun parseToImageVector(ktFile: KtFile): ImageVector? {
        val imports = ktFile.childOfType<KtImportList>()
            ?.childrenOfType<KtImportDirective>()
            ?.mapNotNull { it.importedFqName?.asString() }

        // TODO: add logic to check if imports contain androidx.compose.ui.graphics.vector.ImageVector

        val property = ktFile.childOfType<KtProperty>() ?: return null
        val blockBody = property.getter?.bodyBlockExpression ?: return null

        val ktImageVector = blockBody.parseImageVectorParams() ?: return null
        val pathsData = blockBody.parseApplyBlock()

        return ImageVector.Builder(
            name = ktImageVector.name.ifEmpty { property.name.orEmpty() },
            defaultWidth = ktImageVector.defaultWidth.dp,
            defaultHeight = ktImageVector.defaultHeight.dp,
            viewportWidth = ktImageVector.viewportWidth,
            viewportHeight = ktImageVector.viewportHeight,
        ).apply {
            pathsData.forEach { pathData ->
                path(
                    fill = pathData.fillColor,
                    fillAlpha = pathData.fillAlpha,
                    stroke = pathData.strokeColor,
                    strokeAlpha = pathData.strokeAlpha,
                    strokeLineWidth = pathData.strokeLineWidth,
                    strokeLineCap = pathData.strokeLineCap,
                    strokeLineJoin = pathData.strokeLineJoin,
                    strokeLineMiter = pathData.strokeLineMiter,
                    pathFillType = pathData.pathFillType,
                ) {
                    pathData.instructions.forEach {
                        when (it) {
                            is PathNode.ArcTo -> arcTo(
                                horizontalEllipseRadius = it.horizontalEllipseRadius,
                                verticalEllipseRadius = it.verticalEllipseRadius,
                                theta = it.theta,
                                isMoreThanHalf = it.isMoreThanHalf,
                                isPositiveArc = it.isPositiveArc,
                                x1 = it.arcStartX,
                                y1 = it.arcStartY,
                            )
                            is PathNode.Close -> close()
                            is PathNode.CurveTo -> curveTo(
                                x1 = it.x1,
                                y1 = it.y1,
                                x2 = it.x2,
                                y2 = it.y2,
                                x3 = it.x3,
                                y3 = it.y3,
                            )
                            is PathNode.HorizontalTo -> horizontalLineTo(x = it.x)
                            is PathNode.LineTo -> lineTo(x = it.x, y = it.y)
                            is PathNode.MoveTo -> moveTo(x = it.x, y = it.y)
                            is PathNode.QuadTo -> quadTo(
                                x1 = it.x1,
                                y1 = it.y1,
                                x2 = it.x2,
                                y2 = it.y2,
                            )
                            is PathNode.ReflectiveCurveTo -> reflectiveCurveTo(
                                x1 = it.x1,
                                y1 = it.y1,
                                x2 = it.x2,
                                y2 = it.y2,
                            )
                            is PathNode.ReflectiveQuadTo -> reflectiveQuadTo(
                                x1 = it.x,
                                y1 = it.y,
                            )
                            is PathNode.RelativeArcTo -> arcToRelative(
                                a = it.horizontalEllipseRadius,
                                b = it.verticalEllipseRadius,
                                theta = it.theta,
                                isMoreThanHalf = it.isMoreThanHalf,
                                isPositiveArc = it.isPositiveArc,
                                dx1 = it.arcStartDx,
                                dy1 = it.arcStartDy,
                            )
                            is PathNode.RelativeCurveTo -> curveToRelative(
                                dx1 = it.dx1,
                                dy1 = it.dy1,
                                dx2 = it.dx2,
                                dy2 = it.dy2,
                                dx3 = it.dx3,
                                dy3 = it.dy3,
                            )
                            is PathNode.RelativeHorizontalTo -> horizontalLineToRelative(dx = it.dx)
                            is PathNode.RelativeLineTo -> lineToRelative(dx = it.dx, dy = it.dy)
                            is PathNode.RelativeMoveTo -> moveToRelative(dx = it.dx, dy = it.dy)
                            is PathNode.RelativeQuadTo -> quadToRelative(
                                dx1 = it.dx1,
                                dy1 = it.dy1,
                                dx2 = it.dx2,
                                dy2 = it.dy2,
                            )
                            is PathNode.RelativeReflectiveCurveTo -> reflectiveCurveToRelative(
                                dx1 = it.dx1,
                                dy1 = it.dy1,
                                dx2 = it.dx2,
                                dy2 = it.dy2,
                            )
                            is PathNode.RelativeReflectiveQuadTo -> reflectiveQuadToRelative(
                                dx1 = it.dx,
                                dy1 = it.dy,
                            )
                            is PathNode.RelativeVerticalTo -> verticalLineToRelative(dy = it.dy)
                            is PathNode.VerticalTo -> verticalLineTo(y = it.y)
                        }
                    }
                }
            }
        }.build()
    }
}
