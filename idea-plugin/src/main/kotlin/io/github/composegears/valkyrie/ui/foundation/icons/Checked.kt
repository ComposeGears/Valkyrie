package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Checked: ImageVector
    get() {
        if (_Checked != null) {
            return _Checked!!
        }
        _Checked = ImageVector.Builder(
            name = "Checked",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(2.5f, 8.25f)
                lineTo(6f, 11.75f)
                lineTo(13.5f, 4.25f)
            }
        }.build()

        return _Checked!!
    }

@Suppress("ObjectPropertyName")
private var _Checked: ImageVector? = null
