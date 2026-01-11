@file:Suppress("ktlint:standard:argument-list-wrapping", "ktlint:standard:annotation")

package io.github.composegears.valkyrie.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.Valkyrie"

@Composable
@ReadOnlyComposable
fun stringResource(@PropertyKey(resourceBundle = BUNDLE) key: String): String {
    return ValkyrieBundle.message(key)
}

private object ValkyrieBundle : DynamicBundle(BUNDLE) {

    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) = getMessage(key, *params)
}
