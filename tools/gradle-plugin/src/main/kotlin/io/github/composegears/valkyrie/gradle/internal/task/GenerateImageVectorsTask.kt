package io.github.composegears.valkyrie.gradle.internal.task

import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.FullQualifiedImports
import io.github.composegears.valkyrie.generator.jvm.imagevector.FullQualifiedImports.Companion.reservedComposeQualifiers
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.gradle.IconPackExtension
import io.github.composegears.valkyrie.gradle.NestedPack
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import java.io.File
import kotlinx.io.files.Path
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
internal abstract class GenerateImageVectorsTask : DefaultTask() {
    @get:[PathSensitive(PathSensitivity.RELATIVE) InputFiles]
    abstract val iconFiles: ConfigurableFileCollection

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val outputFormat: Property<OutputFormat>

    @get:Input
    abstract val useComposeColors: Property<Boolean>

    @get:Input
    abstract val generatePreview: Property<Boolean>

    @get:Input
    abstract val useExplicitMode: Property<Boolean>

    @get:Input
    abstract val addTrailingComma: Property<Boolean>

    @get:Input
    abstract val indentSize: Property<Int>

    @get:Input
    abstract val usePathDataString: Property<Boolean>

    @get:Optional
    @get:Input
    abstract val autoMirror: Property<Boolean>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Optional
    @get:Nested
    abstract val iconPack: Property<IconPackExtension>

    @get:Input
    abstract val sourceSet: Property<String>

    private val basicConfig: ImageVectorGeneratorConfig
        get() = ImageVectorGeneratorConfig(
            packageName = packageName.get(),
            iconPackPackage = packageName.get(),
            packName = "",
            nestedPackName = "",
            outputFormat = outputFormat.get(),
            useComposeColors = useComposeColors.get(),
            generatePreview = generatePreview.get(),
            useFlatPackage = false,
            useExplicitMode = useExplicitMode.get(),
            addTrailingComma = addTrailingComma.get(),
            usePathDataString = usePathDataString.get(),
            indentSize = indentSize.get(),
        )

    @TaskAction
    fun execute() {
        val startTime = System.currentTimeMillis()
        val packageNameValue = packageName.orNull ?: throw GradleException("No package name configured for $this")

        logger.info("Starting ImageVector generation for source set '${sourceSet.get()}'")
        logger.info("Package name: $packageNameValue")
        logger.info("Output format: ${outputFormat.get()}")

        // e.g. "<project-root>/build/generated/sources/valkyrie/main"
        val outputDirectory = outputDirectory.get().asFile
        outputDirectory.deleteRecursively() // make sure nothing is left over from previous run
        outputDirectory.mkdirs()

        // Detect icons with names conflicting with reserved Compose qualifiers
        val iconNames = iconFiles.files.map { IconNameFormatter.format(name = it.name) }

        val fullQualifiedNames = iconNames.filter { reservedComposeQualifiers.contains(it) }

        if (fullQualifiedNames.isNotEmpty()) {
            logger.lifecycle(
                "Found icons names that conflict with reserved Compose qualifiers. " +
                    "Full qualified import will be used for: \"${fullQualifiedNames.joinToString(", ")}\"",
            )
        }

        // Check for case-insensitive duplicates that would cause file overwrites on case-insensitive file systems
        val caseInsensitiveDuplicates = iconNames
            .groupBy { it.lowercase() }
            .filter { it.value.size > 1 && it.value.distinct().size > 1 }
            .values
            .flatten()
            .distinct()

        if (caseInsensitiveDuplicates.isNotEmpty()) {
            throw GradleException(
                "Found icon names that would collide on case-insensitive file systems (macOS/Windows): " +
                    "${caseInsensitiveDuplicates.joinToString(", ")}. " +
                    "These icons would overwrite each other during generation. " +
                    "Please rename the source files to avoid case-insensitive duplicates.",
            )
        }

        if (iconPack.isPresent && iconPack.get().targetSourceSet.get() == sourceSet.get()) {
            generateIconPack(outputDirectory = outputDirectory)
        }

        if (iconPack.isPresent) {
            generateIconsWithIconPack(
                outputDirectory = outputDirectory,
                fullQualifiedNames = fullQualifiedNames,
            )
        } else {
            generateIconsWithoutPack(
                outputDirectory = outputDirectory,
                fullQualifiedNames = fullQualifiedNames,
            )
        }

        val executionTime = System.currentTimeMillis() - startTime
        logger.info("ImageVector generation completed in ${executionTime}ms")
    }

