package io.github.composegears.valkyrie.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposePanel
import javax.swing.JComponent

fun buildComposePanel(
    height: Int = 800,
    width: Int = 800,
    y: Int = 0,
    x: Int = 0,
    content: @Composable ComposePanel.() -> Unit,
): JComponent = ComposePanel().apply {
    setBounds(x = x, y = y, width = width, height = height)
    setContent {
        content()
    }
}
