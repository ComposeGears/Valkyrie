@file:Suppress("ktlint:standard:filename")

package io.github.composegears.valkyrie

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Valkyrie",
    ) {
        ValkyrieApp()
    }
}
