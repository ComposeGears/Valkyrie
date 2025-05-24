package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.KotlinLogo: ImageVector
    get() {
        if (_KotlinLogo != null) {
            return _KotlinLogo!!
        }
        _KotlinLogo = ImageVector.Builder(
            name = "KotlinLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.003f to Color(0xFFE44857),
                        0.469f to Color(0xFFC711E1),
                        1f to Color(0xFF7F52FF),
                    ),
                    start = Offset(24f, 0f),
                    end = Offset(0f, 24f),
                ),
            ) {
                moveTo(24f, 24f)
                lineTo(0f, 24f)
                lineTo(0f, 0f)
                lineTo(24f, 0f)
                lineTo(12f, 12f)
                close()
                moveTo(24f, 24f)
            }
        }.build()

        return _KotlinLogo!!
    }

@Suppress("ObjectPropertyName")
private var _KotlinLogo: ImageVector? = null
