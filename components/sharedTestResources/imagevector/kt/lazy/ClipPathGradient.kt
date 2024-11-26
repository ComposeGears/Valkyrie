package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ClipPathGradient: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ClipPathGradient",
        defaultWidth = 5000.dp,
        defaultHeight = 2916.dp,
        viewportWidth = 5000f,
        viewportHeight = 2916f
    ).apply {
        group(
            clipPathData = PathData {
                moveTo(1026f, 918f)
                curveTo(1877f, 1321.7f, 2427f, 602.7f, 2721.6f, 0f)
                horizontalLineTo(0f)
                verticalLineToRelative(698.6f)
                curveToRelative(264.3f, -29.7f, 602.1f, 18.2f, 1026f, 219.3f)
            }
        ) {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(413f, 1501.2f),
                    end = Offset(2500.6f, -349.7f)
                )
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(2721.6f)
                verticalLineToRelative(1321.7f)
                horizontalLineToRelative(-2721.6f)
                close()
            }
        }
        group(
            clipPathData = PathData {
                moveTo(2721.6f, 0f)
                curveToRelative(-294.6f, 602.7f, -844.7f, 1321.7f, -1695.6f, 918f)
                curveTo(602.1f, 716.9f, 264.3f, 668.9f, 0f, 698.6f)
                verticalLineToRelative(573.2f)
                curveToRelative(331f, -133.9f, 782.3f, -132.4f, 1266f, 406.1f)
                curveToRelative(1219.8f, 1358.1f, 2464f, -1169f, 3734f, -1120.8f)
                verticalLineTo(0f)
                horizontalLineToRelative(-2278.4f)
                close()
            }
        ) {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(717.4f, 2963.9f),
                    end = Offset(4849.2f, -387.4f)
                )
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(5000f)
                verticalLineToRelative(3036.1f)
                horizontalLineToRelative(-5000f)
                close()
            }
        }
        group(
            clipPathData = PathData {
                moveTo(5000f, 557.2f)
                curveToRelative(-1270f, -48.2f, -2514.2f, 2478.9f, -3734f, 1120.8f)
                curveTo(782.3f, 1139.5f, 331f, 1138f, 0f, 1271.9f)
                verticalLineToRelative(566.4f)
                curveToRelative(354f, -194.6f, 925.2f, -246.3f, 1626f, 629.7f)
                curveToRelative(157.9f, 197.4f, 309.2f, 344f, 455f, 448f)
                horizontalLineToRelative(1220.8f)
                curveToRelative(566.6f, -357.5f, 1094.3f, -1069.6f, 1698.3f, -1323.7f)
                verticalLineTo(557.2f)
                close()
            }
        ) {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(304.5f, 3342f),
                    end = Offset(4838.1f, 101.2f)
                )
            ) {
                moveTo(0f, 509f)
                horizontalLineToRelative(5000f)
                verticalLineToRelative(2527.1f)
                horizontalLineToRelative(-5000f)
                close()
            }
        }
        group(
            clipPathData = PathData {
                moveTo(5000f, 1592.3f)
                curveToRelative(-604f, 254.1f, -1131.7f, 966.3f, -1698.3f, 1323.7f)
                horizontalLineToRelative(1698.3f)
                verticalLineToRelative(-1323.7f)
                close()
                moveTo(1626f, 2468f)
                curveTo(925.2f, 1592f, 354f, 1643.7f, 0f, 1838.3f)
                verticalLineToRelative(1077.7f)
                horizontalLineToRelative(2081f)
                curveToRelative(-145.8f, -104.1f, -297f, -250.6f, -455f, -448f)
                close()
            }
        ) {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(1225.5f, 3877.8f),
                    end = Offset(3836.7f, 550.9f)
                )
            ) {
                moveTo(0f, 1592f)
                horizontalLineToRelative(5000f)
                verticalLineToRelative(1324f)
                horizontalLineToRelative(-5000f)
                close()
            }
        }
    }.build()
}
