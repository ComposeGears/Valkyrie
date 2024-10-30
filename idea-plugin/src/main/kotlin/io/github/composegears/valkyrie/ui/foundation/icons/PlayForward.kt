package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.PlayForward: ImageVector
    get() {
        if (_PlayForward != null) {
            return _PlayForward!!
        }
        _PlayForward = ImageVector.Builder(
            name = "PlayForward",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Round,
            ) {
                moveTo(6f, 12.5f)
                lineTo(10.5f, 8f)
                lineTo(6f, 3.5f)
            }
        }.build()

        return _PlayForward!!
    }

@Suppress("ObjectPropertyName")
private var _PlayForward: ImageVector? = null
