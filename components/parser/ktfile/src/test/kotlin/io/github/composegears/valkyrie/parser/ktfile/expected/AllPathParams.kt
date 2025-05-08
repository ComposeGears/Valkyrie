package io.github.composegears.valkyrie.parser.ktfile.expected

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ExpectedAllPathParams = ImageVector.Builder(
    name = "AllPathParams",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 18f,
    viewportHeight = 18f,
).apply {
    path(
        name = "path_name",
        fill = SolidColor(Color(0xFF232F34)),
        fillAlpha = 0.5f,
        stroke = SolidColor(Color(0xFF232F34)),
        strokeAlpha = 0.5f,
        strokeLineWidth = 1f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 3f,
        pathFillType = PathFillType.EvenOdd,
    ) {
        moveTo(6.75f, 12.127f)
        moveToRelative(1f, -2f)
        lineTo(3.623f, 9f)
        lineToRelative(-5.49f, 1.3f)
        horizontalLineTo(1.4f)
        horizontalLineToRelative(-6f)
        verticalLineTo(95.06f)
        verticalLineToRelative(10.0f)
        curveTo(11.76f, 1.714f, 11.755f, 1.715f, 11.768f, 1.714f)
        curveToRelative(3.236f, 0.224f, 7.033f, 0f, 7.033f, 0f)
        reflectiveCurveTo(11.957f, 41.979f, 0.013f, 44.716f)
        reflectiveCurveToRelative(6.586f, 6.584f, 9.823f, 6.805f)
        quadTo(20.306f, 6.477f, 20.306f, 6.508f)
        quadToRelative(0.04f, -0.3f, 0.06f, -0.61f)
        reflectiveQuadTo(5f, 3f)
        reflectiveQuadToRelative(4f, 1f)
        arcTo(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 5.092f)
        arcTo(0.75f, 0.75f, 0f, true, false, 3f, 5.092f)
        arcToRelative(0.763f, 0.763f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.55f, -0.066f)
        arcToRelative(0.763f, 0.763f, 0f, false, true, -0.55f, -0.066f)
        close()
    }
}.build()
