package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.psi.TEST_DATA_PATH
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedIconWithImportMember
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedMaterialIcon
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedMaterialIconOnlyWithPath
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedMaterialIconWithPath
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedMaterialIconWithoutParam
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected.ExpectedMaterialIconWithoutParam2
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinCodeInsightTest
import io.github.composegears.valkyrie.sdk.ir.compose.toComposeImageVector
import org.junit.jupiter.api.Test

class MaterialIconParserTest : KotlinCodeInsightTest() {

    @Test
    fun `parse material icon`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/MaterialIcon.all.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIcon)
    }

    @Test
    fun `parse material icon without param`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/MaterialIcon.all.without.param.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIconWithoutParam)
    }

    @Test
    fun `parse material icon with several materialPath`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/MaterialIcon.several.materialpath.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIconWithoutParam2)
    }

    @Test
    fun `parse material icon only with materialPath`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/MaterialIcon.material.path.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIconOnlyWithPath)
    }

    @Test
    fun `parse icon with import member`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/IconWithImportMember.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedIconWithImportMember)
    }

    @Test
    fun `parse material icon with path`() = runInEdtAndGet {
        val ktFile = loadKtFile("backing/MaterialIcon.path.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        assertThat(imageVector).isEqualTo(ExpectedMaterialIconWithPath)
    }

    override fun getTestDataPath() = TEST_DATA_PATH
}
