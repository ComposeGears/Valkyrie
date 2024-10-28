package io.github.composegears.valkyrie.ui.foundation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
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
    val rootContent = @Composable {
        CompositionLocalProvider(
            LocalProject provides project,
            LocalComponent provides currentComponent,
            content = content,
        )
    }

    ValkyrieTheme(rootContent)
}

@Composable
fun ValkyrieTheme(content: @Composable () -> Unit) {
    val intelliJTheme = rememberIntelliJTheme()

    when (intelliJTheme.theme) {
        Theme.DARK -> IntellijDarkTheme(
            background = intelliJTheme.background,
            onBackground = intelliJTheme.onBackground,
            primary = intelliJTheme.primary,
            content = content,
        )
        Theme.LIGHT -> IntellijLightTheme(content = content)
    }
}

@Composable
fun PreviewTheme(
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    alignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    val contentWrapper = @Composable {
        Box(
            modifier = modifier.fillMaxSize(),
            content = content,
            contentAlignment = alignment,
        )
    }

    PreviewWrapper {
        if (isDark) {
            IntellijDarkTheme(content = contentWrapper)
        } else {
            IntellijLightTheme(content = contentWrapper)
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
            background = Color(0xFF1C1D1F),
            onBackground = onBackground,
            surface = background,
            surfaceContainerHigh = Color(0xFF3A3A3C),
            surfaceContainerHighest = Color(0xFF4B4C4F),
            surfaceContainer = Color(0xFF3A3A3C),
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
        colorScheme = lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            background = background,
            onBackground = onBackground,
            surface = background,
            surfaceContainerHigh = Color(0xFFEDEDED),
            surfaceContainerHighest = Color.White,
            surfaceContainer = Color(0xFFEDEDED),
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

@Stable
val ColorScheme.isLight
    @Composable
    get() = background.luminance() > 0.5f
