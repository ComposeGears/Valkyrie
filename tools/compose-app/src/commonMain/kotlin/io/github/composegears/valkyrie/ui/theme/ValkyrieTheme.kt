package io.github.composegears.valkyrie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ValkyrieTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ThemeObserver()

    val colorScheme = when {
        LocalTheme.current.isDarkTheme -> ValkyrieColors.dark()
        else -> ValkyrieColors.light()
    }

    MaterialTheme(colorScheme = colorScheme) {
        Surface(modifier = modifier, content = content)
    }
}

@Composable
private fun ThemeObserver() {
    val localTheme = LocalTheme.current

    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(isSystemInDarkTheme) {
        localTheme.isDarkTheme = isSystemInDarkTheme
    }
}

private object ValkyrieColors {
    fun dark() = darkColorScheme(
        primary = Color(0xFF6B9BFA),
        onPrimary = Color.White,
        background = Color(0xFF2B2D30),
        onBackground = Color(0xFFDFE1E5),
        surface = Color(0xFF2B2D30),
        surfaceContainerHigh = Color(0xFF3A3A3C),
        surfaceContainerHighest = Color(0xFF4B4C4F),
        surfaceContainer = Color(0xFF3A3A3C),
        onSurface = Color(0xFFDFE1E5),
        inverseSurface = Color(0xFFDFE1E5),
        inverseOnSurface = Color(0xFF2B2D30),
        surfaceVariant = Color(0xFF4B4C4F),
        onSurfaceVariant = Color(0xFFDFE1E5),
        error = Color(0xFFA63B39),
        onError = Color.White,
    )

    fun light() = lightColorScheme(
        primary = Color(0xFF315FBD),
        onPrimary = Color.White,
        background = Color(0xFFF7F8FA),
        onBackground = Color.Black,
        surface = Color(0xFFF7F8FA),
        surfaceContainerHigh = Color(0xFFEDEDED),
        surfaceContainerHighest = Color.White,
        surfaceContainer = Color(0xFFEDEDED),
        onSurface = Color.Black,
        inverseSurface = Color.Black,
        inverseOnSurface = Color(0xFFF7F8FA),
        surfaceVariant = Color.White,
        onSurfaceVariant = Color.Black,
        error = Color(0xFFF5090A),
        onError = Color.White,
    )
}
