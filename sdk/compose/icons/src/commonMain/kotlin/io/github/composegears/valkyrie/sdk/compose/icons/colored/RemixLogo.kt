package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.RemixLogo: ImageVector
    get() {
        if (_RemixLogo != null) {
            return _RemixLogo!!
        }
        _RemixLogo = ImageVector.Builder(
            name = "RemixLogo",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 32f,
            viewportHeight = 32f,
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF08B1FA),
                        1f to Color(0xFF4F8EFF),
                    ),
                    start = Offset(16.25f, 4f),
                    end = Offset(16.25f, 30f),
                ),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(19.332f, 4.005f)
                arcTo(7.662f, 7.662f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19f, 6.26f)
                curveTo(19f, 9.866f, 22.134f, 13f, 26.26f, 13f)
                arcToRelative(6.476f, 6.476f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.999f, -0.75f)
                curveToRelative(0.158f, 0.725f, 0.241f, 1.478f, 0.241f, 2.25f)
                curveTo(29.5f, 20.299f, 24.799f, 25f, 19f, 25f)
                verticalLineToRelative(5f)
                lineTo(3f, 30f)
                lineTo(3f, 4f)
                horizontalLineToRelative(16f)
                curveToRelative(0.111f, 0f, 0.222f, 0.002f, 0.332f, 0.005f)
                close()
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF0090FF),
                        1f to Color(0xFF0059FF),
                    ),
                    start = Offset(16f, 4f),
                    end = Offset(16f, 30f),
                ),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(3f, 4f)
                lineToRelative(26f, 26f)
                lineTo(3f, 30f)
                close()
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFFFF3A50),
                        1f to Color(0xFFED306B),
                    ),
                    start = Offset(26f, 2f),
                    end = Offset(26f, 10f),
                ),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(26f, 6f)
                moveToRelative(-4f, 0f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8f, 0f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = true, -8f, 0f)
            }
        }.build()

        return _RemixLogo!!
    }

@Suppress("ObjectPropertyName")
private var _RemixLogo: ImageVector? = null
