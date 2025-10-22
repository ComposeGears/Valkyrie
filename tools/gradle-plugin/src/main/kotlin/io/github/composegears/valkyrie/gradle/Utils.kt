package io.github.composegears.valkyrie.gradle

import com.android.build.api.dsl.CommonExtension
import io.github.composegears.valkyrie.gradle.GenerateImageVectorsTask.Companion.TASK_NAME
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun registerTask(
    target: Project,
    extension: ValkyrieExtension,
    sourceSet: KotlinSourceSet,
) {
    val taskName = "${TASK_NAME}${sourceSet.name.capitalized()}"
    target.tasks.register(taskName, GenerateImageVectorsTask::class.java) { task ->
        task.description = "Converts SVG & Drawable files into ImageVector Kotlin accessor properties"

        val svgFiles = sourceSet.findSvgFiles()
        val drawableFiles = sourceSet.findDrawableFiles()
        task.svgFiles.conventionCompat(svgFiles)
        task.drawableFiles.conventionCompat(drawableFiles)
        task.onlyIf("Needs at least one input file") {
            !svgFiles.isEmpty || !drawableFiles.isEmpty
        }

        task.packageName.convention(extension.packageName.orElse(target.packageNameOrThrow()))

        val outputRoot = extension.outputDirectory
        val perSourceSetDir = outputRoot.map { it.dir(sourceSet.name) }
        task.outputDirectory.convention(perSourceSetDir)
        sourceSet.kotlin.srcDir(perSourceSetDir)

        task.iconPackName.convention(extension.iconPackName)
        task.nestedPackName.convention(extension.nestedPackName)
        task.outputFormat.convention(extension.outputFormat)
        task.useComposeColors.convention(extension.useComposeColors)
        task.generatePreview.convention(extension.generatePreview)
        task.previewAnnotationType.convention(extension.previewAnnotationType)
        task.useFlatPackage.convention(extension.useFlatPackage)
        task.useExplicitMode.convention(extension.useExplicitMode)
        task.addTrailingComma.convention(extension.addTrailingComma)
        task.indentSize.convention(extension.indentSize)
    }
}

private val NO_PACKAGE_NAME_ERROR = """
    Couldn't automatically estimate package name - make sure to set this property in your gradle script like:

    valkyrie {
        packageName = "my.output.package.name"
    }
""".trimIndent()

private fun ConfigurableFileCollection.conventionCompat(
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

internal fun Project.packageNameOrThrow(): Provider<String> = provider {
    if (ANDROID_PLUGIN_IDS.any(pluginManager::hasPlugin)) {
        extensions
            .findByType(CommonExtension::class.java)
            ?.namespace
            ?: throw GradleException(NO_PACKAGE_NAME_ERROR)
    } else {
        throw GradleException(NO_PACKAGE_NAME_ERROR)
    }
}

private fun KotlinSourceSet.root(): Directory = with(project) {
    // kotlin.srcDirs returns a set like ["src/main/kotlin", "src/main/java"] - we want the "src/main" directory.
    val src = provider {
        kotlin.srcDirs
            .firstOrNull()
            ?.resolve("..")
            ?: error("No srcDir found for source set $name")
    }
    return layout.dir(src).get()
}

private fun KotlinSourceSet.findSvgFiles(): FileCollection = root()
    .dir("svg")
    .asFileTree
    .filter { it.extension == "svg" }

private fun KotlinSourceSet.findDrawableFiles(): FileCollection = root()
    .dir("res")
    .asFileTree
    .filter { it.parentFile?.name.orEmpty().contains("drawable") && it.extension == "xml" }
