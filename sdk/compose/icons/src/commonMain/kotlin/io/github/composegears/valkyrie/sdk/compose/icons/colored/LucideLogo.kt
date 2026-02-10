package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.LucideLogo: ImageVector
    get() {
        if (_LucideLogo != null) {
            return _LucideLogo!!
        }
        _LucideLogo = ImageVector.Builder(
            name = "Colored.LucideLogo",
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
                moveTo(14f, 12f)
                curveTo(14f, 9.791f, 12.209f, 8f, 10f, 8f)
                curveTo(7.791f, 8f, 6f, 9.791f, 6f, 12f)
                curveTo(6f, 16.418f, 9.582f, 20f, 14f, 20f)
                curveTo(18.418f, 20f, 22f, 16.418f, 22f, 12f)
                curveTo(22f, 8.446f, 20.455f, 5.253f, 18f, 3.056f)
            }
            path(
                stroke = SolidColor(Color(0xFFF56565)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(10f, 12f)
                curveTo(10f, 14.209f, 11.791f, 16f, 14f, 16f)
                curveTo(16.209f, 16f, 18f, 14.209f, 18f, 12f)
                curveTo(18f, 7.582f, 14.418f, 4f, 10f, 4f)
                curveTo(5.582f, 4f, 2f, 7.582f, 2f, 12f)
                curveTo(2f, 15.584f, 3.571f, 18.801f, 6.063f, 21f)
            }
        }.build()

        return _LucideLogo!!
    }

@Suppress("ObjectPropertyName")
private var _LucideLogo: ImageVector? = null
