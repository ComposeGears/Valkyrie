@file:Suppress("ktlint:standard:argument-list-wrapping", "ktlint:standard:annotation")

package io.github.composegears.valkyrie.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE_NAME = "messages.Valkyrie"

@Composable
@ReadOnlyComposable
fun stringResource(key: @PropertyKey(resourceBundle = BUNDLE_NAME) String): String {
    if (LocalInspectionMode.current) return key

    return ValkyrieBundle.message(key)
}

object ValkyrieStrings {

    fun getString(key: @PropertyKey(resourceBundle = BUNDLE_NAME) String): String {
        return ValkyrieBundle.message(key)
    }
}

private object ValkyrieBundle : DynamicBundle(BUNDLE_NAME) {

    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE_NAME) key: String, vararg params: Any) = getMessage(key, *params)
}
