@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui.foundation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.intellij.openapi.project.Project
import java.awt.Component

val LocalProject = staticCompositionLocalOf<Project> { error("LocalProject not provided") }
val LocalComponent = staticCompositionLocalOf<Component> { error("LocalComponent not provided") }
