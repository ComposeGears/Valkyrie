package io.github.composegears.valkyrie.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.theme.intellig.SwingColor
import io.github.composegears.valkyrie.util.LocalProject

val green200 = Color(0xffa5d6a7)
val green500 = Color(0xff4caf50)

val teal200 = Color(0xff80deea)

private val DarkGreenColorPalette = darkColorScheme(
    primary = green200,
    secondary = teal200,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    error = Color.Red,
)

private val LightGreenColorPalette = lightColorScheme(
    primary = green500,
    secondary = teal200,
    onPrimary = Color.White,
    onSurface = Color.Black
)

@Composable
fun WidgetTheme(
    project: Project,
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkGreenColorPalette else LightGreenColorPalette
    val swingColor = SwingColor()

    MaterialTheme(
        colorScheme = colors.copy(
            background = swingColor.background,
            onBackground = swingColor.onBackground,
            surface = swingColor.background,
            onSurface = swingColor.onBackground,
        ),
        content = {
            CompositionLocalProvider(LocalProject provides project,) {
                content()
            }
        }
    )
}