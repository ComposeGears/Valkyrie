package io.github.composegears.valkyrie.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.ide.BrowserUtil

@Composable
fun rememberBrowser(): Browser {
    if (LocalInspectionMode.current) return NoOpBrowser

    return BrowserImpl
}

interface Browser {
    fun open(url: String)
}

private object NoOpBrowser : Browser {
    override fun open(url: String) = Unit
}

private object BrowserImpl : Browser {
    override fun open(url: String) = BrowserUtil.browse(url)
}
