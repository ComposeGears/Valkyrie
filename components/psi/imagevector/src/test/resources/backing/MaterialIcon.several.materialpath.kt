package androidx.compose.material.icons.filled

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.test.Icons

private var _percent: ImageVector? = null

val Icons.Percent: ImageVector
    get() {
        if (_percent != null) return _percent!!

        _percent = materialIcon(name = "Percent") {
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

        return _percent!!
    }
