package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ValkyrieIcons.Back: ImageVector
    get() {
        if (_Back != null) {
            return _Back!!
        }
        _Back = ImageVector.Builder(
            name = "Back",
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
                moveTo(10f, 12.5f)
                lineTo(5.5f, 8f)
                lineTo(10f, 3.5f)
            }
        }.build()

        return _Back!!
    }

@Suppress("ObjectPropertyName")
private var _Back: ImageVector? = null
