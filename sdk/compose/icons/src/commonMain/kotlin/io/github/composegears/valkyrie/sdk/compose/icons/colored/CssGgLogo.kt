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
val ValkyrieIcons.Colored.CssGgLogo: ImageVector
    get() {
        if (_CssGgLogo != null) {
            return _CssGgLogo!!
        }
        _CssGgLogo = ImageVector.Builder(
            name = "Colored.CssGgLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 42f,
            viewportHeight = 42f,
        ).apply {
            path(fill = SolidColor(Color(0xFF5F19DD))) {
                moveTo(21f, 1f)
                arcToRelative(20f, 20f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 40f)
                arcToRelative(20f, 20f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -40f)
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
            ) {
                moveTo(27f, 12f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3f, 3f)
                verticalLineToRelative(12f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 3f, -3f)
                horizontalLineTo(15f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 3f, 3f)
                verticalLineTo(15f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, -3f, 3f)
                horizontalLineToRelative(12f)
                arcToRelative(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, -6f)
                close()
            }
        }.build()

        return _CssGgLogo!!
    }

@Suppress("ObjectPropertyName")
private var _CssGgLogo: ImageVector? = null
