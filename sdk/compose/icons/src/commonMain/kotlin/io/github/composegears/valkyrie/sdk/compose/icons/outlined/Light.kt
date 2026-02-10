package io.github.composegears.valkyrie.sdk.compose.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Outlined.Light: ImageVector
    get() {
        if (_Light != null) {
            return _Light!!
        }
        _Light = ImageVector.Builder(
            name = "Outlined.Light",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(480f, 600f)
                quadToRelative(50f, 0f, 85f, -35f)
                reflectiveQuadToRelative(35f, -85f)
                quadToRelative(0f, -50f, -35f, -85f)
                reflectiveQuadToRelative(-85f, -35f)
                quadToRelative(-50f, 0f, -85f, 35f)
                reflectiveQuadToRelative(-35f, 85f)
                quadToRelative(0f, 50f, 35f, 85f)
                reflectiveQuadToRelative(85f, 35f)
                close()
                moveTo(480f, 640f)
                quadToRelative(-66.85f, 0f, -113.42f, -46.58f)
                quadTo(320f, 546.85f, 320f, 480f)
                reflectiveQuadToRelative(46.58f, -113.42f)
                quadTo(413.15f, 320f, 480f, 320f)
                reflectiveQuadToRelative(113.42f, 46.58f)
                quadTo(640f, 413.15f, 640f, 480f)
                reflectiveQuadToRelative(-46.58f, 113.42f)
                quadTo(546.85f, 640f, 480f, 640f)
                close()
                moveTo(200f, 500f)
                lineTo(60f, 500f)
                verticalLineToRelative(-40f)
                horizontalLineToRelative(140f)
                verticalLineToRelative(40f)
                close()
                moveTo(900f, 500f)
                lineTo(760f, 500f)
                verticalLineToRelative(-40f)
                horizontalLineToRelative(140f)
                verticalLineToRelative(40f)
                close()
                moveTo(460f, 200f)
                verticalLineToRelative(-140f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(140f)
                horizontalLineToRelative(-40f)
                close()
                moveTo(460f, 900f)
                verticalLineToRelative(-140f)
                horizontalLineToRelative(40f)
                verticalLineToRelative(140f)
                horizontalLineToRelative(-40f)
                close()
                moveTo(269.85f, 296.15f)
                lineToRelative(-86.39f, -83.92f)
                lineToRelative(27.77f, -29.77f)
                lineToRelative(84.46f, 85.39f)
                lineToRelative(-25.84f, 28.3f)
                close()
                moveTo(748.77f, 777.54f)
                lineTo(664.08f, 691.92f)
                lineTo(690.15f, 663.85f)
                lineTo(776.54f, 747.77f)
                lineTo(748.77f, 777.54f)
                close()
                moveTo(663.85f, 269.85f)
                lineTo(747.77f, 183.46f)
                lineTo(777.54f, 211.23f)
                lineTo(692.15f, 295.69f)
                lineTo(663.85f, 269.85f)
                close()
                moveTo(182.46f, 748.77f)
                lineToRelative(85.62f, -84.69f)
                lineToRelative(26.54f, 26.07f)
                lineToRelative(-83.16f, 87.16f)
                lineToRelative(-29f, -28.54f)
                close()
                moveTo(480f, 480f)
                close()
            }
        }.build()

        return _Light!!
    }

@Suppress("ObjectPropertyName")
private var _Light: ImageVector? = null
