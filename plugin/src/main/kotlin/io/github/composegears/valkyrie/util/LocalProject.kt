package io.github.composegears.valkyrie.util

import androidx.compose.runtime.compositionLocalOf
import com.intellij.openapi.project.Project

val LocalProject = compositionLocalOf<Project> { error("LocalProject not provided") }
