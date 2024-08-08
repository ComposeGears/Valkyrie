package io.github.composegears.valkyrie.psi.imagevector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
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
                name = "AllPathParams",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 18f,
                viewportHeight = 18f,
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF232F34)),
                    fillAlpha = 0.5f,
                    stroke = SolidColor(Color(0xFF232F34)),
                    strokeAlpha = 0.5f,
                    strokeLineWidth = 1f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 3f,
                    pathFillType = PathFillType.EvenOdd,
                ) {
                    moveTo(6.75f, 12.127f)
                    lineTo(3.623f, 9f)
                    lineTo(2.558f, 10.057f)
                    lineTo(6.75f, 14.25f)
                    lineTo(15.75f, 5.25f)
                    lineTo(14.693f, 4.192f)
                    lineTo(6.75f, 12.127f)
                    close()
                }
            }.build(),
        )
    }
}
