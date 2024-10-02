package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.IconWithShorthandColor: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "IconWithShorthandColor",
        defaultWidth = 48.dp,
        defaultHeight = 48.dp,
        viewportWidth = 512f,
        viewportHeight = 512f
    ).apply {
        group {
            path(fill = SolidColor(Color(0xFFD80027))) {
                moveTo(0f, 0f)
                horizontalLineToRelative(512f)
                verticalLineToRelative(167f)
                lineToRelative(-23.2f, 89.7f)
                lineTo(512f, 345f)
                verticalLineToRelative(167f)
                horizontalLineTo(0f)
                verticalLineTo(345f)
                lineToRelative(29.4f, -89f)
                lineTo(0f, 167f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEEEEEE))) {
                moveTo(0f, 167f)
                horizontalLineToRelative(512f)
                verticalLineToRelative(178f)
                horizontalLineTo(0f)
                close()
            }
        }
    }.build()
}
