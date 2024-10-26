package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.FitContent: ImageVector
    get() {
        if (_FitContent != null) {
            return _FitContent!!
        }
        _FitContent = ImageVector.Builder(
            name = "FitContent",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(11.5f, 7f)
                lineTo(11.5f, 7f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 7.5f)
                lineTo(12f, 10.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.5f, 11f)
                lineTo(11.5f, 11f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11f, 10.5f)
                lineTo(11f, 7.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.5f, 7f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(12f, 10.5f)
                lineTo(12f, 10.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 11.5f, 11f)
                lineTo(8.5f, 11f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 10.5f)
                lineTo(8f, 10.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.5f, 10f)
                lineTo(11.5f, 10f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 10.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(4.5f, 5f)
                lineTo(4.5f, 5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 5.5f)
                lineTo(5f, 8.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.5f, 9f)
                lineTo(4.5f, 9f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 8.5f)
                lineTo(4f, 5.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.5f, 5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(8f, 5.5f)
                lineTo(8f, 5.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.5f, 6f)
                lineTo(4.5f, 6f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 5.5f)
                lineTo(4f, 5.5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4.5f, 5f)
                lineTo(7.5f, 5f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 5.5f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(3f, 2.5f)
                lineTo(13f, 2.5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.5f, 4f)
                lineTo(14.5f, 12f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13f, 13.5f)
                lineTo(3f, 13.5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1.5f, 12f)
                lineTo(1.5f, 4f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 2.5f)
                close()
            }
        }.build()

        return _FitContent!!
    }

@Suppress("ObjectPropertyName")
private var _FitContent: ImageVector? = null
