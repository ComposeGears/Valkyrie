package io.github.composegears.valkyrie.parser.ktfile

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
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
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.parser.ktfile.common.BaseKtParserTest
import io.github.composegears.valkyrie.parser.ktfile.common.ParseType
import io.github.composegears.valkyrie.parser.ktfile.common.createKtFile
import io.github.composegears.valkyrie.parser.ktfile.common.toKtFile
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class KtFileToImageVectorParserTest : BaseKtParserTest() {

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `empty image vector`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/EmptyImageVector.kt",
            pathToBacking = "backing/EmptyImageVector.kt",
        )

        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "EmptyImageVector",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).build()

        assertThat(imageVector).isEqualTo(expected)
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

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all path params`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/AllPathParams.kt",
            pathToBacking = "backing/AllPathParams.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "AllPathParams",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).apply {
            path(
                name = "path_name",
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
    fun `parse material icon`() = runInEdtAndGet {
        val ktFile = project.createKtFile(from = "backing/MaterialIcon.kt")
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

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

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with group`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/IconWithGroup.kt",
            pathToBacking = "backing/IconWithGroup.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

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

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse single path property`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "lazy/SinglePath.kt",
            pathToBacking = "backing/SinglePath.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "SinglePath",
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

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse all group params`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/AllGroupParams.kt",
            pathToBacking = "imagevector/kt/backing/AllGroupParams.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "AllGroupParams",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            group(
                name = "group",
                rotate = 15f,
                pivotX = 10f,
                pivotY = 10f,
                scaleX = 0.8f,
                scaleY = 0.8f,
                translationX = 6f,
                translationY = 1f,
            ) {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    fillAlpha = 0.3f,
                ) {
                    moveTo(15.67f, 4f)
                    horizontalLineTo(14f)
                    verticalLineTo(2f)
                    horizontalLineToRelative(-4f)
                    verticalLineToRelative(2f)
                    horizontalLineTo(8.33f)
                    curveTo(7.6f, 4f, 7f, 4.6f, 7f, 5.33f)
                    verticalLineTo(9f)
                    horizontalLineToRelative(4.93f)
                    lineTo(13f, 7f)
                    verticalLineToRelative(2f)
                    horizontalLineToRelative(4f)
                    verticalLineTo(5.33f)
                    curveTo(17f, 4.6f, 16.4f, 4f, 15.67f, 4f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF000000))) {
                    moveTo(13f, 12.5f)
                    horizontalLineToRelative(2f)
                    lineTo(11f, 20f)
                    verticalLineToRelative(-5.5f)
                    horizontalLineTo(9f)
                    lineTo(11.93f, 9f)
                    horizontalLineTo(7f)
                    verticalLineToRelative(11.67f)
                    curveTo(7f, 21.4f, 7.6f, 22f, 8.33f, 22f)
                    horizontalLineToRelative(7.33f)
                    curveToRelative(0.74f, 0f, 1.34f, -0.6f, 1.34f, -1.33f)
                    verticalLineTo(9f)
                    horizontalLineToRelative(-4f)
                    verticalLineToRelative(3.5f)
                    close()
                }
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
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

        val expected = ImageVector.Builder(
            name = "ComposeColor",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(1f, 1f)
                lineTo(5f, 1f)
                lineTo(5f, 5f)
                lineTo(1f, 5f)
                close()
            }
            path(fill = SolidColor(Color.DarkGray)) {
                moveTo(6f, 1f)
                lineTo(10f, 1f)
                lineTo(10f, 5f)
                lineTo(6f, 5f)
                close()
            }
            path(fill = SolidColor(Color.Gray)) {
                moveTo(11f, 1f)
                lineTo(15f, 1f)
                lineTo(15f, 5f)
                lineTo(11f, 5f)
                close()
            }
            path(fill = SolidColor(Color.LightGray)) {
                moveTo(1f, 6f)
                lineTo(5f, 6f)
                lineTo(5f, 10f)
                lineTo(1f, 10f)
                close()
            }
            path(fill = SolidColor(Color.White)) {
                moveTo(6f, 6f)
                lineTo(10f, 6f)
                lineTo(10f, 10f)
                lineTo(6f, 10f)
                close()
            }
            path(fill = SolidColor(Color.Red)) {
                moveTo(11f, 6f)
                lineTo(15f, 6f)
                lineTo(15f, 10f)
                lineTo(11f, 10f)
                close()
            }
            path(fill = SolidColor(Color.Green)) {
                moveTo(1f, 11f)
                lineTo(5f, 11f)
                lineTo(5f, 15f)
                lineTo(1f, 15f)
                close()
            }
            path(fill = SolidColor(Color.Blue)) {
                moveTo(6f, 11f)
                lineTo(10f, 11f)
                lineTo(10f, 15f)
                lineTo(6f, 15f)
                close()
            }
            path(fill = SolidColor(Color.Yellow)) {
                moveTo(11f, 11f)
                lineTo(15f, 11f)
                lineTo(15f, 15f)
                lineTo(11f, 15f)
                close()
            }
            path(fill = SolidColor(Color.Cyan)) {
                moveTo(4.5f, 4.5f)
                lineTo(8.5f, 4.5f)
                lineTo(8.5f, 8.5f)
                lineTo(4.5f, 8.5f)
                close()
            }
            path(fill = SolidColor(Color.Magenta)) {
                moveTo(9.5f, 9.5f)
                lineTo(12.5f, 9.5f)
                lineTo(12.5f, 12.5f)
                lineTo(9.5f, 12.5f)
                close()
            }
            path(fill = SolidColor(Color.Red.copy(alpha = 0.5019608f))) {
                moveTo(1f, 8.5f)
                lineTo(4.5f, 8.5f)
                lineTo(4.5f, 12f)
                lineTo(1f, 12f)
                close()
            }
            path(fill = SolidColor(Color.Green.copy(alpha = 0.5019608f))) {
                moveTo(5f, 8.5f)
                lineTo(8.5f, 8.5f)
                lineTo(8.5f, 12f)
                lineTo(5f, 12f)
                close()
            }
            path(fill = SolidColor(Color.Blue.copy(alpha = 0.5019608f))) {
                moveTo(9f, 8.5f)
                lineTo(12.5f, 8.5f)
                lineTo(12.5f, 12f)
                lineTo(9f, 12f)
                close()
            }
            path(fill = SolidColor(Color.Black.copy(alpha = 0.2509804f))) {
                moveTo(2f, 2f)
                lineTo(6f, 2f)
                lineTo(6f, 6f)
                lineTo(2f, 6f)
                close()
            }
            path(fill = SolidColor(Color.White.copy(alpha = 0.7490196f))) {
                moveTo(6f, 2f)
                lineTo(10f, 2f)
                lineTo(10f, 6f)
                lineTo(6f, 6f)
                close()
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }
}
