package io.github.composegears.valkyrie.ui.screen.intro.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

@Composable
fun rememberPluginVersion(): String {
  if (LocalInspectionMode.current) return "1.0.0"

  return remember {
    val pluginId = PluginId.getId("io.github.composegears.valkyrie")
    PluginManagerCore.getPlugin(pluginId)?.version.toString()
  }
}
