package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.SimpleLogo: ImageVector
    get() {
        if (_SimpleLogo != null) {
            return _SimpleLogo!!
        }
        _SimpleLogo = ImageVector.Builder(
            name = "SimpleLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Gray)) {
                moveTo(12f, 0f)
                curveTo(8.688f, 0f, 6f, 2.688f, 6f, 6f)
                reflectiveCurveToRelative(2.688f, 6f, 6f, 6f)
                curveToRelative(4.64f, -0.001f, 7.526f, 5.039f, 5.176f, 9.04f)
                horizontalLineToRelative(1.68f)
                arcTo(7.507f, 7.507f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 10.5f)
                arcTo(4.502f, 4.502f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.5f, 6f)
                curveToRelative(0f, -2.484f, 2.016f, -4.5f, 4.5f, -4.5f)
                reflectiveCurveToRelative(4.5f, 2.016f, 4.5f, 4.5f)
                lineTo(18f, 6f)
                curveToRelative(0f, -3.312f, -2.688f, -6f, -6f, -6f)
                close()
                moveTo(12f, 3f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 6f)
                curveToRelative(4f, 0f, 4f, -6f, 0f, -6f)
                close()
                moveTo(12f, 4.5f)
                arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13.5f, 6f)
                verticalLineToRelative(0.002f)
                curveToRelative(-0.002f, 1.336f, -1.617f, 2.003f, -2.561f, 1.058f)
                curveTo(9.995f, 6.115f, 10.664f, 4.5f, 12f, 4.5f)
                close()
                moveTo(7.5f, 15f)
                verticalLineToRelative(1.5f)
                lineTo(9f, 16.5f)
                verticalLineToRelative(6f)
                lineTo(4.5f, 22.5f)
                lineTo(4.5f, 24f)
                horizontalLineToRelative(15f)
                verticalLineToRelative(-1.5f)
                lineTo(15f, 22.5f)
                verticalLineToRelative(-6f)
                horizontalLineToRelative(1.5f)
                lineTo(16.5f, 15f)
                close()
                moveTo(10.5f, 16.5f)
                horizontalLineToRelative(3f)
                verticalLineToRelative(6f)
                horizontalLineToRelative(-3f)
                close()
                moveTo(4.5f, 17.97f)
                curveToRelative(0f, 1.09f, 0.216f, 2.109f, 0.644f, 3.069f)
                horizontalLineToRelative(1.684f)
                arcTo(5.957f, 5.957f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 17.97f)
                close()
            }
        }.build()

        return _SimpleLogo!!
    }

@Suppress("ObjectPropertyName")
private var _SimpleLogo: ImageVector? = null
