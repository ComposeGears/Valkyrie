package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.AllGroupParams: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "AllGroupParams",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        group(
            name = "group",
            rotate = 15f,
            pivotX = 10f,
            pivotY = 10f,
            scaleX = 0.8f,
            scaleY = 0.8f,
            translationX = 6f,
            translationY = 1f
        ) {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 0.3f
            ) {
                moveTo(15.67f, 4f)
                horizontalLineTo(14f)
                verticalLineTo(2f)
                horizontalLineToRelative(-4f)
                verticalLineToRelative(2f)
                horizontalLineTo(8.33f)
                curveTo(7.6f, 4f, 7f, 4.6f, 7f, 5.33f)
                verticalLineTo(9f)
                horizontalLineToRelative(4.93f)
                lineTo(13f, 7f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(4f)
                verticalLineTo(5.33f)
                curveTo(17f, 4.6f, 16.4f, 4f, 15.67f, 4f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(13f, 12.5f)
                horizontalLineToRelative(2f)
                lineTo(11f, 20f)
                verticalLineToRelative(-5.5f)
                horizontalLineTo(9f)
                lineTo(11.93f, 9f)
                horizontalLineTo(7f)
                verticalLineToRelative(11.67f)
                curveTo(7f, 21.4f, 7.6f, 22f, 8.33f, 22f)
                horizontalLineToRelative(7.33f)
                curveToRelative(0.74f, 0f, 1.34f, -0.6f, 1.34f, -1.33f)
                verticalLineTo(9f)
                horizontalLineToRelative(-4f)
                verticalLineToRelative(3.5f)
                close()
            }
        }
    }.build()
}
