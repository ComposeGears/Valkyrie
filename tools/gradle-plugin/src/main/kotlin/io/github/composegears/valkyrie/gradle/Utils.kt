package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
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

internal fun KotlinSourceSet.defaultOutputDir() = project
    .layout
    .buildDirectory
    .dir("generated/sources/valkyrie/$name")

internal fun KotlinSourceSet.findSvgFiles(): FileCollection = project
    .layout
    .projectDirectory
    .dir("src/$name/svg")
    .asFileTree
    .filter { it.extension == "svg" }

internal fun KotlinSourceSet.findDrawableFiles(): FileCollection = project
    .layout
    .projectDirectory
    .dir("src/$name/res")
    .asFileTree
    .filter { it.parentFile?.name.orEmpty().contains("drawable") && it.extension == "xml" }

internal fun Project.isAndroid(): Boolean = plugins.hasPlugin("com.android.base")

private const val COMPOSE_UI = "androidx.compose.ui:ui:${BuildConfig.COMPOSE_UI_VERSION}"

internal fun Project.addDependencies(sourceSet: KotlinSourceSet, isMultiplatform: Boolean) {
    if (isMultiplatform) {
        extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
            sourceSets.getByName(sourceSet.name).apply {
                dependencies { api(COMPOSE_UI) }
            }
        }
    } else {
        val config = when (sourceSet.name) {
            "main" -> "api"
            else -> "${sourceSet.name}Api"
        }
        dependencies.apply {
            add(config, COMPOSE_UI)
        }
    }
}
