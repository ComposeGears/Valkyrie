package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Backspace: ImageVector
    get() {
        if (_Backspace != null) {
            return _Backspace!!
        }
        _Backspace = ImageVector.Builder(
            name = "AutoMirrored.Filled.Backspace",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirror = true,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                strokeLineWidth = 1f,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
            ) {
                moveTo(22.0f, 3.0f)
                lineTo(7.0f, 3.0f)
                curveToRelative(-0.69f, 0.0f, -1.23f, 0.35f, -1.59f, 0.88f)
                lineTo(0.0f, 12.0f)
                lineToRelative(5.41f, 8.11f)
                curveToRelative(0.36f, 0.53f, 0.9f, 0.89f, 1.59f, 0.89f)
                horizontalLineToRelative(15.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(24.0f, 5.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
                moveTo(19.0f, 15.59f)
                lineTo(17.59f, 17.0f)
                lineTo(14.0f, 13.41f)
                lineTo(10.41f, 17.0f)
                lineTo(9.0f, 15.59f)
                lineTo(12.59f, 12.0f)
                lineTo(9.0f, 8.41f)
                lineTo(10.41f, 7.0f)
                lineTo(14.0f, 10.59f)
                lineTo(17.59f, 7.0f)
                lineTo(19.0f, 8.41f)
                lineTo(15.41f, 12.0f)
                lineTo(19.0f, 15.59f)
                close()
            }
        }.build()

        return _Backspace!!
    }

@Suppress("ObjectPropertyName")
private var _Backspace: ImageVector? = null
