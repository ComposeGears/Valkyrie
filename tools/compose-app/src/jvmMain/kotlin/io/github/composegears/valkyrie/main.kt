@file:Suppress("ktlint:standard:filename")

package io.github.composegears.valkyrie

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit

fun main() {
    System.setProperty("apple.awt.application.appearance", "system")

    application {
        FileKit.init("Valkyrie")

        Window(
            onCloseRequest = ::exitApplication,
            title = "Valkyrie",
        ) {
            ValkyrieApp()
        }
    }
}
