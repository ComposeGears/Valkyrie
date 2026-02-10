package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

val ValkyrieIcons.ClipPathGradient: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ClipPathGradient",
        defaultWidth = 5000.dp,
        defaultHeight = 2916.dp,
        viewportWidth = 5000f,
        viewportHeight = 2916f
    ).apply {
        group(
            clipPathData = addPathNodes("M 1026.0 918.0 C 1877.0 1321.7 2427.0 602.7 2721.6 0.0 H 0.0 v 698.6 c 264.3 -29.7 602.1 18.2 1026.0 219.3")
        ) {
            addPath(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(413f, 1501.2f),
                    end = Offset(2500.6f, -349.7f)
                ),
                pathData = addPathNodes("M 0.0 0.0 h 2721.6 v 1321.7 h -2721.6 Z")
            )
        }
        group(
            clipPathData = addPathNodes("M 2721.6 0.0 c -294.6 602.7 -844.7 1321.7 -1695.6 918.0 C 602.1 716.9 264.3 668.9 0.0 698.6 v 573.2 c 331.0 -133.9 782.3 -132.4 1266.0 406.1 c 1219.8 1358.1 2464.0 -1169.0 3734.0 -1120.8 V 0.0 h -2278.4 Z")
        ) {
            addPath(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(717.4f, 2963.9f),
                    end = Offset(4849.2f, -387.4f)
                ),
                pathData = addPathNodes("M 0.0 0.0 h 5000.0 v 3036.1 h -5000.0 Z")
            )
        }
        group(
            clipPathData = addPathNodes("M 5000.0 557.2 c -1270.0 -48.2 -2514.2 2478.9 -3734.0 1120.8 C 782.3 1139.5 331.0 1138.0 0.0 1271.9 v 566.4 c 354.0 -194.6 925.2 -246.3 1626.0 629.7 c 157.9 197.4 309.2 344.0 455.0 448.0 h 1220.8 c 566.6 -357.5 1094.3 -1069.6 1698.3 -1323.7 V 557.2 Z")
        ) {
            addPath(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(304.5f, 3342f),
                    end = Offset(4838.1f, 101.2f)
                ),
                pathData = addPathNodes("M 0.0 509.0 h 5000.0 v 2527.1 h -5000.0 Z")
            )
        }
        group(
            clipPathData = addPathNodes("M 5000.0 1592.3 c -604.0 254.1 -1131.7 966.3 -1698.3 1323.7 h 1698.3 v -1323.7 Z M 1626.0 2468.0 C 925.2 1592.0 354.0 1643.7 0.0 1838.3 v 1077.7 h 2081.0 c -145.8 -104.1 -297.0 -250.6 -455.0 -448.0 Z")
        ) {
            addPath(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF00CCFF),
                        1f to Color(0xFF000066)
                    ),
                    start = Offset(1225.5f, 3877.8f),
                    end = Offset(3836.7f, 550.9f)
                ),
                pathData = addPathNodes("M 0.0 1592.0 h 5000.0 v 1324.0 h -5000.0 Z")
            )
        }
    }.build()
}
