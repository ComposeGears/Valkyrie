package io.github.composegears.valkyrie.gradle.internal

import io.github.composegears.valkyrie.gradle.ValkyrieExtension
import io.github.composegears.valkyrie.gradle.dsl.conventionCompat
import io.github.composegears.valkyrie.gradle.internal.task.GenerateImageVectorsTask
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
    val sourceSetName = sourceSet.name
    project.tasks.register(taskName, GenerateImageVectorsTask::class.java) { task ->
        task.description = "Converts SVG & Drawable files into ImageVector Kotlin accessor properties"

        val resourceDirName = extension.resourceDirectoryName
        val iconFiles = sourceSet.findIconFiles(resourceDirName)
        task.iconFiles.conventionCompat(iconFiles)
        task.onlyIf("Needs at least one input file or iconPack") {
            !iconFiles.isEmpty ||
                (extension.iconPack.isPresent && sourceSetName == extension.iconPack.get().targetSourceSet.get())
        }

        task.packageName.convention(extension.packageName)

        val outputRoot = extension.outputDirectory
        val perSourceSetDir = outputRoot.map { it.dir(sourceSet.name) }
        task.outputDirectory.convention(perSourceSetDir)
        sourceSet.kotlin.srcDir(perSourceSetDir)

        task.outputFormat.convention(extension.imageVector.outputFormat)
        task.useComposeColors.convention(extension.imageVector.useComposeColors)
        task.generatePreview.convention(extension.imageVector.generatePreview)
        task.previewAnnotationType.convention(extension.imageVector.previewAnnotationType)
        task.useExplicitMode.convention(extension.codeStyle.useExplicitMode)
        task.addTrailingComma.convention(extension.imageVector.addTrailingComma)
        task.indentSize.convention(extension.codeStyle.indentSize)

        task.sourceSet.convention(sourceSet.name)
        task.iconPack.convention(extension.iconPack)
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
