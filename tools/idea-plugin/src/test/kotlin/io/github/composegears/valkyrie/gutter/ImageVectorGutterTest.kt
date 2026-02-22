package io.github.composegears.valkyrie.gutter

import assertk.assertThat
import assertk.assertions.hasSize
import com.intellij.codeInsight.daemon.LineMarkerInfo.LineMarkerGutterIconRenderer
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.completion.ImageVectorIcon
import io.github.composegears.valkyrie.sdk.core.extensions.cast
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinCodeInsightTest
import kotlin.test.assertNotEquals
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
            """.trimIndent(),
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
            
            """.trimIndent(),
        )

        val gutters = myFixture.findAllGutters()
        assertThat(gutters).hasSize(2)

        val gutterMark1 = gutters[0].cast<LineMarkerGutterIconRenderer<*>>()
        assertEquals("WithoutPath", gutterMark1.tooltipText)
        assertNotNull(gutterMark1.icon)

        val gutterMark2 = gutters[1].cast<LineMarkerGutterIconRenderer<*>>()
        assertEquals("MyPath", gutterMark2.tooltipText)
        assertNotNull(gutterMark2.icon)

        assertNotEquals(
            gutterMark1.lineMarkerInfo.icon.cast<ImageVectorIcon>().toString(),
            gutterMark2.lineMarkerInfo.icon.cast<ImageVectorIcon>().toString(),
        )
    }

    @Test
    fun `multiple equal name gutter icons in file`() = runInEdtAndGet {
        myFixture.configureByText(
            "Stylus.kt",
            """
            import androidx.compose.material.icons.Icons
            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val Icons.Outlined.Stylus: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
                ImageVector.Builder(
                    name = "StylusFountainPen",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(18.727f, 8.55f)
                        curveToRelative(-0.044f, -0.177f, -0.137f, -0.336f, -0.279f, -0.478f)
                        lineToRelative(-5.732f, -5.281f)
                        curveToRelative(-0.195f, -0.195f, -0.433f, -0.292f, -0.716f, -0.292f)
                        reflectiveCurveToRelative(-0.522f, 0.097f, -0.716f, 0.292f)
                        lineToRelative(-5.732f, 5.281f)
                        curveToRelative(-0.141f, 0.142f, -0.234f, 0.301f, -0.279f, 0.478f)
                        curveToRelative(-0.044f, 0.177f, -0.049f, 0.363f, -0.013f, 0.557f)
                        lineToRelative(1.937f, 8.12f)
                        curveToRelative(0.053f, 0.248f, 0.177f, 0.447f, 0.372f, 0.597f)
                        reflectiveCurveToRelative(0.416f, 0.226f, 0.663f, 0.226f)
                        lineToRelative(7.536f, -0f)
                        curveToRelative(0.248f, 0f, 0.469f, -0.075f, 0.663f, -0.226f)
                        reflectiveCurveToRelative(0.318f, -0.349f, 0.372f, -0.597f)
                        lineToRelative(1.937f, -8.12f)
                        curveToRelative(0.035f, -0.195f, 0.031f, -0.38f, -0.013f, -0.557f)
                        close()
                        moveTo(14.919f, 15.927f)
                        lineToRelative(-5.838f, 0f)
                        lineToRelative(-1.619f, -6.714f)
                        lineToRelative(3.476f, -3.211f)
                        verticalLineToRelative(2.813f)
                        curveToRelative(-0.248f, 0.177f, -0.442f, 0.398f, -0.584f, 0.663f)
                        curveToRelative(-0.142f, 0.265f, -0.212f, 0.557f, -0.212f, 0.876f)
                        curveToRelative(0f, 0.513f, 0.181f, 0.951f, 0.544f, 1.314f)
                        curveToRelative(0.363f, 0.363f, 0.8f, 0.544f, 1.314f, 0.544f)
                        reflectiveCurveToRelative(0.951f, -0.181f, 1.314f, -0.544f)
                        curveToRelative(0.363f, -0.363f, 0.544f, -0.8f, 0.544f, -1.314f)
                        curveToRelative(0f, -0.318f, -0.071f, -0.61f, -0.212f, -0.876f)
                        curveToRelative(-0.141f, -0.265f, -0.336f, -0.486f, -0.584f, -0.663f)
                        lineToRelative(-0f, -2.813f)
                        lineToRelative(3.476f, 3.211f)
                        lineToRelative(-1.619f, 6.714f)
                        close()
                    }
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(8.22f, 18.846f)
                        lineTo(15.78f, 18.846f)
                        arcTo(
                            0.997f,
                            0.997f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            16.777f,
                            19.843f
                        )
                        lineTo(16.777f, 20.503f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.78f, 21.5f)
                        lineTo(8.22f, 21.5f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.223f, 20.503f)
                        lineTo(7.223f, 19.843f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.22f, 18.846f)
                        close()
                    }
                }.build()
            }

            val Icons.TwoTone.Stylus: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
                ImageVector.Builder(
                    name = "TwoToneStylusFountainPen",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(18.727f, 8.55f)
                        curveToRelative(-0.044f, -0.177f, -0.137f, -0.336f, -0.279f, -0.478f)
                        lineToRelative(-5.732f, -5.281f)
                        curveToRelative(-0.195f, -0.195f, -0.433f, -0.292f, -0.716f, -0.292f)
                        reflectiveCurveToRelative(-0.522f, 0.097f, -0.716f, 0.292f)
                        lineToRelative(-5.732f, 5.281f)
                        curveToRelative(-0.141f, 0.142f, -0.234f, 0.301f, -0.279f, 0.478f)
                        curveToRelative(-0.044f, 0.177f, -0.049f, 0.363f, -0.013f, 0.557f)
                        lineToRelative(1.937f, 8.12f)
                        curveToRelative(0.053f, 0.248f, 0.177f, 0.447f, 0.372f, 0.597f)
                        reflectiveCurveToRelative(0.416f, 0.226f, 0.663f, 0.226f)
                        lineToRelative(7.536f, -0f)
                        curveToRelative(0.248f, 0f, 0.469f, -0.075f, 0.663f, -0.226f)
                        reflectiveCurveToRelative(0.318f, -0.349f, 0.372f, -0.597f)
                        lineToRelative(1.937f, -8.12f)
                        curveToRelative(0.035f, -0.195f, 0.031f, -0.38f, -0.013f, -0.557f)
                        close()
                        moveTo(14.919f, 15.927f)
                        lineToRelative(-5.838f, 0f)
                        lineToRelative(-1.619f, -6.714f)
                        lineToRelative(3.476f, -3.211f)
                        verticalLineToRelative(2.813f)
                        curveToRelative(-0.248f, 0.177f, -0.442f, 0.398f, -0.584f, 0.663f)
                        curveToRelative(-0.142f, 0.265f, -0.212f, 0.557f, -0.212f, 0.876f)
                        curveToRelative(0f, 0.513f, 0.181f, 0.951f, 0.544f, 1.314f)
                        curveToRelative(0.363f, 0.363f, 0.8f, 0.544f, 1.314f, 0.544f)
                        reflectiveCurveToRelative(0.951f, -0.181f, 1.314f, -0.544f)
                        curveToRelative(0.363f, -0.363f, 0.544f, -0.8f, 0.544f, -1.314f)
                        curveToRelative(0f, -0.318f, -0.071f, -0.61f, -0.212f, -0.876f)
                        curveToRelative(-0.141f, -0.265f, -0.336f, -0.486f, -0.584f, -0.663f)
                        lineToRelative(-0f, -2.813f)
                        lineToRelative(3.476f, 3.211f)
                        lineToRelative(-1.619f, 6.714f)
                        close()
                    }
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(8.22f, 18.846f)
                        lineTo(15.78f, 18.846f)
                        arcTo(
                            0.997f,
                            0.997f,
                            0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            16.777f,
                            19.843f
                        )
                        lineTo(16.777f, 20.503f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15.78f, 21.5f)
                        lineTo(8.22f, 21.5f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7.223f, 20.503f)
                        lineTo(7.223f, 19.843f)
                        arcTo(0.997f, 0.997f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8.22f, 18.846f)
                        close()
                    }
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 0.3f,
                        strokeAlpha = 0.3f
                    ) {
                        moveTo(14.919f, 15.927f)
                        lineToRelative(-5.838f, 0f)
                        lineToRelative(-1.619f, -6.714f)
                        lineToRelative(3.476f, -3.211f)
                        lineToRelative(0f, 2.813f)
                        curveToRelative(-0.248f, 0.177f, -0.442f, 0.398f, -0.584f, 0.663f)
                        curveToRelative(-0.142f, 0.265f, -0.212f, 0.557f, -0.212f, 0.876f)
                        curveToRelative(0f, 0.513f, 0.181f, 0.951f, 0.544f, 1.314f)
                        reflectiveCurveToRelative(0.8f, 0.544f, 1.314f, 0.544f)
                        reflectiveCurveToRelative(0.951f, -0.181f, 1.314f, -0.544f)
                        reflectiveCurveToRelative(0.544f, -0.8f, 0.544f, -1.314f)
                        curveToRelative(0f, -0.318f, -0.071f, -0.61f, -0.212f, -0.876f)
                        curveToRelative(-0.141f, -0.265f, -0.336f, -0.486f, -0.584f, -0.663f)
                        lineToRelative(-0f, -2.813f)
                        lineToRelative(3.476f, 3.211f)
                        lineToRelative(-1.619f, 6.714f)
                        close()
                    }
                }.build()
            }      
            """.trimIndent(),
        )

        val gutters = myFixture.findAllGutters()
        assertThat(gutters).hasSize(2)

        val gutterMark1 = gutters[0].cast<LineMarkerGutterIconRenderer<*>>()
        assertEquals("Stylus", gutterMark1.tooltipText)
        assertNotNull(gutterMark1.icon)

        val gutterMark2 = gutters[1].cast<LineMarkerGutterIconRenderer<*>>()
        assertEquals("Stylus", gutterMark2.tooltipText)
        assertNotNull(gutterMark2.icon)

        assertNotEquals(
            gutterMark1.lineMarkerInfo.icon.cast<ImageVectorIcon>().toString(),
            gutterMark2.lineMarkerInfo.icon.cast<ImageVectorIcon>().toString(),
        )
    }
}
