package io.github.composegears.valkyrie.gradle.internal

import io.github.composegears.valkyrie.gradle.ValkyrieExtension
import io.github.composegears.valkyrie.gradle.dsl.conventionCompat
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun registerTask(
    project: Project,
    extension: ValkyrieExtension,
    sourceSet: KotlinSourceSet,
) {
    val taskName = "${TASK_NAME}${sourceSet.name.capitalized()}"
    project.tasks.register(taskName, GenerateImageVectorsTask::class.java) { task ->
        task.description = "Converts SVG & Drawable files into ImageVector Kotlin accessor properties"

        val resourceDirName = extension.resourceDirectoryName
        val iconFiles = sourceSet.findIconFiles(resourceDirName)
        task.iconFiles.conventionCompat(iconFiles)
        task.onlyIf("Needs at least one input file") { !iconFiles.isEmpty }

        task.packageName.convention(extension.packageName)

        val outputRoot = extension.outputDirectory
        val perSourceSetDir = outputRoot.map { it.dir(sourceSet.name) }
        task.outputDirectory.convention(perSourceSetDir)
        sourceSet.kotlin.srcDir(perSourceSetDir)

        task.iconPackName.convention(extension.iconPackName)
        task.nestedPackName.convention(extension.nestedPackName)
        task.outputFormat.convention(extension.imageVector.outputFormat)
        task.useComposeColors.convention(extension.imageVector.useComposeColors)
        task.generatePreview.convention(extension.imageVector.generatePreview)
        task.previewAnnotationType.convention(extension.imageVector.previewAnnotationType)
        task.useFlatPackage.convention(extension.useFlatPackage)
        task.useExplicitMode.convention(extension.imageVector.useExplicitMode)
        task.addTrailingComma.convention(extension.imageVector.addTrailingComma)
        task.indentSize.convention(extension.imageVector.indentSize)
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

private fun KotlinSourceSet.findIconFiles(resourceDirectoryName: Property<String>): FileCollection {
    return root()
        .dir(resourceDirectoryName.get())
        .asFileTree
        .filter {
            it.extension == "svg" || it.extension == "xml"
        }
}
