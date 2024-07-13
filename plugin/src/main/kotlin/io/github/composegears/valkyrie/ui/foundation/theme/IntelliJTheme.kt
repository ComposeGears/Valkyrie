package io.github.composegears.valkyrie.ui.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import io.github.composegears.valkyrie.ui.foundation.theme.IntelliJTheme.Theme
import javax.swing.UIManager
import java.awt.Color as AwtColor

@Composable
fun rememberIntelliJTheme(): IntelliJTheme {
    val themeExtractor = remember<IntelliJThemeExtractor> { IntelliJThemeExtractorImpl() }
    val messageBus = remember { ApplicationManager.getApplication().messageBus.connect() }

    DisposableEffect(messageBus) {
        messageBus.subscribe(
            LafManagerListener.TOPIC,
            ThemeChangeListener(themeExtractor::invalidate)
        )

        onDispose {
            messageBus.disconnect()
        }
    }

    return themeExtractor.theme
}

private class ThemeChangeListener(
    private val onChanged: () -> Unit
) : LafManagerListener {

    override fun lookAndFeelChanged(source: LafManager) = onChanged()
}

data class IntelliJTheme(
    val theme: Theme,
    val primary: Color,
    val background: Color,
    val onBackground: Color
) {
    enum class Theme {
        LIGHT, DARK
    }
}

private interface IntelliJThemeExtractor {
    val theme: IntelliJTheme

    fun invalidate()
}

private class IntelliJThemeExtractorImpl : IntelliJThemeExtractor {

    override var theme by mutableStateOf(buildIntelliJTheme())

    override fun invalidate() {
        theme = buildIntelliJTheme()
    }

    private fun buildIntelliJTheme(): IntelliJTheme {
        val theme = getCurrentTheme()
        val primary = getColor(PRIMARY)
        val background = getColor(BACKGROUND_KEY)
        val onBackground = getColor(ON_BACKGROUND_KEY)
        return IntelliJTheme(
            theme = theme,
            primary = primary,
            background = background,
            onBackground = onBackground
        )
    }

    @Suppress("UnstableApiUsage")
    private fun getCurrentTheme(): Theme {
        val info = LafManager.getInstance().currentUIThemeLookAndFeel

        return when {
            info == null || info.isDark -> Theme.DARK
            else -> Theme.LIGHT
        }
    }

    private fun getColor(key: String): Color = UIManager.getColor(key).toComposeColor()

    companion object {
        private const val PRIMARY = "Link.activeForeground"
        private const val BACKGROUND_KEY = "Panel.background"
        private const val ON_BACKGROUND_KEY = "Panel.foreground"

        private fun AwtColor.toComposeColor(): Color = Color(red, green, blue, alpha)
    }
}