@file:Suppress("ktlint:compose:compositionlocal-allowlist")

package io.github.composegears.valkyrie.ui.foundation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.intellij.openapi.project.Project

val LocalProject = staticCompositionLocalOf<ProjectWrapper> { ProjectWrapper.Stub }

/**
 * Wrapper for the IntelliJ [Project] instance to avoid "class not found exception" in Compose Preview functions
 */
sealed interface ProjectWrapper {
    val current: Project

    class Platform(override val current: Project) : ProjectWrapper

    data object Stub : ProjectWrapper {
        override val current: Project
            get() = error("Stub wrapper should be used only for Preview")
    }
}
