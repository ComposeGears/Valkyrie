package io.github.composegears.valkyrie.gutter

import assertk.assertThat
import assertk.assertions.hasSize
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinCodeInsightTest
import org.junit.jupiter.api.Test

class ImageVectorGutterTest : KotlinCodeInsightTest() {

    @Test
    fun `single gutter icon in file`() = runInEdtAndGet {
        myFixture.configureByText(
            "WithoutPath.kt",
            """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.unit.dp

            val WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null
            """.trimIndent()
        )

        val gutters = myFixture.findAllGutters()
        assertThat(gutters).hasSize(1)

        val gutter = gutters.first()
        assertEquals("WithoutPath", gutter.tooltipText)
        assertNotNull(gutter.icon)
    }

    @Test
    fun `multiple gutter icons in file`() = runInEdtAndGet {
        myFixture.configureByText(
            "WithoutPathIcons.kt",
            """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.unit.dp

            val WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null
            
            val MyPath: ImageVector
                get() {
                    if (_MyPath != null) {
                        return _MyPath!!
                    }
                    _MyPath = ImageVector.Builder(
                        name = "MyPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _MyPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _MyPath: ImageVector? = null
            
            """.trimIndent()
        )

        val gutters = myFixture.findAllGutters()
        assertThat(gutters).hasSize(2)

        gutters[0].run {
            assertEquals("WithoutPath", tooltipText)
            assertNotNull(icon)
        }
        gutters[1].run {
            assertEquals("MyPath", tooltipText)
            assertNotNull(icon)
        }
    }
}
