package io.github.composegears.valkyrie.psi.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.testFramework.ProjectExtension
import io.github.composegears.valkyrie.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.psi.imagevector.common.ParseType
import io.github.composegears.valkyrie.psi.imagevector.common.createKtFile
import io.github.composegears.valkyrie.psi.imagevector.common.toKtFile
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedAllGroupParams
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedAllPathParams
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedClipPathGradient
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedComposeColor
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedEmptyImageVector
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedEmptyPaths
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedIconWithGroup
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedLinearGradient
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedLinearGradientWithStroke
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedMaterialIcon
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedRadialGradient
import io.github.composegears.valkyrie.psi.imagevector.expected.ExpectedSinglePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    private fun check(suspend: () -> Unit) = runBlocking {
        withContext(Dispatchers.EDT) {
            suspend()
        }
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `empty image vector`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/EmptyImageVector.kt",
            pathToBacking = "backing/EmptyImageVector.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedEmptyImageVector)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `empty paths`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/EmptyPaths.kt",
            pathToBacking = "backing/EmptyPaths.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedEmptyPaths)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all path params`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/AllPathParams.kt",
            pathToBacking = "backing/AllPathParams.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedAllPathParams)
    }

    @org.junit.jupiter.api.Test
    fun `parse material icon`() = check {
        val ktFile = project.createKtFile(from = "backing/MaterialIcon.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIcon)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with group`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/IconWithGroup.kt",
            pathToBacking = "backing/IconWithGroup.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedIconWithGroup)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse single path property`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/SinglePath.kt",
            pathToBacking = "backing/SinglePath.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedSinglePath)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all group params`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/AllGroupParams.kt",
            pathToBacking = "imagevector/kt/backing/AllGroupParams.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedAllGroupParams)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse compose color`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/ComposeColor.kt",
            pathToBacking = "imagevector/kt/backing/ComposeColor.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedComposeColor)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradient.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedLinearGradient)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient and stroke`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradientWithStroke.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradientWithStroke.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedLinearGradientWithStroke)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse linear gradient with clip path`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/ClipPathGradient.kt",
            pathToBacking = "imagevector/kt/backing/ClipPathGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedClipPathGradient)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with radial gradient`(parseType: ParseType) = check {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/RadialGradient.kt",
            pathToBacking = "imagevector/kt/lazy/RadialGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedRadialGradient)
    }
}
