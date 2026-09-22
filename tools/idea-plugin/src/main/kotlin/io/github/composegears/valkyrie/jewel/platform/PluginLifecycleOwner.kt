package io.github.composegears.valkyrie.jewel.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * IntelliJ 2026.2.3 bundles `androidx.lifecycle.compose` twice — `intellij.libraries.compose.runtime.desktop`
 * ships the non-Android build, `intellij.libraries.compose.foundation.desktop` an Android one. The Compose
 * scene provides the copy its own module resolves, while plugin code reads the other one, so
 * `LocalLifecycleOwner` appears unset. Providing the owner from the plugin classloader keeps provider and
 * consumer on the same copy.
 */
@Composable
fun rememberPluginLifecycleOwner(): LifecycleOwner {
    val lifecycleOwner = remember { PluginLifecycleOwner() }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.currentState = Lifecycle.State.RESUMED
        onDispose {
            lifecycleOwner.lifecycle.currentState = Lifecycle.State.DESTROYED
        }
    }

    return lifecycleOwner
}

private class PluginLifecycleOwner : LifecycleOwner {
    override val lifecycle: LifecycleRegistry = LifecycleRegistry.createUnsafe(this)
}
