package io.github.composegears.valkyrie.gradle.internal.task

import io.github.composegears.valkyrie.gradle.IconPackExtension
import io.github.composegears.valkyrie.gradle.NestedPack
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import io.github.composegears.valkyrie.sdk.core.tree.MutableTreeNode
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.CodeStyleConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.FullyQualifiedImports
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.FullyQualifiedImports.Companion.reservedComposeTypeNames
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.ImageVectorGenerator
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

    @get:Input
    abstract val suppressUnusedReceiverWarning: Property<Boolean>

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

    private val codeStyleConfig: CodeStyleConfig
        get() = CodeStyleConfig(
            useExplicitMode = useExplicitMode.get(),
            indentSize = indentSize.get(),
        )

    private val imageVectorConfig: ImageVectorConfig
        get() = ImageVectorConfig(
            outputFormat = outputFormat.get(),
            useComposeColors = useComposeColors.get(),
            generatePreview = generatePreview.get(),
            useFlatPackage = iconPack.isPresent && iconPack.get().useFlatPackage.get(),
            addTrailingComma = addTrailingComma.get(),
            usePathDataString = usePathDataString.get(),
            suppressUnusedReceiverWarning = suppressUnusedReceiverWarning.get(),
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

        val fullyQualifiedNames = iconNames.filter { it in reservedComposeTypeNames }

        if (fullyQualifiedNames.isNotEmpty()) {
            logger.lifecycle(
                "Found icons names that conflict with reserved Compose qualifiers. " +
                    "Full qualified import will be used for: \"${fullyQualifiedNames.joinToString(", ")}\"",
            )
        }

        // Check for duplicates with nested pack awareness
        if (iconPack.isPresent) {
            val nestedPacks = if (iconPack.get().nestedPacks.get().isNotEmpty()) {
                NestedPackFlattener.flatten(iconPack.get().nestedPacks.get())
            } else {
                emptyList()
            }
            DuplicateIconValidator.validateDuplicates(
                files = iconFiles.files.toList(),
                iconNames = iconNames,
                packName = iconPack.get().name.get(),
                nestedPacks = nestedPacks,
                useFlatPackage = iconPack.get().useFlatPackage.get(),
            )
        } else {
            // No icon pack - validate as package without nesting
            DuplicateIconValidator.checkDuplicatesInIconNames(
                names = iconNames,
                context = "\"${packageName.get()}\"",
            )
        }

        if (iconPack.isPresent && iconPack.get().targetSourceSet.get() == sourceSet.get()) {
            generateIconPack(outputDirectory = outputDirectory)
        }

        if (iconPack.isPresent) {
            generateIconsWithIconPack(
                outputDirectory = outputDirectory,
                fullQualifiedNames = fullyQualifiedNames,
            )
        } else {
            generateIconsWithoutPack(
                outputDirectory = outputDirectory,
                fullQualifiedNames = fullyQualifiedNames,
            )
        }

        val executionTime = System.currentTimeMillis() - startTime
        logger.info("ImageVector generation completed in ${executionTime}ms")
    }

    private fun generateIconPack(outputDirectory: File) {
        val packageName = packageName.get()
        val iconPackExtension = iconPack.get()

        val pack = buildIconPackTree(iconPackExtension)

        IconPackGenerator.create(
            config = IconPackGeneratorConfig(
                packageName = packageName,
                iconPackTree = pack,
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
            logger.lifecycle("Generated \"${pack.data}\" iconpack in package \"$packageName\"")
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

        val config = ImageVectorGeneratorConfig.simple(
            iconName = "",
            packageName = packageName,
            codeStyle = codeStyleConfig,
            imageVector = imageVectorConfig,
            fullyQualifiedImports = FullyQualifiedImports.from(fullQualifiedNames),
        )
        var convertedCount = 0
        iconFiles.files.forEach { file ->
            runCatching {
                processIconFile(
                    file = file,
                    config = config,
                    targetDirectory = targetDirectory,
                    nestedPackName = null,
                    flattenedNestedPack = null,
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

        // Note: for ImageVectorConfig, we must pass a linear chain tree (single path)
        // Individual nested packs are handled separately in generateIconsForNestedPacks
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "",
            packageName = packageName,
            iconPackTree = buildTree(pack.name.get()),
            codeStyle = codeStyleConfig,
            imageVector = imageVectorConfig,
            fullyQualifiedImports = FullyQualifiedImports.from(fullQualifiedNames),
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
                config = config.copy(imageVector = config.imageVector.copy(useFlatPackage = useFlatPackage)),
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
                    flattenedNestedPack = null,
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
        val packName = config.iconPackTree?.data.orEmpty()

        // Flatten nested packs recursively to handle arbitrary depth
        val flattenedPacks = NestedPackFlattener.flatten(nestedPacks)
        val nestedPackIconCounts = mutableMapOf<String, Int>()

        // Find the common resources directory.
        // We need to go up the hierarchy based on the max nesting depth of nested packs.
        // For 1-level nested: depth = 1, go up 1 level to get from "filled/" to "valkyrieResources/"
        // For 2-level nested: depth = 2, go up 2 levels to get from "material/filled/" to "valkyrieResources/"
        val maxNestedPackDepth = flattenedPacks.maxOfOrNull { it.hierarchyPath.size } ?: 1
        val resourcesDir = iconFiles.files.findResourcesDirectory(maxNestedPackDepth)

        iconFiles.files.forEach { file ->
            // Calculate relative path from resources directory
            val relativeFileDir = if (resourcesDir != null && resourcesDir.isDirectory) {
                file.parentFile.toRelativeString(resourcesDir)
            } else {
                file.parentFile.name
            }

            // Find matching flattened nested pack based on source folder - exact match required
            val matchingNestedPack = flattenedPacks.find { nestedPack ->
                val normalizedSourceFolder = nestedPack.sourceFolder.replace('/', File.separatorChar)
                relativeFileDir == normalizedSourceFolder
            }

            if (matchingNestedPack != null) {
                val nestedPackName = matchingNestedPack.displayName
                val nestedPackConfig = config.copy(
                    iconPackTree = buildNestedTree(packName, nestedPackName),
                )
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
                        flattenedNestedPack = matchingNestedPack,
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
        flattenedNestedPack: FlattenedNestedPack? = null,
    ) {
        val parseOutput = SvgXmlParser.toIrImageVector(ParserType.Jvm, Path(file.absolutePath))

        // Apply autoMirror override if specified (nested pack > icon pack > root)
        val effectiveAutoMirror = resolveAutoMirror(flattenedNestedPack)
        val irImageVector = if (effectiveAutoMirror != null) {
            parseOutput.irImageVector.copy(autoMirror = effectiveAutoMirror)
        } else {
            parseOutput.irImageVector
        }

        val vectorSpecOutput = ImageVectorGenerator.convert(
            vector = irImageVector,
            config = config.copy(iconName = parseOutput.iconName),
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

    private fun iconWord(count: Int): String = if (count == 1) "icon" else "icons"

    private fun logFileParseError(file: File, error: Throwable) {
        logger.warn("Skipping file ${file.name} due to processing error, details: ${error.message}")
    }

    private fun resolveAutoMirror(flattenedNestedPack: FlattenedNestedPack?): Boolean? {
        // Priority: nested pack > icon pack > root extension
        if (flattenedNestedPack?.autoMirror != null) {
            return flattenedNestedPack.autoMirror
        }

        if (iconPack.isPresent && iconPack.get().autoMirror.isPresent) {
            return iconPack.get().autoMirror.get()
        }

        if (autoMirror.isPresent) {
            return autoMirror.get()
        }

        return null
    }

    private fun buildIconPackTree(iconPackExtension: IconPackExtension): IconPackTree {
        return buildTree(iconPackExtension.name.get()) {
            buildNestedPacksTree(this, iconPackExtension.nestedPacks.get())
        }
    }

    private fun buildNestedPacksTree(parent: MutableTreeNode<String>, nestedPacks: List<NestedPack>) {
        nestedPacks.forEach { pack ->
            parent.child(pack.name.get()) {
                val children = pack.nestedPacks.get()
                logger.info("Building tree for '${pack.name.get()}' with ${children.size} children")
                buildNestedPacksTree(this, children)
            }
        }
    }

    /**
     * Build a nested tree from root pack and display name path.
     * Example: buildNestedTree("ValkyrieIcons", "Material.Filled") produces:
     *   ValkyrieIcons
     *     └─ Material
     *         └─ Filled
     */
    private fun buildNestedTree(rootPackName: String, displayNamePath: String): IconPackTree {
        val parts = displayNamePath.split(".")
        if (parts.isEmpty()) {
            return buildTree(rootPackName)
        }

        return buildTree(rootPackName) {
            // Recursively build nested children
            fun addChildLevels(node: MutableTreeNode<String>, levelIndex: Int) {
                if (levelIndex >= parts.size) return
                node.child(parts[levelIndex]) {
                    addChildLevels(this, levelIndex + 1)
                }
            }
            addChildLevels(this, 0)
        }
    }
}
