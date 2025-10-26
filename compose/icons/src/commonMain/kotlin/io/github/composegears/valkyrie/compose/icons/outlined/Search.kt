package io.github.composegears.valkyrie.compose.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Outlined.Search: ImageVector
    get() {
        if (_Search != null) {
            return _Search!!
        }
        _Search = ImageVector.Builder(
            name = "Outlined.Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(779.38f, 806.15f)
                lineTo(528.92f, 555.69f)
                quadToRelative(-30f, 25.54f, -69f, 39.54f)
                reflectiveQuadToRelative(-78.38f, 14f)
                quadToRelative(-96.1f, 0f, -162.67f, -66.53f)
                quadToRelative(-66.56f, -66.53f, -66.56f, -162.57f)
                quadToRelative(0f, -96.05f, 66.53f, -162.71f)
                quadToRelative(66.53f, -66.65f, 162.57f, -66.65f)
                quadToRelative(96.05f, 0f, 162.71f, 66.56f)
                quadTo(610.77f, 283.9f, 610.77f, 380f)
                quadToRelative(0f, 41.69f, -14.77f, 80.69f)
                reflectiveQuadToRelative(-38.77f, 66.69f)
                lineToRelative(250.46f, 250.47f)
                lineToRelative(-28.31f, 28.3f)
                close()
                moveTo(381.54f, 569.23f)
                quadToRelative(79.61f, 0f, 134.42f, -54.81f)
                quadToRelative(54.81f, -54.8f, 54.81f, -134.42f)
                quadToRelative(0f, -79.62f, -54.81f, -134.42f)
                quadToRelative(-54.81f, -54.81f, -134.42f, -54.81f)
                quadToRelative(-79.62f, 0f, -134.42f, 54.81f)
                quadToRelative(-54.81f, 54.8f, -54.81f, 134.42f)
                quadToRelative(0f, 79.62f, 54.81f, 134.42f)
                quadToRelative(54.8f, 54.81f, 134.42f, 54.81f)
                close()
            }
        }.build()

        return _Search!!
    }

@Suppress("ObjectPropertyName")
private var _Search: ImageVector? = null
