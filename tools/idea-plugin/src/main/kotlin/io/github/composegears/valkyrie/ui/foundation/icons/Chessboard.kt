package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Chessboard: ImageVector
    get() {
        if (_Chessboard != null) {
            return _Chessboard!!
        }
        _Chessboard = ImageVector.Builder(
            name = "Chessboard",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(2.5f, 2.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(2.5f, 10.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(6.5f, 6.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(10.5f, 2.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(10.5f, 10.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(-3f)
                close()
            }
        }.build()

        return _Chessboard!!
    }

@Suppress("ObjectPropertyName")
private var _Chessboard: ImageVector? = null
