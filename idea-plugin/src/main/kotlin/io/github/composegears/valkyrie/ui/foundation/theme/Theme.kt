package io.github.composegears.valkyrie.ui.foundation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.ui.foundation.theme.IntelliJTheme.Theme
import java.awt.Component

@Composable
fun ValkyrieTheme(
    project: Project,
    currentComponent: Component,
    content: @Composable () -> Unit,
) {
    val intelliJTheme = rememberIntelliJTheme()

    when (intelliJTheme.theme) {
        Theme.DARK -> IntellijDarkTheme(
            background = intelliJTheme.background,
            onBackground = intelliJTheme.onBackground,
            primary = intelliJTheme.primary,
        ) {
            CompositionLocalProvider(
                LocalProject provides project,
                LocalComponent provides currentComponent,
                content = content,
            )
        }
        Theme.LIGHT -> IntellijLightTheme {
            CompositionLocalProvider(
                LocalProject provides project,
                LocalComponent provides currentComponent,
                content = content,
            )
        }
    }
}

@Composable
fun PreviewTheme(
    isDark: Boolean = true,
    content: @Composable () -> Unit,
) {
    PreviewWrapper {
        if (isDark) {
            IntellijDarkTheme(content = content)
        } else {
            IntellijLightTheme(content = content)
        }
    }
}

@Composable
private fun IntellijDarkTheme(
    background: Color = Color(0xFF2B2D30),
    onBackground: Color = Color(0xFFDFE1E5),
    primary: Color = Color(0xFF6B9BFA),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = primary,
            onPrimary = Color.White,
            background = background,
            onBackground = onBackground,
            surface = background,
            onSurface = onBackground,
            inverseSurface = onBackground,
            inverseOnSurface = background,
            surfaceVariant = Color(0xFF4B4C4F),
            onSurfaceVariant = onBackground,
            error = Color(0xFFA63B39),
            onError = Color.White,
        ),
    ) {
        Surface(content = content)
    }
}

@Composable
private fun IntellijLightTheme(
    background: Color = Color(0xFFF7F8FA),
    onBackground: Color = Color.Black,
    primary: Color = Color(0xFF315FBD),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = primary,
            onPrimary = Color.White,
            background = background,
            onBackground = onBackground,
            surface = background,
            onSurface = onBackground,
            inverseSurface = onBackground,
            inverseOnSurface = background,
            surfaceVariant = Color.White,
            onSurfaceVariant = Color.Black,
            error = Color(0xFFF5090A),
            onError = Color.White,
        ),
    ) {
        Surface(content = content)
    }
}

@Composable
private fun PreviewWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        value = LocalInspectionMode provides true,
        content = content,
    )
}
