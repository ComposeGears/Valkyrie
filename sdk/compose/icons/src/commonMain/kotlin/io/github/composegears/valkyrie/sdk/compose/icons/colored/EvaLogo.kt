package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.EvaLogo: ImageVector
    get() {
        if (_EvaLogo != null) {
            return _EvaLogo!!
        }
        _EvaLogo = ImageVector.Builder(
            name = "EvaLogo",
            defaultWidth = 22.dp,
            defaultHeight = 17.dp,
            viewportWidth = 22f,
            viewportHeight = 17f,
        ).apply {
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 0f,
                strokeLineWidth = 1f,
            ) {
                moveTo(1f, -4f)
                lineTo(21f, -4f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23f, -2f)
                lineTo(23f, 18f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 21f, 20f)
                lineTo(1f, 20f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -1f, 18f)
                lineTo(-1f, -2f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 1f, -4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFA8B4)),
                strokeLineWidth = 1f,
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(12f, 17f)
                curveTo(11.734f, 17f, 11.48f, 16.895f, 11.292f, 16.706f)
                lineTo(3.525f, 8.926f)
                curveTo(1.489f, 6.886f, 1.489f, 3.567f, 3.525f, 1.527f)
                curveTo(4.508f, 0.543f, 5.821f, 0f, 7.22f, 0f)
                curveTo(8.619f, 0f, 9.932f, 0.543f, 10.915f, 1.527f)
                lineTo(12f, 2.614f)
                lineTo(13.084f, 1.528f)
                curveTo(14.068f, 0.543f, 15.381f, 0f, 16.78f, 0f)
                curveTo(18.179f, 0f, 19.492f, 0.543f, 20.475f, 1.527f)
                curveTo(22.511f, 3.567f, 22.511f, 6.886f, 20.476f, 8.926f)
                lineTo(12.708f, 16.707f)
                curveTo(12.52f, 16.895f, 12.266f, 17f, 12f, 17f)
            }
            path(
                fill = SolidColor(Color(0xFFFF3D71)),
                strokeLineWidth = 1f,
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(5.22f, 2f)
                curveTo(4.356f, 2f, 3.546f, 2.334f, 2.94f, 2.941f)
                curveTo(1.682f, 4.201f, 1.682f, 6.252f, 2.941f, 7.514f)
                lineTo(10f, 14.585f)
                lineTo(17.06f, 7.514f)
                curveTo(18.319f, 6.252f, 18.319f, 4.201f, 17.06f, 2.941f)
                curveTo(15.848f, 1.726f, 13.712f, 1.728f, 12.5f, 2.941f)
                lineTo(10.708f, 4.736f)
                curveTo(10.332f, 5.113f, 9.668f, 5.113f, 9.292f, 4.736f)
                lineTo(7.5f, 2.94f)
                curveTo(6.894f, 2.334f, 6.085f, 2f, 5.22f, 2f)
                moveTo(10f, 17f)
                curveTo(9.735f, 17f, 9.48f, 16.895f, 9.293f, 16.706f)
                lineTo(1.525f, 8.926f)
                curveTo(-0.511f, 6.886f, -0.511f, 3.567f, 1.525f, 1.527f)
                curveTo(2.509f, 0.543f, 3.821f, 0f, 5.22f, 0f)
                curveTo(6.619f, 0f, 7.932f, 0.543f, 8.915f, 1.527f)
                lineTo(10f, 2.614f)
                lineTo(11.085f, 1.528f)
                curveTo(12.069f, 0.543f, 13.381f, 0f, 14.781f, 0f)
                curveTo(16.179f, 0f, 17.492f, 0.543f, 18.475f, 1.527f)
                curveTo(20.512f, 3.567f, 20.512f, 6.886f, 18.476f, 8.926f)
                lineTo(10.708f, 16.707f)
                curveTo(10.52f, 16.895f, 10.266f, 17f, 10f, 17f)
            }
        }.build()

        return _EvaLogo!!
    }

@Suppress("ObjectPropertyName")
private var _EvaLogo: ImageVector? = null
