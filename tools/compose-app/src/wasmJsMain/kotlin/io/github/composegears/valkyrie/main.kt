@file:Suppress("ktlint:standard:filename")

package io.github.composegears.valkyrie

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        LaunchedEffect(Unit) {
            hideLoader()
        }
        ValkyrieApp()
    }
}

private fun hideLoader() {
    document.getElementById("loader").safeAs<HTMLElement>()?.run { style.display = "none" }
    document.getElementById("app").safeAs<HTMLElement>()?.run { style.display = "block" }
}
