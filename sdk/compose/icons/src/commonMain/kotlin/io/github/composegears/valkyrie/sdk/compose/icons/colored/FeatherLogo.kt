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
val ValkyrieIcons.Colored.FeatherLogo: ImageVector
    get() {
        if (_FeatherLogo != null) {
            return _FeatherLogo!!
        }
        _FeatherLogo = ImageVector.Builder(
            name = "Colored.FeatherLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(20.24f, 12.24f)
                arcToRelative(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8.49f, -8.49f)
                lineTo(5f, 10.5f)
                verticalLineTo(19f)
                horizontalLineToRelative(8.5f)
                close()
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(16f, 8f)
                lineTo(2f, 22f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(17.5f, 15f)
                lineTo(9f, 15f)
            }
        }.build()

        return _FeatherLogo!!
    }

@Suppress("ObjectPropertyName")
private var _FeatherLogo: ImageVector? = null
