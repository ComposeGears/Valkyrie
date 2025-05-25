package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import io.github.composegears.valkyrie.gradle.GenerateSvgImageVectorTask.Companion.TASK_NAME
import java.io.File
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

class ValkyrieGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create(name = "valkyrie", type = ValkyrieExtension::class)

        val svgTask = tasks.register<GenerateSvgImageVectorTask>(TASK_NAME) {
            group = "build"
            description = "Converts SVG files into generated ImageVector Kotlin accessor properties"
            svgFiles.setFrom(findSvgFiles())
            drawableFiles.setFrom(findDrawableFiles())
            packageName.set(extension.packageName.orNull ?: estimatePackageNameOrThrow())
            config.set(extension.config)
            outputSourceSet.set(extension.outputSourceSet.orNull ?: estimateSourceSet())
            outputDirectory.set(extension.outputDirectory)
        }

        afterEvaluate {
            // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
            val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
            if (extension.generateAtSync.get() && isIdeSyncing) {
                tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(svgTask)
            }

            // Run generation before any kind of kotlin source processing
            tasks.withType(AbstractKotlinCompile::class).configureEach { it.dependsOn(svgTask) }
        }
    }

    private fun Project.estimatePackageNameOrThrow(): String {
        val androidPluginIds = listOf(
            "com.android.application",
            "com.android.library",
            "com.android.test",
        )
        if (androidPluginIds.any(pluginManager::hasPlugin)) {
            return extensions
                .findByType(CommonExtension::class)
                ?.namespace
                ?: throw GradleException(NO_PACKAGE_NAME_ERROR)
        }
        throw GradleException(NO_PACKAGE_NAME_ERROR)
    }

    private fun Project.estimateSourceSet() = if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME
    } else {
        SourceSet.MAIN_SOURCE_SET_NAME
    }

    private fun Project.findSvgFiles(): Set<File> = layout.projectDirectory
        .dir("src/${estimateSourceSet()}/svg")
        .asFileTree
        .filter { it.extension == "svg" }
        .files

    private fun Project.findDrawableFiles(): Set<File> = layout.projectDirectory
        .dir("src/${estimateSourceSet()}/res")
        .asFileTree
        .filter { it.parentFile?.name.orEmpty().contains("drawable") && it.extension == "xml" }
        .files
}

private val NO_PACKAGE_NAME_ERROR = """
    Couldn't automatically estimate package name - make sure to set this property in your gradle script like:

    valkyrie {
        packageName = "my.output.package.name"
    }
""".trimIndent()
