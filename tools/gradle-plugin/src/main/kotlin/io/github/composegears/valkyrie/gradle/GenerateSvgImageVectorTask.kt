package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import java.nio.file.Path as JPath
import kotlinx.io.files.Path as KPath
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class GenerateSvgImageVectorTask : DefaultTask() {
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    @get:InputFiles
    abstract val svgFiles: ConfigurableFileCollection

    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    @get:InputFiles
    abstract val drawableFiles: ConfigurableFileCollection

    @get:Input abstract val packageName: Property<String>

    @get:Input abstract val outputSourceSet: Property<String>

    @get:Nested abstract val config: Property<ValkyrieConfig>

    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun execute() = with(config.get()) {
        val packageName = packageName.get()
        val outputDirectory = outputDirectory.get().asFile
        outputDirectory.deleteRecursively() // make sure nothing is left over from previous run
        outputDirectory.mkdirs()

        // e.g. "<project-root>/build/generated/sources/valkyrie/main"
        val sourceSetDirectory = outputDirectory.resolve(outputSourceSet.get())

        val generatedFiles = arrayListOf<JPath>()
        var fileIndex = 0

        (svgFiles + drawableFiles).files.forEach { file ->
            val parseOutput = SvgXmlParser.toIrImageVector(ParserType.Jvm, KPath(file.absolutePath))
            val config = ImageVectorGeneratorConfig(
                packageName = packageName,
                iconPackPackage = packageName,
                packName = iconPackName,
                nestedPackName = nestedPackName,
                outputFormat = outputFormat,
                useComposeColors = useComposeColors,
                generatePreview = generatePreview,
                previewAnnotationType = previewAnnotationType,
                useFlatPackage = useFlatPackage,
                useExplicitMode = useExplicitMode,
                addTrailingComma = addTrailingComma,
                indentSize = indentSize,
            )
            val vectorSpecOutput = ImageVectorGenerator.convert(
                vector = parseOutput.irImageVector,
                iconName = parseOutput.iconName,
                config = config,
            )

            val path = vectorSpecOutput.content.writeToKt(
                outputDir = when {
                    useFlatPackage -> sourceSetDirectory
                    else -> sourceSetDirectory.resolve(nestedPackName.lowercase())
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
        const val TASK_NAME = "generateSvgImageVector"
    }
}
