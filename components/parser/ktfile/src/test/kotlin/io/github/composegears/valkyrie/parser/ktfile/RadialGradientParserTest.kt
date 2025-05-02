package io.github.composegears.valkyrie.parser.ktfile

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.parser.ktfile.common.BaseKtParserTest
import io.github.composegears.valkyrie.parser.ktfile.common.ParseType
import io.github.composegears.valkyrie.parser.ktfile.common.toKtFile
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class RadialGradientParserTest : BaseKtParserTest() {

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with radial gradient`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/RadialGradient.kt",
            pathToBacking = "imagevector/kt/lazy/RadialGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "RadialGradient",
            defaultWidth = 100.dp,
            defaultHeight = 20.dp,
            viewportWidth = 100f,
            viewportHeight = 20f,
        ).apply {
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.19f to Color(0xFFD53A42),
                        0.39f to Color(0xFFDF7A40),
                        0.59f to Color(0xFFF0A941),
                        1f to Color(0xFFFFFFF0),
                    ),
                    center = Offset(0f, 10f),
                    radius = 100f,
                ),
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(100f)
                verticalLineToRelative(20f)
                horizontalLineToRelative(-100f)
                close()
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }
}
