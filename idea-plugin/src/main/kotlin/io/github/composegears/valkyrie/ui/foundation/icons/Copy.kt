package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ValkyrieIcons.Copy: ImageVector
    get() {
        if (_Copy != null) {
            return _Copy!!
        }
        _Copy = ImageVector.Builder(
            name = "Copy",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(4f, 3.5f)
                lineTo(10f, 3.5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.5f, 5f)
                lineTo(11.5f, 12f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10f, 13.5f)
                lineTo(4f, 13.5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.5f, 12f)
                lineTo(2.5f, 5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 3.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(5.5f, 6f)
                lineTo(8.5f, 6f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 6.5f)
                lineTo(9f, 6.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.5f, 7f)
                lineTo(5.5f, 7f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 6.5f)
                lineTo(5f, 6.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.5f, 6f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(5.5f, 8f)
                lineTo(8.5f, 8f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 8.5f)
                lineTo(9f, 8.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.5f, 9f)
                lineTo(5.5f, 9f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 8.5f)
                lineTo(5f, 8.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.5f, 8f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(5.5f, 10f)
                lineTo(8.5f, 10f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 10.5f)
                lineTo(9f, 10.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.5f, 11f)
                lineTo(5.5f, 11f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 10.5f)
                lineTo(5f, 10.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5.5f, 10f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6C707E)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(11.002f, 2f)
                horizontalLineTo(11.6f)
                curveTo(12.373f, 2f, 13f, 2.627f, 13f, 3.4f)
                verticalLineTo(3.911f)
                curveTo(13.001f, 3.94f, 13.002f, 3.97f, 13.002f, 4f)
                verticalLineTo(11.548f)
                curveTo(13.606f, 11.112f, 14f, 10.402f, 14f, 9.6f)
                verticalLineTo(3.4f)
                curveTo(14f, 2.075f, 12.925f, 1f, 11.6f, 1f)
                horizontalLineTo(6.4f)
                curveTo(5.597f, 1f, 4.886f, 1.394f, 4.45f, 2f)
                horizontalLineTo(6.4f)
                horizontalLineTo(11.002f)
                close()
            }
        }.build()

        return _Copy!!
    }

@Suppress("ObjectPropertyName")
private var _Copy: ImageVector? = null
