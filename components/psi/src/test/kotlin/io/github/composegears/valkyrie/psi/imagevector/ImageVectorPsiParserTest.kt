package io.github.composegears.valkyrie.psi.imagevector

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.extensions.castOrNull
import io.github.composegears.valkyrie.psi.util.project
import kotlin.io.path.name
import kotlin.io.path.readText
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class ImageVectorPsiParserTest {

    @Test
    fun `parse image vector`() {
        val path = getResourcePath("SeveralPath.kt")
        val ktFile = PsiManager.getInstance(project)
            .findFile(LightVirtualFile(path.name, KotlinFileType.INSTANCE, path.readText()))
            .castOrNull<KtFile>()

        assertThat(ktFile).isNotNull()
        val imageVector = ImageVectorPsiParser.parseToImageVector(ktFile!!)

        assertThat(imageVector).isEqualTo(
            ImageVector.Builder(
                name = "SeveralPath",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 18f,
                viewportHeight = 18f,
            ).build(),
        )
    }
}
