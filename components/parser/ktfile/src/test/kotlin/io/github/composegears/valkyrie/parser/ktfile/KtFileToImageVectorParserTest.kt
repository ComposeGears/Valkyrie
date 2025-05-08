package io.github.composegears.valkyrie.parser.ktfile

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.openapi.project.Project
import com.intellij.testFramework.ProjectExtension
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.parser.ktfile.common.ParseType
import io.github.composegears.valkyrie.parser.ktfile.common.createKtFile
import io.github.composegears.valkyrie.parser.ktfile.common.toKtFile
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedAllGroupParams
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedAllPathParams
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedClipPathGradient
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedComposeColor
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedEmptyImageVector
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedEmptyPaths
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedIconWithGroup
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedLinearGradient
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedLinearGradientWithStroke
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedMaterialIcon
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedRadialGradient
import io.github.composegears.valkyrie.parser.ktfile.expected.ExpectedSinglePath
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class KtFileToImageVectorParserTest {

    companion object {
        @JvmField
        @RegisterExtension
        val projectExtension = ProjectExtension()
    }

    private val project: Project
        get() = projectExtension.project

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `empty image vector`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/EmptyImageVector.kt",
            pathToBacking = "backing/EmptyImageVector.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedEmptyImageVector)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `empty paths`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/EmptyPaths.kt",
            pathToBacking = "backing/EmptyPaths.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedEmptyPaths)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all path params`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/AllPathParams.kt",
            pathToBacking = "backing/AllPathParams.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedAllPathParams)
    }

    @Test
    fun `parse material icon`() = runInEdtAndGet {
        val ktFile = project.createKtFile(from = "backing/MaterialIcon.kt")
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedMaterialIcon)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with group`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/IconWithGroup.kt",
            pathToBacking = "backing/IconWithGroup.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedIconWithGroup)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse single path property`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/SinglePath.kt",
            pathToBacking = "backing/SinglePath.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedSinglePath)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all group params`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/AllGroupParams.kt",
            pathToBacking = "imagevector/kt/backing/AllGroupParams.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedAllGroupParams)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse compose color`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/ComposeColor.kt",
            pathToBacking = "imagevector/kt/backing/ComposeColor.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedComposeColor)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradient.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedLinearGradient)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient and stroke`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradientWithStroke.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradientWithStroke.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedLinearGradientWithStroke)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse linear gradient with clip path`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/ClipPathGradient.kt",
            pathToBacking = "imagevector/kt/backing/ClipPathGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedClipPathGradient)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with radial gradient`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/RadialGradient.kt",
            pathToBacking = "imagevector/kt/lazy/RadialGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        assertThat(imageVector).isEqualTo(ExpectedRadialGradient)
    }
}
