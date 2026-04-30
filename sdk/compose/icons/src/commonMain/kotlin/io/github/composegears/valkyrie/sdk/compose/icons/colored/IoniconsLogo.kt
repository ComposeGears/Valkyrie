package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

@Suppress("UnusedReceiverParameter")
val ValkyrieIcons.Colored.IoniconsLogo: ImageVector
    get() {
        if (_IoniconsLogo != null) {
            return _IoniconsLogo!!
        }
        _IoniconsLogo = ImageVector.Builder(
            name = "Colored.IoniconsLogo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 512f,
            viewportHeight = 512f,
        ).apply {
            path(fill = SolidColor(Color(0xFF3880FF))) {
                moveTo(73.6f, 256f)
                curveTo(73.6f, 155.263f, 155.263f, 73.6f, 256f, 73.6f)
                curveTo(296.552f, 73.6f, 333.992f, 86.812f, 364.286f, 109.203f)
                curveTo(372.232f, 90.501f, 388.244f, 76.053f, 407.956f, 70.227f)
                curveTo(366.589f, 36.357f, 313.656f, 16f, 256f, 16f)
                curveTo(123.451f, 16f, 16f, 123.452f, 16f, 256f)
                reflectiveCurveTo(123.451f, 496f, 256f, 496f)
                curveTo(388.548f, 496f, 495.999f, 388.548f, 495.999f, 256f)
                curveTo(495.999f, 228.259f, 491.281f, 201.573f, 482.592f, 176.731f)
                curveTo(470.768f, 192.716f, 452.164f, 203.38f, 431.028f, 204.473f)
                curveTo(435.823f, 220.792f, 438.399f, 238.078f, 438.399f, 256f)
                curveTo(438.399f, 356.737f, 356.736f, 438.4f, 256f, 438.4f)
                reflectiveCurveTo(73.6f, 356.737f, 73.6f, 256f)
            }
            path(fill = SolidColor(Color(0xFF3880FF))) {
                moveTo(491.065f, 207.374f)
                arcToRelative(238f, 238f, 0f, isMoreThanHalf = false, isPositiveArc = false, -8.473f, -30.643f)
                curveTo(470.769f, 192.715f, 452.164f, 203.38f, 431.028f, 204.473f)
                arcToRelative(181.6f, 181.6f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.719f, 35.964f)
                curveTo(460.006f, 237.7f, 479.247f, 225.211f, 491.065f, 207.374f)
                moveTo(256f, 361.001f)
                curveTo(313.853f, 361.001f, 361f, 313.944f, 361f, 256.001f)
                curveTo(361f, 198.147f, 313.943f, 151.001f, 256f, 151.001f)
                reflectiveCurveTo(151f, 198.147f, 151f, 256.001f)
                reflectiveCurveTo(198.147f, 361.001f, 256f, 361.001f)
                moveTo(413.5f, 166f)
                curveTo(442.495f, 166f, 466f, 142.495f, 466f, 113.5f)
                reflectiveCurveTo(442.495f, 61f, 413.5f, 61f)
                reflectiveCurveTo(361f, 84.505f, 361f, 113.5f)
                reflectiveCurveTo(384.505f, 166f, 413.5f, 166f)
            }
        }.build()

        return _IoniconsLogo!!
    }

@Suppress("ObjectPropertyName")
private var _IoniconsLogo: ImageVector? = null
