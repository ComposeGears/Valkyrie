package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import java.nio.file.Path as JPath
import kotlinx.io.files.Path as KPath
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.ABSOLUTE
import org.gradle.api.tasks.TaskAction
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

@CacheableTask
abstract class GenerateImageVectorsTask : DefaultTask() {
    @get:[PathSensitive(ABSOLUTE) InputFiles] abstract val svgFiles: ConfigurableFileCollection
    @get:[PathSensitive(ABSOLUTE) InputFiles] abstract val drawableFiles: ConfigurableFileCollection

    @get:[Input Optional] abstract val packageName: Property<String>
    @get:[Input Optional] abstract val iconPackName: Property<String>
    @get:[Input Optional] abstract val nestedPackName: Property<String>
    @get:[Input Optional] abstract val outputFormat: Property<OutputFormat>
    @get:[Input Optional] abstract val useComposeColors: Property<Boolean>
    @get:[Input Optional] abstract val generatePreview: Property<Boolean>
    @get:[Input Optional] abstract val previewAnnotationType: Property<PreviewAnnotationType>
    @get:[Input Optional] abstract val useFlatPackage: Property<Boolean>
    @get:[Input Optional] abstract val useExplicitMode: Property<Boolean>
    @get:[Input Optional] abstract val addTrailingComma: Property<Boolean>
    @get:[Input Optional] abstract val indentSize: Property<Int>

    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun execute() {
        val packageName = packageName.orNull
            ?: throw GradleException("No package name configured for $this")

        // e.g. "<project-root>/build/generated/sources/valkyrie/main"
        val outputDirectory = outputDirectory.get().asFile
        outputDirectory.deleteRecursively() // make sure nothing is left over from previous run
        outputDirectory.mkdirs()

        val generatedFiles = arrayListOf<JPath>()
        var fileIndex = 0

        // Using the same defaults as `SvgXmlToImageVectorCommand` in tools/cli.
        val useFlatPackage = useFlatPackage.getOrElse(false)
        val nestedPackName = nestedPackName.getOrElse("")
        val config = ImageVectorGeneratorConfig(
            packageName = packageName,
            iconPackPackage = packageName,
            packName = iconPackName.getOrElse(""),
            nestedPackName = nestedPackName,
            outputFormat = outputFormat.getOrElse(OutputFormat.BackingProperty),
            useComposeColors = useComposeColors.getOrElse(true),
            generatePreview = generatePreview.getOrElse(false),
            previewAnnotationType = previewAnnotationType.getOrElse(PreviewAnnotationType.AndroidX),
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode.getOrElse(false),
            addTrailingComma = addTrailingComma.getOrElse(false),
            indentSize = indentSize.getOrElse(4),
        )

        (svgFiles + drawableFiles).files.forEach { file ->
            val parseOutput = SvgXmlParser.toIrImageVector(ParserType.Jvm, KPath(file.absolutePath))
            val vectorSpecOutput = ImageVectorGenerator.convert(
                vector = parseOutput.irImageVector,
                iconName = parseOutput.iconName,
                config = config,
            )

            val path = vectorSpecOutput.content.writeToKt(
                outputDir = when {
                    useFlatPackage -> outputDirectory
                    else -> outputDirectory.resolve(nestedPackName.lowercase())
                }.absolutePath,
                nameWithoutExtension = vectorSpecOutput.name,
            )
            generatedFiles.add(path)
            fileIndex++
            logger.info("File $fileIndex = $path")
        }

        logger.lifecycle("Generated ${generatedFiles.size} ImageVectors in package $packageName")
    }

    companion object {
        const val TASK_NAME = "generateImageVectors"
        const val TASK_GROUP = LifecycleBasePlugin.BUILD_GROUP

        fun register(
            target: Project,
            extension: ValkyrieExtension,
            sourceSet: KotlinSourceSet,
        ) {
            val sourceSetIsEmpty = sourceSet.root()
                .asFileTree
                .filter { it.isFile }
                .isEmpty

            if (sourceSetIsEmpty) {
                // nothing at all in the source set - nothing to work with so no task needs generating
                target.logger.info("Source set ${sourceSet.name} is empty - skipping registration of codegen task")
                return
            }

            val taskName = "${TASK_NAME}${sourceSet.name.capitalized()}"
            target.tasks.register(taskName, GenerateImageVectorsTask::class.java) { task ->
                task.group = TASK_GROUP
                task.description = "Converts SVG & Drawable files into ImageVector Kotlin accessor properties"

                task.svgFiles.conventionCompat(sourceSet.findSvgFiles())
                task.drawableFiles.conventionCompat(sourceSet.findDrawableFiles())
                task.packageName.convention(extension.packageName.orElse(target.packageNameOrThrow()))

                val outputDir = extension.outputDirectory.orElse(target.defaultOutputDir(sourceSet))
                task.outputDirectory.convention(outputDir)
                sourceSet.kotlin.srcDir(outputDir)

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
    }
}
