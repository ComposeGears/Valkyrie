package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.GoogleMaterialLogo: ImageVector
    get() {
        if (_GoogleMaterialLogo != null) {
            return _GoogleMaterialLogo!!
        }
        _GoogleMaterialLogo = ImageVector.Builder(
            name = "Colored.GoogleMaterialLogo",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(fill = SolidColor(Color(0xFFF29900))) {
                moveTo(13.5f, 2f)
                horizontalLineTo(8f)
                lineTo(1f, 13f)
                horizontalLineToRelative(5.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF1A73E8))) {
                moveTo(8f, 2f)
                horizontalLineToRelative(5f)
                verticalLineToRelative(11f)
                horizontalLineTo(8f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEA4335))) {
                moveTo(3.25f, 4.25f)
                moveToRelative(-2.25f, 0f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4.5f, 0f)
                arcToRelative(2.25f, 2.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4.5f, 0f)
            }
            path(fill = SolidColor(Color(0xFF0D652D))) {
                moveTo(13.33f, 10f)
                lineTo(13f, 13f)
                curveToRelative(-1.66f, 0f, -3f, -1.34f, -3f, -3f)
                reflectiveCurveToRelative(1.34f, -3f, 3f, -3f)
                lineToRelative(0.33f, 3f)
                close()
            }
            path(fill = SolidColor(Color(0xFF174EA6))) {
                moveTo(10.5f, 4.5f)
                arcTo(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13f, 2f)
                lineToRelative(0.45f, 2.5f)
                lineTo(13f, 7f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.5f, -2.5f)
                close()
            }
            path(fill = SolidColor(Color(0xFF1A73E8))) {
                moveTo(13f, 2f)
                arcToRelative(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 5f)
            }
            path(fill = SolidColor(Color(0xFF34A853))) {
                moveTo(13f, 7f)
                curveToRelative(1.66f, 0f, 3f, 1.34f, 3f, 3f)
                reflectiveCurveToRelative(-1.34f, 3f, -3f, 3f)
            }
        }.build()

        return _GoogleMaterialLogo!!
    }

@Suppress("ObjectPropertyName")
private var _GoogleMaterialLogo: ImageVector? = null
