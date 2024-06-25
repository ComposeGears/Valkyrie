package io.github.composegears.valkyrie.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.theme.intellig.SwingColor.Theme
import io.github.composegears.valkyrie.theme.intellig.rememberSwingColor
import java.awt.Component

@Composable
fun ValkyrieTheme(
    project: Project,
    currentComponent: Component,
    content: @Composable () -> Unit,
) {
    val swingColor = rememberSwingColor()

    val colors = when (swingColor.theme) {
        Theme.DARK -> darkColorScheme(onPrimary = Color.White)
        Theme.LIGHT -> lightColorScheme(onPrimary = Color.White)
    }

    MaterialTheme(
        colorScheme = colors.copy(
            primary = swingColor.primary,
            background = swingColor.background,
            onBackground = swingColor.onBackground,
            surface = swingColor.background,
            onSurface = swingColor.onBackground,
            surfaceVariant = swingColor.onBackground,
            onSurfaceVariant = swingColor.background,
            inverseSurface = swingColor.onBackground,
            inverseOnSurface = swingColor.background,
        ),
        content = {
            CompositionLocalProvider(
                LocalProject provides project,
                LocalComponent provides currentComponent
            ) {
                content()
            }
        }
    )
}