package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import java.nio.file.Path as JPath
import kotlinx.io.files.Path as KPath
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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

@CacheableTask
abstract class GenerateImageVectorsTask : DefaultTask() {
    @get:[PathSensitive(ABSOLUTE) InputFiles] abstract val svgFiles: ConfigurableFileCollection
    @get:[PathSensitive(ABSOLUTE) InputFiles] abstract val drawableFiles: ConfigurableFileCollection

    @get:Input abstract val packageName: Property<String>
    @get:[Input Optional] abstract val iconPackName: Property<String>
    @get:[Input Optional] abstract val nestedPackName: Property<String>
    @get:Input abstract val outputFormat: Property<OutputFormat>
    @get:Input abstract val useComposeColors: Property<Boolean>
    @get:Input abstract val generatePreview: Property<Boolean>
    @get:Input abstract val previewAnnotationType: Property<PreviewAnnotationType>
    @get:Input abstract val useFlatPackage: Property<Boolean>
    @get:Input abstract val useExplicitMode: Property<Boolean>
    @get:Input abstract val addTrailingComma: Property<Boolean>
    @get:Input abstract val indentSize: Property<Int>

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
        val useFlatPackage = useFlatPackage.get()
        val nestedPackName = nestedPackName.getOrElse("")
        val config = ImageVectorGeneratorConfig(
            packageName = packageName,
            iconPackPackage = packageName,
            packName = iconPackName.getOrElse(""),
            nestedPackName = nestedPackName,
            outputFormat = outputFormat.get(),
            useComposeColors = useComposeColors.get(),
            generatePreview = generatePreview.get(),
            previewAnnotationType = previewAnnotationType.get(),
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode.get(),
            addTrailingComma = addTrailingComma.get(),
            indentSize = indentSize.get(),
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

    internal companion object {
        internal const val TASK_NAME = "generateImageVectors"
    }
}
