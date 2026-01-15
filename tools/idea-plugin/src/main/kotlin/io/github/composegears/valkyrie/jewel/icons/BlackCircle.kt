package io.github.composegears.valkyrie.jewel.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IntelliJIcons.BlackCircle: ImageVector
    get() {
        if (_BlackCircle != null) {
            return _BlackCircle!!
        }
        _BlackCircle = ImageVector.Builder(
            name = "BlackCircle",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1f,
            ) {
                moveTo(8f, 2.5f)
                lineTo(8f, 2.5f)
                arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13.5f, 8f)
                lineTo(13.5f, 8f)
                arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 13.5f)
                lineTo(8f, 13.5f)
                arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.5f, 8f)
                lineTo(2.5f, 8f)
                arcTo(5.5f, 5.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 2.5f)
                close()
            }
        }.build()

        return _BlackCircle!!
    }

@Suppress("ObjectPropertyName")
private var _BlackCircle: ImageVector? = null
