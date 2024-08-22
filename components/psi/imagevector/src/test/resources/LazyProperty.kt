import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Outlined.LazyProperty: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LazyProperty",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color(0xFF232F34))) {
            moveTo(19f, 13f)
            lineTo(13f, 13f)
            lineTo(13f, 19f)
            lineTo(11f, 19f)
            lineTo(11f, 13f)
            lineTo(5f, 13f)
            lineTo(5f, 11f)
            lineTo(11f, 11f)
            lineTo(11f, 5f)
            lineTo(13f, 5f)
            lineTo(13f, 11f)
            lineTo(19f, 11f)
            lineTo(19f, 13f)
            close()
        }
    }.build()
}
