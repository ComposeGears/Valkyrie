package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import io.github.composegears.valkyrie.gradle.GenerateSvgImageVectorTask.Companion.TASK_NAME
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.SourceSet
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

@Suppress("unused") // Registered as a Gradle plugin.
class ValkyrieGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val extension = extensions.create("valkyrie", ValkyrieExtension::class.java)

        val svgTask = tasks.register(TASK_NAME, GenerateSvgImageVectorTask::class.java) { task ->
            task.group = LifecycleBasePlugin.BUILD_GROUP
            task.description = "Converts SVG files into generated ImageVector Kotlin accessor properties"
            task.svgFiles.conventionCompat(findSvgFiles())
            task.drawableFiles.conventionCompat(findDrawableFiles())
            task.packageName.set(extension.packageName.orNull ?: estimatePackageNameOrThrow())
            task.config.set(extension.config)
            task.outputSourceSet.set(extension.outputSourceSet.orNull ?: estimateSourceSet())
            val defaultOutputDir = layout.buildDirectory.dir("generated/sources/valkyrie")
            task.outputDirectory.set(extension.outputDirectory.orNull ?: defaultOutputDir.get())
        }

        afterEvaluate {
            // Run generation immediately if we're syncing Intellij/Android Studio - helps to speed up dev cycle
            val isIdeSyncing = System.getProperty("idea.sync.active") == "true"
            if (extension.generateAtSync.getOrElse(false) && isIdeSyncing) {
                tasks.findByName("prepareKotlinIdeaImport")?.dependsOn(svgTask)
            }

            // Run generation before any kind of kotlin source processing
            tasks.withType(AbstractKotlinCompile::class.java).configureEach { it.dependsOn(svgTask) }
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
                .findByType(CommonExtension::class.java)
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

    private fun Project.findSvgFiles(): FileCollection = layout.projectDirectory
        .dir("src/${estimateSourceSet()}/svg")
        .asFileTree
        .filter { it.extension == "svg" }

    private fun Project.findDrawableFiles(): FileCollection = layout.projectDirectory
        .dir("src/${estimateSourceSet()}/res")
        .asFileTree
        .filter { it.parentFile?.name.orEmpty().contains("drawable") && it.extension == "xml" }
}

private val NO_PACKAGE_NAME_ERROR = """
    Couldn't automatically estimate package name - make sure to set this property in your gradle script like:

    valkyrie {
        packageName = "my.output.package.name"
    }
""".trimIndent()
