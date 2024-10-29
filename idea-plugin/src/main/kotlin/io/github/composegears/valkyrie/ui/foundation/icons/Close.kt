package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Close: ImageVector
    get() {
        if (_Close != null) {
            return _Close!!
        }
        _Close = ImageVector.Builder(
            name = "Close",
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
                moveTo(2.5f, 13.5f)
                lineTo(13.5f, 2.5f)
                moveTo(13.5f, 13.5f)
                lineTo(2.5f, 2.5f)
            }
        }.build()

        return _Close!!
    }

@Suppress("ObjectPropertyName")
private var _Close: ImageVector? = null
