package io.github.composegears.valkyrie.compose.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Outlined.Dark: ImageVector
    get() {
        if (_Dark != null) {
            return _Dark!!
        }
        _Dark = ImageVector.Builder(
            name = "Outlined.Dark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(482.31f, 800f)
                quadToRelative(-133.34f, 0f, -226.67f, -93.33f)
                quadToRelative(-93.33f, -93.34f, -93.33f, -226.67f)
                quadToRelative(0f, -121.54f, 79.23f, -210.77f)
                reflectiveQuadToRelative(196.15f, -105.38f)
                quadToRelative(3.23f, 0f, 6.35f, 0.23f)
                quadToRelative(3.11f, 0.23f, 6.11f, 0.69f)
                quadToRelative(-20.23f, 28.23f, -32.03f, 62.81f)
                quadToRelative(-11.81f, 34.57f, -11.81f, 72.42f)
                quadToRelative(0f, 106.67f, 74.66f, 181.33f)
                quadTo(555.64f, 556f, 662.31f, 556f)
                quadToRelative(38.07f, 0f, 72.54f, -11.81f)
                quadToRelative(34.46f, -11.81f, 61.92f, -32.04f)
                quadToRelative(0.46f, 3f, 0.69f, 6.12f)
                quadToRelative(0.23f, 3.11f, 0.23f, 6.35f)
                quadToRelative(-15.38f, 116.92f, -104.61f, 196.15f)
                reflectiveQuadTo(482.31f, 800f)
                close()
                moveTo(482.31f, 760f)
                quadToRelative(88f, 0f, 158f, -48.5f)
                reflectiveQuadToRelative(102f, -126.5f)
                quadToRelative(-20f, 5f, -40f, 8f)
                reflectiveQuadToRelative(-40f, 3f)
                quadToRelative(-123f, 0f, -209.5f, -86.5f)
                reflectiveQuadTo(366.31f, 300f)
                quadToRelative(0f, -20f, 3f, -40f)
                reflectiveQuadToRelative(8f, -40f)
                quadToRelative(-78f, 32f, -126.5f, 102f)
                reflectiveQuadToRelative(-48.5f, 158f)
                quadToRelative(0f, 116f, 82f, 198f)
                reflectiveQuadToRelative(198f, 82f)
                close()
                moveTo(472.31f, 490f)
                close()
            }
        }.build()

        return _Dark!!
    }

@Suppress("ObjectPropertyName")
private var _Dark: ImageVector? = null
