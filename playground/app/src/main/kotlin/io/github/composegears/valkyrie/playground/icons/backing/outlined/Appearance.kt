package io.github.composegears.valkyrie.playground.icons.backing.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.backing.BackingIcons

val BackingIcons.Outlined.Appearance: ImageVector
    get() {
        if (_Appearance != null) {
            return _Appearance!!
        }
        _Appearance = ImageVector.Builder(
            name = "Outlined.Appearance",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(11.625f, 16.406f)
                lineTo(1.125f, 10.406f)
                curveTo(1.01f, 10.341f, 0.914f, 10.246f, 0.848f, 10.132f)
                curveTo(0.781f, 10.017f, 0.746f, 9.887f, 0.746f, 9.755f)
                curveTo(0.746f, 9.622f, 0.781f, 9.492f, 0.848f, 9.378f)
                curveTo(0.914f, 9.264f, 1.01f, 9.169f, 1.125f, 9.103f)
                lineTo(11.625f, 3.103f)
                curveTo(11.738f, 3.038f, 11.866f, 3.004f, 11.997f, 3.004f)
                curveTo(12.128f, 3.004f, 12.256f, 3.038f, 12.369f, 3.103f)
                lineTo(22.869f, 9.103f)
                curveTo(22.984f, 9.169f, 23.08f, 9.264f, 23.146f, 9.378f)
                curveTo(23.213f, 9.492f, 23.248f, 9.622f, 23.248f, 9.755f)
                curveTo(23.248f, 9.887f, 23.213f, 10.017f, 23.146f, 10.132f)
                curveTo(23.08f, 10.246f, 22.984f, 10.341f, 22.869f, 10.406f)
                lineTo(12.369f, 16.406f)
                curveTo(12.256f, 16.471f, 12.128f, 16.505f, 11.997f, 16.505f)
                curveTo(11.866f, 16.505f, 11.738f, 16.471f, 11.625f, 16.406f)
                close()
            }
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(12f, 18.637f)
                lineTo(22.125f, 12.849f)
                verticalLineTo(12.854f)
                curveTo(22.48f, 12.651f, 22.946f, 12.778f, 23.149f, 13.133f)
                curveTo(23.352f, 13.488f, 23.224f, 13.954f, 22.869f, 14.157f)
                lineTo(12.37f, 20.157f)
                curveTo(12.143f, 20.287f, 11.852f, 20.287f, 11.625f, 20.157f)
                lineTo(1.125f, 14.157f)
                curveTo(0.769f, 13.953f, 0.642f, 13.484f, 0.846f, 13.128f)
                curveTo(1.05f, 12.772f, 1.519f, 12.645f, 1.875f, 12.849f)
                lineTo(12f, 18.637f)
                close()
            }
        }.build()

        return _Appearance!!
    }

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
private var _Appearance: ImageVector? = null
