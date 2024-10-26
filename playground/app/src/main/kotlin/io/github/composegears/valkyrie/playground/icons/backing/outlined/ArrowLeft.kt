package io.github.composegears.valkyrie.playground.icons.backing.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.backing.BackingIcons

val BackingIcons.Outlined.ArrowLeft: ImageVector
    get() {
        if (_ArrowLeft != null) {
            return _ArrowLeft!!
        }
        _ArrowLeft = ImageVector.Builder(
            name = "Outlined.ArrowLeft",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(21f, 12.313f)
                curveTo(21f, 11.76f, 20.552f, 11.313f, 20f, 11.313f)
                horizontalLineTo(6.548f)
                lineTo(12.477f, 5.384f)
                curveTo(12.868f, 4.992f, 12.867f, 4.357f, 12.474f, 3.967f)
                lineTo(12.207f, 3.702f)
                curveTo(11.816f, 3.314f, 11.185f, 3.315f, 10.795f, 3.705f)
                lineTo(2f, 12.5f)
                lineTo(10.793f, 21.293f)
                curveTo(11.183f, 21.683f, 11.817f, 21.683f, 12.207f, 21.293f)
                lineTo(12.468f, 21.032f)
                curveTo(12.858f, 20.642f, 12.858f, 20.009f, 12.469f, 19.618f)
                lineTo(6.548f, 13.688f)
                horizontalLineTo(20f)
                curveTo(20.552f, 13.688f, 21f, 13.24f, 21f, 12.688f)
                verticalLineTo(12.313f)
                close()
            }
        }.build()

        return _ArrowLeft!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowLeft: ImageVector? = null