    private fun generateIconPack(outputDirectory: File) {
        val packageName = packageName.get()
        val iconPackExtension = iconPack.get()

        val pack = IconPack(
            name = iconPackExtension.name.get(),
            nested = iconPackExtension.nestedPacks.get().map { nestedPack ->
                IconPack(name = nestedPack.name.get())
            },
        )

        IconPackGenerator.create(
            config = IconPackGeneratorConfig(
                packageName = packageName,
                iconPack = pack,
                useExplicitMode = useExplicitMode.get(),
                indentSize = indentSize.get(),
            ),
        ).also {
            val packagePath = packageName.replace('.', File.separatorChar)
            val absolutePath = outputDirectory.resolve(packagePath).absolutePath

            it.content.writeToKt(
                outputDir = absolutePath,
                nameWithoutExtension = it.name,
            )
            logger.lifecycle("Generated \"${pack.name}\" iconpack in package \"$packageName\"")
        }
    }

    private fun generateIconsWithoutPack(outputDirectory: File, fullQualifiedNames: List<String>) {
        val packageName = packageName.get()

        if (iconFiles.isEmpty) {
            logger.lifecycle("No icon files found for ImageVector generation in package \"$packageName\"")
            return
        }

        val targetDirectory = resolveTargetDirectory(
            outputDirectory = outputDirectory,
            packageName = packageName,
            nestedPackName = "",
            useFlatPackage = false,
        )

        val config = basicConfig.copy(fullQualifiedImports = createFullQualifiedImports(fullQualifiedNames))
        var convertedCount = 0
        iconFiles.files.forEach { file ->
            runCatching {
                processIconFile(
                    file = file,
                    config = config,
                    targetDirectory = targetDirectory,
                    nestedPackName = null,
                )
                convertedCount++
            }.onFailure {
                logFileParseError(file = file, error = it)
            }
        }
        logger.lifecycle("Generated $convertedCount ImageVector ${iconWord(convertedCount)} in package \"$packageName\"")
    }

    private fun generateIconsWithIconPack(outputDirectory: File, fullQualifiedNames: List<String>) {
        val packageName = packageName.get()

        val pack = iconPack.get()
        val nestedPacks = pack.nestedPacks.get()
        val useFlatPackage = pack.useFlatPackage.get()

        val config = basicConfig.copy(
            packName = pack.name.get(),
            fullQualifiedImports = createFullQualifiedImports(fullQualifiedNames),
        )

        if (iconFiles.isEmpty) {
            logger.lifecycle("No icon files to process for ImageVector generation")
            return
        }

        if (nestedPacks.isEmpty()) {
            generateIconsForSinglePack(
                outputDirectory = outputDirectory,
                packageName = packageName,
                config = config,
            )
        } else {
            generateIconsForNestedPacks(
                outputDirectory = outputDirectory,
                packageName = packageName,
                useFlatPackage = useFlatPackage,
                nestedPacks = nestedPacks,
                config = config.copy(useFlatPackage = useFlatPackage),
            )
        }
    }

    private fun generateIconsForSinglePack(
        outputDirectory: File,
        packageName: String,
        config: ImageVectorGeneratorConfig,
    ) {
        val targetDirectory = resolveTargetDirectory(
            outputDirectory = outputDirectory,
            packageName = packageName,
            nestedPackName = "",
            useFlatPackage = false,
        )

        var convertedCount = 0
        iconFiles.files.forEach { file ->
            runCatching {
                processIconFile(
                    file = file,
                    config = config,
                    targetDirectory = targetDirectory,
                    nestedPackName = null,
                )
                convertedCount++
            }.onFailure {
                logFileParseError(file = file, error = it)
            }
        }
        logger.lifecycle("Generated $convertedCount ImageVector ${iconWord(convertedCount)} in package \"$packageName\"")
    }

