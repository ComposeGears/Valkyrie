package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

@Suppress("UnusedReceiverParameter")
val ValkyrieIcons.Colored.HeroiconsLogo: ImageVector
    get() {
        if (_HeroiconsLogo != null) {
            return _HeroiconsLogo!!
        }
        _HeroiconsLogo = ImageVector.Builder(
            name = "Colored.HeroiconsLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF8B5CF6)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(12f, 2.714f)
                arcTo(11.959f, 11.959f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3.598f, 6f)
                arcTo(11.99f, 11.99f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 9.749f)
                curveToRelative(0f, 5.592f, 3.824f, 10.29f, 9f, 11.623f)
                curveToRelative(5.176f, -1.332f, 9f, -6.03f, 9f, -11.622f)
                curveToRelative(0f, -1.31f, -0.21f, -2.571f, -0.598f, -3.751f)
                horizontalLineToRelative(-0.152f)
                curveToRelative(-3.196f, 0f, -6.1f, -1.248f, -8.25f, -3.285f)
                close()
            }
        }.build()

        return _HeroiconsLogo!!
    }

@Suppress("ObjectPropertyName")
private var _HeroiconsLogo: ImageVector? = null
