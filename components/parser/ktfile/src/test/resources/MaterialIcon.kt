package androidx.compose.material.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.Path

public val Icons.Filled.Settings: ImageVector
    get() {
        if (_settings != null) {
            return _settings!!
        }
        _settings = materialIcon(name = "Filled.Settings", autoMirror = true) {
            materialPath(
                fillAlpha = 0.5f,
                strokeAlpha = 0.6f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(19.14f, 12.94f)
                close()
            }
        }
        return _settings!!
    }

private var _settings: ImageVector? = null
