package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Settings: ImageVector
    get() {
        if (_Settings != null) {
            return _Settings!!
        }
        _Settings = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF6C707E)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(3.227f, 4.37f)
                curveTo(2.922f, 4.769f, 2.667f, 5.208f, 2.47f, 5.676f)
                lineTo(3.31f, 7.094f)
                curveTo(3.641f, 7.653f, 3.641f, 8.347f, 3.31f, 8.906f)
                lineTo(2.47f, 10.324f)
                curveTo(2.667f, 10.792f, 2.922f, 11.231f, 3.227f, 11.63f)
                lineTo(4.873f, 11.612f)
                curveTo(5.522f, 11.605f, 6.123f, 11.952f, 6.441f, 12.518f)
                lineTo(7.249f, 13.953f)
                curveTo(7.496f, 13.984f, 7.748f, 14f, 8.004f, 14f)
                curveTo(8.26f, 14f, 8.512f, 13.984f, 8.759f, 13.953f)
                lineTo(9.567f, 12.518f)
                curveTo(9.885f, 11.952f, 10.486f, 11.605f, 11.135f, 11.612f)
                lineTo(12.782f, 11.63f)
                curveTo(13.086f, 11.231f, 13.341f, 10.792f, 13.538f, 10.324f)
                lineTo(12.698f, 8.906f)
                curveTo(12.367f, 8.347f, 12.367f, 7.653f, 12.698f, 7.094f)
                lineTo(13.538f, 5.676f)
                curveTo(13.341f, 5.208f, 13.086f, 4.769f, 12.782f, 4.37f)
                lineTo(11.135f, 4.388f)
                curveTo(10.486f, 4.395f, 9.885f, 4.048f, 9.567f, 3.482f)
                lineTo(8.759f, 2.047f)
                curveTo(8.512f, 2.016f, 8.26f, 2f, 8.004f, 2f)
                curveTo(7.748f, 2f, 7.496f, 2.016f, 7.249f, 2.047f)
                lineTo(6.441f, 3.482f)
                curveTo(6.123f, 4.048f, 5.522f, 4.395f, 4.873f, 4.388f)
                lineTo(3.227f, 4.37f)
                close()
                moveTo(10.655f, 8f)
                curveTo(10.655f, 9.464f, 9.468f, 10.651f, 8.004f, 10.651f)
                curveTo(6.54f, 10.651f, 5.353f, 9.464f, 5.353f, 8f)
                curveTo(5.353f, 6.536f, 6.54f, 5.349f, 8.004f, 5.349f)
                curveTo(9.468f, 5.349f, 10.655f, 6.536f, 10.655f, 8f)
                close()
                moveTo(4.884f, 3.388f)
                curveTo(5.168f, 3.391f, 5.431f, 3.239f, 5.57f, 2.991f)
                lineTo(6.438f, 1.449f)
                curveTo(6.549f, 1.252f, 6.74f, 1.11f, 6.964f, 1.077f)
                curveTo(7.303f, 1.026f, 7.651f, 1f, 8.004f, 1f)
                curveTo(8.358f, 1f, 8.705f, 1.026f, 9.044f, 1.077f)
                curveTo(9.268f, 1.11f, 9.459f, 1.252f, 9.57f, 1.449f)
                lineTo(10.438f, 2.991f)
                curveTo(10.577f, 3.239f, 10.84f, 3.391f, 11.125f, 3.388f)
                lineTo(12.894f, 3.368f)
                curveTo(13.12f, 3.366f, 13.337f, 3.46f, 13.478f, 3.637f)
                curveTo(13.91f, 4.178f, 14.263f, 4.784f, 14.521f, 5.439f)
                curveTo(14.603f, 5.649f, 14.576f, 5.885f, 14.461f, 6.079f)
                lineTo(13.559f, 7.604f)
                curveTo(13.414f, 7.848f, 13.414f, 8.152f, 13.559f, 8.396f)
                lineTo(14.461f, 9.921f)
                curveTo(14.576f, 10.115f, 14.603f, 10.351f, 14.521f, 10.561f)
                curveTo(14.263f, 11.216f, 13.91f, 11.822f, 13.478f, 12.363f)
                curveTo(13.337f, 12.54f, 13.12f, 12.634f, 12.894f, 12.632f)
                lineTo(11.125f, 12.612f)
                curveTo(10.84f, 12.609f, 10.577f, 12.761f, 10.438f, 13.009f)
                lineTo(9.57f, 14.551f)
                curveTo(9.459f, 14.748f, 9.268f, 14.89f, 9.044f, 14.923f)
                curveTo(8.705f, 14.974f, 8.358f, 15f, 8.004f, 15f)
                curveTo(7.651f, 15f, 7.303f, 14.974f, 6.964f, 14.923f)
                curveTo(6.74f, 14.89f, 6.549f, 14.748f, 6.438f, 14.551f)
                lineTo(5.57f, 13.009f)
                curveTo(5.431f, 12.761f, 5.168f, 12.609f, 4.884f, 12.612f)
                lineTo(3.114f, 12.632f)
                curveTo(2.889f, 12.634f, 2.671f, 12.54f, 2.53f, 12.363f)
                curveTo(2.098f, 11.822f, 1.745f, 11.216f, 1.487f, 10.561f)
                curveTo(1.405f, 10.351f, 1.432f, 10.115f, 1.547f, 9.921f)
                lineTo(2.449f, 8.396f)
                curveTo(2.594f, 8.152f, 2.594f, 7.848f, 2.449f, 7.604f)
                lineTo(1.547f, 6.079f)
                curveTo(1.432f, 5.885f, 1.405f, 5.649f, 1.487f, 5.439f)
                curveTo(1.745f, 4.784f, 2.098f, 4.178f, 2.53f, 3.637f)
                curveTo(2.671f, 3.46f, 2.889f, 3.366f, 3.114f, 3.368f)
                lineTo(4.884f, 3.388f)
                close()
                moveTo(9.655f, 8f)
                curveTo(9.655f, 8.912f, 8.916f, 9.651f, 8.004f, 9.651f)
                curveTo(7.092f, 9.651f, 6.353f, 8.912f, 6.353f, 8f)
                curveTo(6.353f, 7.088f, 7.092f, 6.349f, 8.004f, 6.349f)
                curveTo(8.916f, 6.349f, 9.655f, 7.088f, 9.655f, 8f)
                close()
            }
        }.build()

        return _Settings!!
    }

@Suppress("ObjectPropertyName")
private var _Settings: ImageVector? = null
