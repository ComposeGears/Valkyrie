package io.github.composegears.valkyrie.gradle.internal

import io.github.composegears.valkyrie.gradle.ValkyrieExtension
import io.github.composegears.valkyrie.gradle.dsl.conventionCompat
import io.github.composegears.valkyrie.gradle.internal.task.GenerateImageVectorsTask
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

/**
 * Core task registration used by both Kotlin (JVM/KMP) and Android (AGP 9.0 built-in Kotlin) paths.
 *
 * @param sourceSetName name of the source set (e.g. "main", "debug", "commonMain")
 * @param addGeneratedSrcDir callback that wires the generated output directory into the
 *        appropriate compilation so the Kotlin compiler picks it up.
 */
internal fun registerTask(
    project: Project,
    extension: ValkyrieExtension,
    sourceSetName: String,
    addGeneratedSrcDir: (Provider<*>) -> Unit,
) {
    val taskName = "${TASK_NAME}${sourceSetName.capitalized()}"
    project.tasks.register(taskName, GenerateImageVectorsTask::class.java) { task ->
        task.description = "Converts SVG & Drawable files into ImageVector Kotlin accessor properties"

        val resourceDirName = extension.resourceDirectoryName
        val iconFiles = project.findIconFiles(sourceSetName, resourceDirName)
        task.iconFiles.conventionCompat(iconFiles)
        task.onlyIf("Needs at least one input file or iconPack") {
            !iconFiles.isEmpty ||
                (extension.iconPack.isPresent && sourceSetName == extension.iconPack.get().targetSourceSet.get())
        }

        task.packageName.convention(extension.packageName)

        val outputRoot = extension.outputDirectory
        val perSourceSetDir = outputRoot.map { it.dir("$sourceSetName/kotlin") }
        task.outputDirectory.convention(perSourceSetDir)
        addGeneratedSrcDir(perSourceSetDir)

        task.outputFormat.convention(extension.imageVector.outputFormat)
        task.useComposeColors.convention(extension.imageVector.useComposeColors)
        task.generatePreview.convention(extension.imageVector.generatePreview)
        task.useExplicitMode.convention(extension.codeStyle.useExplicitMode)
        task.addTrailingComma.convention(extension.imageVector.addTrailingComma)
        task.indentSize.convention(extension.codeStyle.indentSize)
        task.usePathDataString.convention(extension.imageVector.usePathDataString)
        task.suppressUnusedReceiverWarning.convention(extension.imageVector.suppressUnusedReceiverWarning)
        task.autoMirror.convention(extension.autoMirror)

        task.sourceSet.convention(sourceSetName)
        task.iconPack.convention(extension.iconPack)
    }
}

/** Convenience overload for Kotlin (JVM / KMP) source sets. */
internal fun registerTask(
    project: Project,
    extension: ValkyrieExtension,
    sourceSet: KotlinSourceSet,
) {
    registerTask(
        project = project,
        extension = extension,
        sourceSetName = sourceSet.name,
        addGeneratedSrcDir = { dir -> sourceSet.kotlin.srcDir(dir) },
    )
}

/**
 * Locates icon files for [sourceSetName] using the standard src/<sourceSetName>/<resourceDir> convention.
 * This is reliable across JVM, KMP, and AGP 9.0 built-in Kotlin where kotlin.srcDirs may be empty
 * at configuration time.
 */
private fun Project.findIconFiles(sourceSetName: String, resourceDirectoryName: Property<String>): FileCollection {
    val resourceDirProvider = resourceDirectoryName.map { dirName ->
        layout.projectDirectory.dir("src/$sourceSetName").dir(dirName)
    }
    return files(
        resourceDirProvider.map { dir -> dir.asFileTree.filter { it.extension == "svg" || it.extension == "xml" } },
    )
}