    private fun generateIconsForNestedPacks(
        outputDirectory: File,
        packageName: String,
        useFlatPackage: Boolean,
        nestedPacks: List<NestedPack>,
        config: ImageVectorGeneratorConfig,
    ) {
        val sourceFolderToNestedPack = nestedPacks.associateBy { it.sourceFolder.get() }
        val nestedPackIconCounts = mutableMapOf<String, Int>()

        iconFiles.files.forEach { file ->
            val parentDirName = file.parentFile.name
            val nestedPack = sourceFolderToNestedPack[parentDirName]

            if (nestedPack != null) {
                val nestedPackName = nestedPack.name.get()
                val nestedPackConfig = config.copy(nestedPackName = nestedPackName)
                val nestedTargetDirectory = resolveTargetDirectory(
                    outputDirectory = outputDirectory,
                    packageName = packageName,
                    nestedPackName = nestedPackName,
                    useFlatPackage = useFlatPackage,
                )

                runCatching {
                    processIconFile(
                        file = file,
                        config = nestedPackConfig,
                        targetDirectory = nestedTargetDirectory,
                        nestedPackName = nestedPackName,
                    )
                    nestedPackIconCounts[nestedPackName] = nestedPackIconCounts.getOrDefault(nestedPackName, 0) + 1
                }.onFailure {
                    logFileParseError(file = file, error = it)
                }
            }
        }

        nestedPackIconCounts.forEach { (nestedPackName, count) ->
            val fullPackage = resolveFullPackageName(packageName, nestedPackName, useFlatPackage)
            logger.lifecycle("Generated $count ImageVector ${iconWord(count)} in nested pack \"$nestedPackName\" (package: \"$fullPackage\")")
        }
    }

    private fun processIconFile(
        file: File,
        config: ImageVectorGeneratorConfig,
        targetDirectory: File,
        nestedPackName: String?,
    ) {
        val parseOutput = SvgXmlParser.toIrImageVector(ParserType.Jvm, Path(file.absolutePath))

        // Apply autoMirror override if specified (nested pack > icon pack > root)
        val effectiveAutoMirror = resolveAutoMirror(nestedPackName)
        val irImageVector = if (effectiveAutoMirror != null) {
            parseOutput.irImageVector.copy(autoMirror = effectiveAutoMirror)
        } else {
            parseOutput.irImageVector
        }

        val vectorSpecOutput = ImageVectorGenerator.convert(
            vector = irImageVector,
            iconName = parseOutput.iconName,
            config = config,
        )

        val path = vectorSpecOutput.content.writeToKt(
            outputDir = targetDirectory.absolutePath,
            nameWithoutExtension = vectorSpecOutput.name,
        )

        val packInfo = nestedPackName?.let { " (nested pack: $it)" } ?: ""
        logger.info("Process ${file.name} into $path$packInfo")
    }

    private fun resolveTargetDirectory(
        outputDirectory: File,
        packageName: String,
        nestedPackName: String,
        useFlatPackage: Boolean,
    ): File {
        val fullPackage = resolveFullPackageName(packageName, nestedPackName, useFlatPackage)
        val packagePath = fullPackage.replace('.', File.separatorChar)
        return outputDirectory.resolve(packagePath)
    }

    private fun resolveFullPackageName(
        packageName: String,
        nestedPackName: String,
        useFlatPackage: Boolean,
    ): String = when {
        useFlatPackage -> packageName
        nestedPackName.isEmpty() -> packageName
        else -> "$packageName.${nestedPackName.lowercase()}"
    }

    private fun createFullQualifiedImports(fullQualifiedNames: List<String>): FullQualifiedImports {
        return FullQualifiedImports(
            brush = "Brush" in fullQualifiedNames,
            color = "Color" in fullQualifiedNames,
            offset = "Offset" in fullQualifiedNames,
        )
    }

    private fun iconWord(count: Int): String = if (count == 1) "icon" else "icons"

    private fun logFileParseError(file: File, error: Throwable) {
        logger.warn("Skipping file ${file.name} due to processing error, details: ${error.message}")
    }

    private fun resolveAutoMirror(nestedPackName: String?): Boolean? {
        // Priority: nested pack > icon pack > root extension
        if (iconPack.isPresent && nestedPackName != null) {
            val nestedPack = iconPack.get().nestedPacks.get().find { it.name.get() == nestedPackName }
            if (nestedPack?.autoMirror?.isPresent == true) {
                return nestedPack.autoMirror.get()
            }
        }

        if (iconPack.isPresent && iconPack.get().autoMirror.isPresent) {
            return iconPack.get().autoMirror.get()
        }

        if (autoMirror.isPresent) {
            return autoMirror.get()
        }

        return null
    }
}
