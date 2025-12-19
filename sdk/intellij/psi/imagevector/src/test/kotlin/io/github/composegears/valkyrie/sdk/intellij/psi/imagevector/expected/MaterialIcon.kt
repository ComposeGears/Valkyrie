package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.unit.dp

val ExpectedMaterialIcon = materialIcon(name = "Filled.Settings", autoMirror = true) {
    materialPath(
        fillAlpha = 0.5f,
        strokeAlpha = 0.6f,
        pathFillType = PathFillType.EvenOdd,
    ) {
        moveTo(19.14f, 12.94f)
        close()
    }
}

val ExpectedMaterialIconWithoutParam = materialIcon(name = "Bell") {
    materialPath {
        moveTo(18.0f, 16.0f)
        lineTo(18.0f, 11.0f)
        curveTo(18.0f, 7.9297f, 16.3594f, 5.3594f, 13.5f, 4.6797f)
        lineTo(13.5f, 4.0f)
        curveTo(13.5f, 3.1719f, 12.8281f, 2.5f, 12.0f, 2.5f)
        curveTo(11.1719f, 2.5f, 10.5f, 3.1719f, 10.5f, 4.0f)
        lineTo(10.5f, 4.6797f)
        curveTo(7.6289f, 5.3594f, 6.0f, 7.9219f, 6.0f, 11.0f)
        lineTo(6.0f, 16.0f)
        lineTo(4.0f, 18.0f)
        lineTo(4.0f, 19.0f)
        lineTo(20.0f, 19.0f)
        lineTo(20.0f, 18.0f)
        close()
        moveTo(12.0f, 22.0f)
        curveTo(13.1016f, 22.0f, 14.0f, 21.1016f, 14.0f, 20.0f)
        lineTo(10.0f, 20.0f)
        curveTo(10.0f, 21.1016f, 10.8906f, 22.0f, 12.0f, 22.0f)
        close()
        moveTo(12.0f, 22.0f)
    }
}

val ExpectedMaterialIconWithoutParam2 = materialIcon(name = "Percent") {
    materialPath(
        pathFillType = PathFillType.EvenOdd,
    ) {
        moveTo(7.5f, 9f)
        curveTo(8.328f, 9f, 9f, 8.328f, 9f, 7.5f)
        curveTo(9f, 6.672f, 8.328f, 6f, 7.5f, 6f)
        curveTo(6.672f, 6f, 6f, 6.672f, 6f, 7.5f)
        curveTo(6f, 8.328f, 6.672f, 9f, 7.5f, 9f)
        close()
        moveTo(7.5f, 11f)
        curveTo(9.433f, 11f, 11f, 9.433f, 11f, 7.5f)
        curveTo(11f, 5.567f, 9.433f, 4f, 7.5f, 4f)
        curveTo(5.567f, 4f, 4f, 5.567f, 4f, 7.5f)
        curveTo(4f, 9.433f, 5.567f, 11f, 7.5f, 11f)
        close()
    }
    materialPath(
        pathFillType = PathFillType.EvenOdd,
    ) {
        moveTo(16.5f, 18f)
        curveTo(17.328f, 18f, 18f, 17.328f, 18f, 16.5f)
        curveTo(18f, 15.672f, 17.328f, 15f, 16.5f, 15f)
        curveTo(15.672f, 15f, 15f, 15.672f, 15f, 16.5f)
        curveTo(15f, 17.328f, 15.672f, 18f, 16.5f, 18f)
        close()
        moveTo(16.5f, 20f)
        curveTo(18.433f, 20f, 20f, 18.433f, 20f, 16.5f)
        curveTo(20f, 14.567f, 18.433f, 13f, 16.5f, 13f)
        curveTo(14.567f, 13f, 13f, 14.567f, 13f, 16.5f)
        curveTo(13f, 18.433f, 14.567f, 20f, 16.5f, 20f)
        close()
    }
    materialPath {
        moveTo(17.657f, 4.929f)
        lineTo(19.071f, 6.343f)
        lineTo(6.343f, 19.071f)
        lineTo(4.929f, 17.657f)
        lineTo(17.657f, 4.929f)
        close()
    }
}

val ExpectedMaterialIconOnlyWithPath = Builder(
    name = "NotificationsAlert",
    defaultWidth = 24.0.dp,
    defaultHeight = 24.0.dp,
    viewportWidth = 24.0f,
    viewportHeight = 24.0f,
).apply {
    materialPath {
        moveTo(18.75f, 16.5385f)
        verticalLineTo(11.1538f)
        curveTo(18.75f, 7.8477f, 16.905f, 5.08f, 13.6875f, 4.3477f)
        verticalLineTo(3.6154f)
        curveTo(13.6875f, 2.7215f, 12.9338f, 2.0f, 12.0f, 2.0f)
        curveTo(11.0662f, 2.0f, 10.3125f, 2.7215f, 10.3125f, 3.6154f)
        verticalLineTo(4.3477f)
        curveTo(7.0837f, 5.08f, 5.25f, 7.8369f, 5.25f, 11.1538f)
        verticalLineTo(16.5385f)
        lineTo(3.0f, 18.6923f)
        verticalLineTo(19.7692f)
        horizontalLineTo(21.0f)
        verticalLineTo(18.6923f)
        lineTo(18.75f, 16.5385f)
        close()
        moveTo(13.125f, 16.5385f)
        horizontalLineTo(10.875f)
        verticalLineTo(14.3846f)
        horizontalLineTo(13.125f)
        verticalLineTo(16.5385f)
        close()
        moveTo(13.125f, 12.2308f)
        horizontalLineTo(10.875f)
        verticalLineTo(7.9231f)
        horizontalLineTo(13.125f)
        verticalLineTo(12.2308f)
        close()
        moveTo(12.0f, 23.0f)
        curveTo(13.2375f, 23.0f, 14.25f, 22.0308f, 14.25f, 20.8462f)
        horizontalLineTo(9.75f)
        curveTo(9.75f, 22.0308f, 10.7512f, 23.0f, 12.0f, 23.0f)
        close()
    }
}.build()
