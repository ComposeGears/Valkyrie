package io.github.composegears.valkyrie.gradle

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.util.GradleVersion

internal fun ConfigurableFileCollection.conventionCompat(paths: Iterable<*>): ConfigurableFileCollection {
    return if (GradleVersion.current() >= GradleVersion.version("8.8")) {
        @Suppress("UnstableApiUsage")
        convention(paths)
    } else {
        setFrom(paths)
        this
    }
}
