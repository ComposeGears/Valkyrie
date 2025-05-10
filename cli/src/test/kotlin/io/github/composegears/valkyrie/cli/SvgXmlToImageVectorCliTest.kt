package io.github.composegears.valkyrie.cli

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.cli.SvgXmlCommand.AddTrailingComma
import io.github.composegears.valkyrie.cli.SvgXmlCommand.GeneratePreview
import io.github.composegears.valkyrie.cli.SvgXmlCommand.IconPackName
import io.github.composegears.valkyrie.cli.SvgXmlCommand.ImageVectorOutputFormat
import io.github.composegears.valkyrie.cli.SvgXmlCommand.ImageVectorPreviewAnnotationType
import io.github.composegears.valkyrie.cli.SvgXmlCommand.IndentSize
import io.github.composegears.valkyrie.cli.SvgXmlCommand.InputPath
import io.github.composegears.valkyrie.cli.SvgXmlCommand.NestedPackName
import io.github.composegears.valkyrie.cli.SvgXmlCommand.OutputPath
import io.github.composegears.valkyrie.cli.SvgXmlCommand.PackageName
import io.github.composegears.valkyrie.cli.SvgXmlCommand.UseComposeColors
import io.github.composegears.valkyrie.cli.SvgXmlCommand.UseExplicitMode
import io.github.composegears.valkyrie.cli.SvgXmlCommand.UseFlatPackage
import io.github.composegears.valkyrie.cli.common.CliTestType
import io.github.composegears.valkyrie.cli.common.CliTestType.DirectMain
import io.github.composegears.valkyrie.cli.common.CliTestType.JarTerminal
import io.github.composegears.valkyrie.cli.common.CommandLineTestRunner
import io.github.composegears.valkyrie.cli.common.toResourceText
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.AndroidX
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.Jetbrains
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readText
import kotlin.properties.Delegates
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class SvgXmlToImageVectorCliTest {

    private var tempDir: Path by Delegates.notNull()

    @BeforeEach
    fun before() {
        tempDir = createTempDirectory()
    }

    @AfterEach
    fun after() {
        tempDir.toFile().delete()
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `batch processing xml`(arg: Pair<CliTestType, OutputFormat>) {
        val (cliTestType, outputFormat) = arg
        val input = getResourcePath("imagevector/xml")

        convert(
            cliTestType = cliTestType,
            svgXmlCommands = listOfNotNull(
                InputPath(input.absolutePathString()),
                OutputPath(tempDir.absolutePathString()),
                PackageName("io.github.composegears.valkyrie.icons"),
                ImageVectorOutputFormat(outputFormat),
            ),
        )

        val resultFiles = tempDir.toFile().listFiles().orEmpty().size
        val inputFiles = input.toFile().listFiles().orEmpty().size
        assertThat(resultFiles).isEqualTo(inputFiles)
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `batch processing svg`(arg: Pair<CliTestType, OutputFormat>) {
        val (cliTestType, outputFormat) = arg
        val input = getResourcePath("imagevector/svg")

        convert(
            cliTestType = cliTestType,
            svgXmlCommands = listOfNotNull(
                InputPath(input.absolutePathString()),
                OutputPath(tempDir.absolutePathString()),
                PackageName("io.github.composegears.valkyrie.icons"),
                ImageVectorOutputFormat(outputFormat),
            ),
        )

        val files = tempDir.toFile().listFiles().orEmpty()
        assertThat(files.size).isEqualTo(4)
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `flat package without icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_flat_package.xml",
            expectedKtName = "FlatPackage.kt",
            useFlatPackage = UseFlatPackage(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `flat package with icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_flat_package.xml",
            expectedKtName = "FlatPackage.pack.kt",
            actualKtName = "FlatPackage.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            useFlatPackage = UseFlatPackage(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `flat package with nested icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_flat_package.xml",
            expectedKtName = "FlatPackage.pack.nested.kt",
            actualKtName = "FlatPackage.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            nestedPackName = NestedPackName("Filled"),
            useFlatPackage = UseFlatPackage(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `generation with explicit mode`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.explicit.kt",
            actualKtName = "WithoutPath.kt",
            useExplicitMode = UseExplicitMode(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `generation without icon pack with indent 1`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.indent1.kt",
            actualKtName = "WithoutPath.kt",
            indentSize = IndentSize(1),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `generation without icon pack with indent 2`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.indent2.kt",
            actualKtName = "WithoutPath.kt",
            indentSize = IndentSize(2),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `generation without icon pack with indent 3`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.indent3.kt",
            actualKtName = "WithoutPath.kt",
            indentSize = IndentSize(3),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `generation without icon pack with indent 6`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.indent6.kt",
            actualKtName = "WithoutPath.kt",
            indentSize = IndentSize(6),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `androidx preview generation without icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.preview.androidx.kt",
            actualKtName = "WithoutPath.kt",
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(AndroidX),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `jetbrains preview generation without icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.preview.jetbrains.kt",
            actualKtName = "WithoutPath.kt",
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(Jetbrains),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `androidx preview generation with icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.pack.preview.androidx.kt",
            actualKtName = "WithoutPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(AndroidX),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `jetbrains preview generation with icon pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.pack.preview.jetbrains.kt",
            actualKtName = "WithoutPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(Jetbrains),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `androidx preview generation with nested pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.pack.nested.preview.androidx.kt",
            actualKtName = "filled/WithoutPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            nestedPackName = NestedPackName("Filled"),
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(AndroidX),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `jetbrains preview generation with nested pack`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.pack.nested.preview.jetbrains.kt",
            actualKtName = "filled/WithoutPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            nestedPackName = NestedPackName("Filled"),
            generatePreview = GeneratePreview(true),
            previewAnnotationType = ImageVectorPreviewAnnotationType(Jetbrains),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `empty path xml`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_without_path.xml",
            expectedKtName = "WithoutPath.pack.kt",
            actualKtName = "WithoutPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon only with path`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_only_path.xml",
            expectedKtName = "OnlyPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with path and solid color`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_fill_color_stroke.xml",
            expectedKtName = "FillColorStroke.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with path and solid color trailing`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_fill_color_stroke.xml",
            expectedKtName = "FillColorStroke.trailing.kt",
            actualKtName = "FillColorStroke.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            addTrailingComma = AddTrailingComma(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with all path params`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_all_path_params.xml",
            expectedKtName = "AllPathParams.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with all group params`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_all_group_params.xml",
            expectedKtName = "AllGroupParams.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            useComposeColors = UseComposeColors(false),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with several path`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_several_path.xml",
            expectedKtName = "SeveralPath.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            useComposeColors = UseComposeColors(false),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with compose colors enabled`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_compose_color.xml",
            expectedKtName = "ComposeColor.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
            useComposeColors = UseComposeColors(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with compose colors and linear gradient`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_compose_color_linear_gradient.xml",
            expectedKtName = "ComposeColor.linear.gradient.kt",
            actualKtName = "ComposeColorLinearGradient.kt",
            useComposeColors = UseComposeColors(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with compose colors and radial gradient`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_compose_color_radial_gradient.xml",
            expectedKtName = "ComposeColor.radial.gradient.kt",
            actualKtName = "ComposeColorRadialGradient.kt",
            useComposeColors = UseComposeColors(true),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with transparent fill color`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/ic_transparent_fill_color.xml",
            expectedKtName = "TransparentFillColor.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with named arguments`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/icon_with_named_args.xml",
            expectedKtName = "IconWithNamedArgs.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `icon with shorthand color`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/xml/icon_with_shorthand_color.xml",
            expectedKtName = "IconWithShorthandColor.kt",
            iconPackName = IconPackName("ValkyrieIcons"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `svg linear gradient parsing`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/svg/ic_linear_gradient.svg",
            expectedKtName = "LinearGradient.kt",
            useComposeColors = UseComposeColors(false),
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `svg radial gradient parsing`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/svg/ic_radial_gradient.svg",
            expectedKtName = "RadialGradient.kt",
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `svg linear gradient with stroke parsing`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/svg/ic_linear_gradient_with_stroke.svg",
            expectedKtName = "LinearGradientWithStroke.kt",
        )
    }

    @ParameterizedTest
    @MethodSource("testMatrix")
    fun `svg with complex gradient`(arg: Pair<CliTestType, OutputFormat>) {
        arg.testConversion(
            inputResource = "imagevector/svg/ic_clip_path_gradient.svg",
            expectedKtName = "ClipPathGradient.kt",
        )
    }

    private fun Pair<CliTestType, OutputFormat>.testConversion(
        inputResource: String,
        expectedKtName: String,
        actualKtName: String = expectedKtName,
        iconPackName: IconPackName? = null,
        useExplicitMode: UseExplicitMode? = null,
        useComposeColors: UseComposeColors? = null,
        nestedPackName: NestedPackName? = null,
        useFlatPackage: UseFlatPackage? = null,
        indentSize: IndentSize? = null,
        generatePreview: GeneratePreview? = null,
        previewAnnotationType: ImageVectorPreviewAnnotationType? = null,
        addTrailingComma: AddTrailingComma? = null,
    ) {
        val (cliTestType, outputFormat) = this
        val input = getResourcePath(inputResource)

        convert(
            cliTestType = cliTestType,
            svgXmlCommands = listOfNotNull(
                InputPath(input.absolutePathString()),
                OutputPath(tempDir.absolutePathString()),
                PackageName("io.github.composegears.valkyrie.icons"),
                iconPackName,
                nestedPackName,
                ImageVectorOutputFormat(outputFormat),
                generatePreview,
                previewAnnotationType,
                useFlatPackage,
                useExplicitMode,
                addTrailingComma,
                indentSize,
                useComposeColors,
            ),
        )

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/$expectedKtName",
            pathToLazyProperty = "imagevector/kt/lazy/$expectedKtName",
        )

        val files = tempDir.toFile().listFiles().orEmpty()
        assertThat(files.size).isEqualTo(1)

        val result = tempDir.resolve(actualKtName).readText()
        assertThat(result).isEqualTo(expected)
    }

    private fun convert(
        cliTestType: CliTestType,
        svgXmlCommands: List<SvgXmlCommand>,
    ) {
        val commands = listOf("svgxml2imagevector") + svgXmlCommands.map(SvgXmlCommand::command)

        when (cliTestType) {
            DirectMain -> main(*commands.toTypedArray())
            JarTerminal -> CommandLineTestRunner(commands).run()
        }
    }

    companion object {
        @JvmStatic
        fun testMatrix() = listOf(
            DirectMain to OutputFormat.BackingProperty,
            DirectMain to OutputFormat.LazyProperty,
            JarTerminal to OutputFormat.BackingProperty,
            JarTerminal to OutputFormat.LazyProperty,
        )
    }
}

private sealed interface SvgXmlCommand {
    val command: String

    data class InputPath(val path: String) : SvgXmlCommand {
        override val command: String = "--input-path=$path"
    }

    data class OutputPath(val path: String) : SvgXmlCommand {
        override val command: String = "--output-path=$path"
    }

    data class PackageName(val name: String) : SvgXmlCommand {
        override val command: String = "--package-name=$name"
    }

    data class IconPackName(val name: String) : SvgXmlCommand {
        override val command: String = "--iconpack-name=$name"
    }

    data class NestedPackName(val name: String) : SvgXmlCommand {
        override val command: String = "--nested-pack-name=$name"
    }

    data class ImageVectorOutputFormat(val format: OutputFormat) : SvgXmlCommand {
        override val command: String
            get() {
                val format = when (format) {
                    OutputFormat.BackingProperty -> "backing-property"
                    OutputFormat.LazyProperty -> "lazy-property"
                }
                return "--output-format=$format"
            }
    }

    data class GeneratePreview(val value: Boolean) : SvgXmlCommand {
        override val command: String = "--generate-preview=$value"
    }

    data class ImageVectorPreviewAnnotationType(val previewAnnotationType: PreviewAnnotationType) : SvgXmlCommand {
        override val command: String
            get() {
                val type = when (previewAnnotationType) {
                    AndroidX -> "androidx"
                    Jetbrains -> "jetbrains"
                }
                return "--preview-annotation-type=$type"
            }
    }

    data class UseFlatPackage(val value: Boolean) : SvgXmlCommand {
        override val command: String = "--flatpackage=$value"
    }

    data class UseExplicitMode(val value: Boolean) : SvgXmlCommand {
        override val command: String = "--explicit-mode=$value"
    }

    data class UseComposeColors(val value: Boolean) : SvgXmlCommand {
        override val command: String = "--use-compose-colors=$value"
    }

    data class AddTrailingComma(val value: Boolean) : SvgXmlCommand {
        override val command: String = "--trailing-comma=$value"
    }

    data class IndentSize(val size: Int) : SvgXmlCommand {
        override val command: String = "--indent-size=$size"
    }
}
