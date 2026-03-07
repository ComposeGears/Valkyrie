package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.FontAwesomeLogo: ImageVector
    get() {
        if (_FontAwesomeLogo != null) {
            return _FontAwesomeLogo!!
        }
        _FontAwesomeLogo = ImageVector.Builder(
            name = "FontAwesomeLogo",
            defaultWidth = 512.dp,
            defaultHeight = 512.dp,
            viewportWidth = 512f,
            viewportHeight = 512f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF418FDE)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(153.33f, 156f)
                curveTo(162.45f, 150.24f, 168.5f, 140.08f, 168.5f, 128.5f)
                curveTo(168.5f, 110.55f, 153.95f, 96f, 136f, 96f)
                curveTo(118.05f, 96f, 103.5f, 110.55f, 103.5f, 128.5f)
                curveTo(103.5f, 138.91f, 108.39f, 148.17f, 116f, 154.12f)
                verticalLineTo(156f)
                verticalLineTo(376f)
                verticalLineTo(416f)
                horizontalLineTo(156f)
                verticalLineTo(376f)
                horizontalLineTo(389.5f)
                curveTo(398.61f, 376f, 406f, 368.61f, 406f, 359.5f)
                curveTo(406f, 357.19f, 405.52f, 354.91f, 404.58f, 352.8f)
                lineTo(366f, 266f)
                lineTo(404.58f, 179.2f)
                curveTo(405.52f, 177.09f, 406f, 174.81f, 406f, 172.5f)
                curveTo(406f, 163.39f, 398.61f, 156f, 389.5f, 156f)
                horizontalLineTo(153.33f)
                close()
            }
        }.build()

        return _FontAwesomeLogo!!
    }

@Suppress("ObjectPropertyName")
private var _FontAwesomeLogo: ImageVector? = null
