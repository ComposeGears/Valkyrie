package io.github.composegears.valkyrie.theme.intellig

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import io.github.composegears.valkyrie.theme.intellig.SwingColor.Theme
import javax.swing.UIManager
import java.awt.Color as AWTColor

interface SwingColor {
    val theme: Theme

    val primary: Color
    val background: Color
    val onBackground: Color

    enum class Theme {
        LIGHT, DARK
    }
}

@Composable
fun rememberSwingColor(): SwingColor {
    val swingColor = remember { SwingColorImpl() }

    val messageBus = remember {
        ApplicationManager.getApplication().messageBus.connect()
    }

    remember(messageBus) {
        messageBus.subscribe(
            LafManagerListener.TOPIC,
            ThemeChangeListener(swingColor::invalidateColors)
        )
    }

    DisposableEffect(messageBus) {
        onDispose {
            messageBus.disconnect()
        }
    }

    return swingColor
}

private class SwingColorImpl : SwingColor {

    override var primary: Color by mutableStateOf(getColor(PRIMARY))
    override var background: Color by mutableStateOf(getColor(BACKGROUND_KEY))
    override var onBackground: Color by mutableStateOf(getColor(ON_BACKGROUND_KEY))
    override var theme: Theme by mutableStateOf(getCurrentTheme())

    fun invalidateColors() {
        primary = getColor(PRIMARY)
        background = getColor(BACKGROUND_KEY)
        onBackground = getColor(ON_BACKGROUND_KEY)
        theme = getCurrentTheme()
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

        private fun AWTColor.toComposeColor(): Color = Color(red, green, blue, alpha)
    }
}