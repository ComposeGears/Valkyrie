package io.github.composegears.valkyrie.theme

import androidx.compose.runtime.compositionLocalOf
import com.intellij.openapi.project.Project
import java.awt.Component

val LocalProject = compositionLocalOf<Project> { error("LocalProject not provided") }
val LocalComponent = compositionLocalOf<Component> { error("LocalComponent not provided")  }
