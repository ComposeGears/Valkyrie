package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.psi.TEST_DATA_PATH
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.common.ParseType
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedAddPathWithPathData
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedAllGroupParams
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedAllPathParams
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedClipPathGradient
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedComposeColor
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedEmptyPaths
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedIconWithGroup
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedLinearGradient
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedLinearGradientWithStroke
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedRadialGradient
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedSinglePath
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedWithoutPathImageVector
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinCodeInsightTest
import io.github.composegears.valkyrie.sdk.ir.compose.toComposeImageVector
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource

@ParameterizedClass
@EnumSource(value = ParseType::class)
class KtFileToImageVectorParserTest(private val parseType: ParseType) : KotlinCodeInsightTest() {

    @Test
    fun `without path image vector`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/WithoutPath.kt",
            pathToBacking = "imagevector/kt/backing/WithoutPath.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedWithoutPathImageVector)
    }

    @Test
    fun `empty paths`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/EmptyPaths.kt",
            pathToBacking = "imagevector/kt/backing/EmptyPaths.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedEmptyPaths)
    }

    @Test
    fun `parse all path params`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/AllPathParams.kt",
            pathToBacking = "imagevector/kt/backing/AllPathParams.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedAllPathParams)
    }

    @Test
    fun `parse icon with group`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "lazy/IconWithGroup.kt",
            pathToBacking = "backing/IconWithGroup.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedIconWithGroup)
    }

    @Test
    fun `parse single path property`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "lazy/SinglePath.kt",
            pathToBacking = "backing/SinglePath.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedSinglePath)
    }

    @Test
    fun `parse all group params`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/AllGroupParams.kt",
            pathToBacking = "imagevector/kt/backing/AllGroupParams.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedAllGroupParams)
    }

    @Test
    fun `parse compose color`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/ComposeColor.kt",
            pathToBacking = "imagevector/kt/backing/ComposeColor.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedComposeColor)
    }

    @Test
    fun `parse icon with linear gradient`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/LinearGradient.kt",
            pathToBacking = "imagevector/kt/backing/LinearGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedLinearGradient)
    }

    @Test
    fun `parse icon with linear gradient and stroke`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/LinearGradientWithStroke.kt",
            pathToBacking = "imagevector/kt/backing/LinearGradientWithStroke.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedLinearGradientWithStroke)
    }

    @Test
    fun `parse linear gradient with clip path`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/ClipPathGradient.kt",
            pathToBacking = "imagevector/kt/backing/ClipPathGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedClipPathGradient)
    }

    @Test
    fun `parse icon with radial gradient`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "imagevector/kt/lazy/RadialGradient.kt",
            pathToBacking = "imagevector/kt/backing/RadialGradient.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedRadialGradient)
    }

    @Test
    fun `parse addPath with pathData string`() = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            pathToLazy = "lazy/AddPathWithPathData.kt",
            pathToBacking = "backing/AddPathWithPathData.kt",
        )
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedAddPathWithPathData)
    }

    override fun getTestDataPath() = TEST_DATA_PATH

    fun ParseType.toKtFile(
        pathToLazy: String,
        pathToBacking: String,
    ): KtFile = when (this) {
        ParseType.Lazy -> loadKtFile(pathToLazy)
        ParseType.Backing -> loadKtFile(pathToBacking)
    }
}
