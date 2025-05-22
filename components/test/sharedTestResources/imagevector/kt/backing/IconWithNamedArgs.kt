package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.IconWithNamedArgs: ImageVector
    get() {
        if (_IconWithNamedArgs != null) {
            return _IconWithNamedArgs!!
        }
        _IconWithNamedArgs = ImageVector.Builder(
            name = "IconWithNamedArgs",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(21f, 6.376f)
                curveToRelative(0f, -0.964f, -0.894f, -1.672f, -1.833f, -1.454f)
                curveToRelative(-1.37f, 0.317f, -3.152f, 0.734f, -4.03f, 0.958f)
                arcToRelative(0.763f, 0.763f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.55f, -0.066f)
                lineToRelative(-4.991f, -2.68f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.528f, -0.07f)
                lineToRelative(-5.49f, 1.3f)
                arcTo(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 5.092f)
                verticalLineToRelative(13.466f)
                curveToRelative(0f, 0.482f, 0.446f, 0.839f, 0.915f, 0.73f)
                curveToRelative(1.355f, -0.311f, 3.852f, -0.89f, 4.946f, -1.169f)
                arcToRelative(0.737f, 0.737f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.55f, 0.07f)
                curveToRelative(1.54f, 0.88f, 4.378f, 2.81f, 5.358f, 2.81f)
                curveToRelative(1.175f, 0f, 4.994f, -1.024f, 6.027f, -1.705f)
                curveToRelative(0.145f, -0.096f, 0.204f, -0.259f, 0.204f, -0.433f)
                lineTo(21f, 6.377f)
                close()
                moveTo(9.23f, 15.923f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.556f, 0.725f)
                lineToRelative(-3.346f, 0.89f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.943f, -0.725f)
                lineTo(4.385f, 6.407f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.55f, -0.723f)
                lineToRelative(3.347f, -0.923f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.949f, 0.723f)
                verticalLineToRelative(10.44f)
                close()
                moveTo(19.615f, 17.593f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.55f, 0.723f)
                lineToRelative(-3.346f, 0.922f)
                arcToRelative(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.95f, -0.723f)
                lineTo(14.769f, 8.026f)
                curveToRelative(0f, -0.316f, 0.198f, -0.598f, 0.5f, -0.69f)
                curveToRelative(0.903f, -0.271f, 2.246f, -0.59f, 3.398f, -0.88f)
                curveToRelative(0.479f, -0.12f, 0.948f, 0.24f, 0.948f, 0.735f)
                verticalLineToRelative(10.402f)
                close()
            }
        }.build()

        return _IconWithNamedArgs!!
    }

@Suppress("ObjectPropertyName")
private var _IconWithNamedArgs: ImageVector? = null
