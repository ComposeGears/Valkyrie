package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

private val NO_PACKAGE_NAME_ERROR = """
    Couldn't automatically estimate package name - make sure to set this property in your gradle script like:

    valkyrie {
        packageName = "my.output.package.name"
    }
""".trimIndent()

internal fun ConfigurableFileCollection.conventionCompat(
    paths: Iterable<*>,
): ConfigurableFileCollection = if (GradleVersion.current() >= GradleVersion.version("8.8")) {
    @Suppress("UnstableApiUsage")
    convention(paths)
} else {
    setFrom(paths)
    this
}

private val ANDROID_PLUGIN_IDS = listOf(
    "com.android.application",
    "com.android.library",
    "com.android.test",
    "com.android.dynamic-feature",
)

internal fun Project.packageNameOrThrow(): Provider<String?> = provider {
    if (ANDROID_PLUGIN_IDS.any(pluginManager::hasPlugin)) {
        extensions
            .findByType(CommonExtension::class.java)
            ?.namespace
            ?: throw GradleException(NO_PACKAGE_NAME_ERROR)
    } else {
        throw GradleException(NO_PACKAGE_NAME_ERROR)
    }
}

internal fun Project.defaultOutputDir(sourceSet: KotlinSourceSet) = project
    .layout
    .buildDirectory
    .dir("generated/sources/valkyrie/${sourceSet.name}")

internal fun KotlinSourceSet.root(): Directory = project
    .layout
    .projectDirectory
    .dir("src/$name")

internal fun KotlinSourceSet.findSvgFiles(): FileCollection = root()
    .dir("svg")
    .asFileTree
    .filter { it.extension == "svg" }

internal fun KotlinSourceSet.findDrawableFiles(): FileCollection = root()
    .dir("res")
    .asFileTree
    .filter { it.parentFile?.name.orEmpty().contains("drawable") && it.extension == "xml" }
