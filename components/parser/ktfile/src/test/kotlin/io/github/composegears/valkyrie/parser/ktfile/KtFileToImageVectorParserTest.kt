package io.github.composegears.valkyrie.parser.ktfile

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.intellij.openapi.application.invokeAndWaitIfNeeded
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.ProjectExtension
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import io.github.composegears.valkyrie.extensions.cast
import io.github.composegears.valkyrie.parser.ktfile.util.toComposeImageVector
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@Suppress("UnstableApiUsage")
class KtFileToImageVectorParserTest {

    companion object {
        @RegisterExtension
        val projectExtension = ProjectExtension()
    }

    private val project: Project
        get() = projectExtension.project

    private fun createKtFile(from: String): KtFile {
        return PsiManager.getInstance(project)
            .findFile(LightVirtualFile("", KotlinFileType.INSTANCE, getResourceText(from)))
            .cast<KtFile>()
    }

    @Test
    fun `empty image vector`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "EmptyImageVector.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
            name = "EmptyImageVector",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @Test
    fun `empty paths`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "EmptyPaths.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
            name = "EmptyPaths",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).apply {
            path { }
            path { }
            path { }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @Test
    fun `parse all path params`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "AllPathParams.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
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
                moveToRelative(1f, -2f)
                lineTo(3.623f, 9f)
                lineToRelative(-5.49f, 1.3f)
                horizontalLineTo(1.4f)
                horizontalLineToRelative(-6f)
                verticalLineTo(95.06f)
                verticalLineToRelative(10.0f)
                curveTo(11.76f, 1.714f, 11.755f, 1.715f, 11.768f, 1.714f)
                curveToRelative(3.236f, 0.224f, 7.033f, 0f, 7.033f, 0f)
                reflectiveCurveTo(11.957f, 41.979f, 0.013f, 44.716f)
                reflectiveCurveToRelative(6.586f, 6.584f, 9.823f, 6.805f)
                quadTo(20.306f, 6.477f, 20.306f, 6.508f)
                quadToRelative(0.04f, -0.3f, 0.06f, -0.61f)
                reflectiveQuadTo(5f, 3f)
                reflectiveQuadToRelative(4f, 1f)
                arcTo(0.75f, 0.75f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 5.092f)
                arcTo(0.75f, 0.75f, 0f, true, false, 3f, 5.092f)
                arcToRelative(0.763f, 0.763f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.55f, -0.066f)
                arcToRelative(0.763f, 0.763f, 0f, false, true, -0.55f, -0.066f)
                close()
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @Test
    fun `parse material icon`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "MaterialIcon.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = materialIcon(name = "Filled.Settings", autoMirror = true) {
            materialPath(
                fillAlpha = 0.5f,
                strokeAlpha = 0.6f,
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(19.14f, 12.94f)
                close()
            }
        }

        assertThat(imageVector).isEqualTo(expected)
    }

    @Test
    fun `parse icon with group`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "IconWithGroup.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
            name = "IconWithGroup",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 512f,
            viewportHeight = 512f,
        ).apply {
            group {
                path(fill = SolidColor(Color(0xFFD80027))) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(512f)
                    verticalLineToRelative(167f)
                    lineToRelative(-23.2f, 89.7f)
                    lineTo(512f, 345f)
                    verticalLineToRelative(167f)
                    horizontalLineTo(0f)
                    verticalLineTo(345f)
                    lineToRelative(29.4f, -89f)
                    lineTo(0f, 167f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFEEEEEE))) {
                    moveTo(0f, 167f)
                    horizontalLineToRelative(512f)
                    verticalLineToRelative(178f)
                    horizontalLineTo(0f)
                    close()
                }
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @Test
    fun `parse icon with linear and radial gradient`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "IconWithGradient.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
            name = "IconWithGradient",
            defaultWidth = 51.dp,
            defaultHeight = 63.dp,
            viewportWidth = 51f,
            viewportHeight = 63f,
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
                stroke = SolidColor(Color(0x00000000)),
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(100f)
                verticalLineToRelative(20f)
                horizontalLineToRelative(-100f)
                close()
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.126f to Color(0xFFE7BD76),
                        0.13f to Color(0xFFE6BB74),
                        0.247f to Color(0xFFD48E4E),
                        0.334f to Color(0xFFCC753B),
                        0.38f to Color(0xFFC96C35),
                        0.891f to Color(0xFFC96D34),
                        0.908f to Color(0xFFCC7439),
                        0.937f to Color(0xFFD28647),
                        0.973f to Color(0xFFDEA763),
                        0.989f to Color(0xFFE5B972),
                    ),
                    start = Offset(46.778f, 40.493f),
                    end = Offset(24.105f, 63.166f),
                ),
            ) {
                moveTo(51f, 44.716f)
                reflectiveCurveToRelative(-6.586f, 6.584f, -9.823f, 6.805f)
                curveToRelative(-3.235f, 0.224f, -7.032f, 0f, -7.032f, 0f)
                reflectiveCurveToRelative(-7.024f, 1.732f, -7.024f, 7.368f)
                verticalLineTo(63f)
                lineToRelative(-3.195f, -0.014f)
                verticalLineToRelative(-5.571f)
                curveToRelative(0f, -6.857f, 10.052f, -7.567f, 10.052f, -7.567f)
                reflectiveCurveTo(39.057f, 41.979f, 51f, 44.716f)
            }
        }.build()

        // assertKt error due usage arrayList inside ImageVector:
        // "did not compare equal to the same type with the same string representation"
        assertThat(imageVector.toString()).isEqualTo(expected.toString())
    }

    @Test
    fun `parse lazy property`() = invokeAndWaitIfNeeded {
        val ktFile = createKtFile(from = "LazyProperty.kt")
        val imageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.toComposeImageVector()

        val expected = ImageVector.Builder(
            name = "LazyProperty",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(19f, 13f)
                lineTo(13f, 13f)
                lineTo(13f, 19f)
                lineTo(11f, 19f)
                lineTo(11f, 13f)
                lineTo(5f, 13f)
                lineTo(5f, 11f)
                lineTo(11f, 11f)
                lineTo(11f, 5f)
                lineTo(13f, 5f)
                lineTo(13f, 11f)
                lineTo(19f, 11f)
                lineTo(19f, 13f)
                close()
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }
}
