package io.github.composegears.valkyrie.parser.ktfile

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
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

class LinearGradientParserTest : BaseKtParserTest() {

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradient.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "LinearGradient",
            defaultWidth = 51.dp,
            defaultHeight = 63.dp,
            viewportWidth = 51f,
            viewportHeight = 63f,
        ).apply {
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(0.013f, 44.716f)
                reflectiveCurveToRelative(6.586f, 6.584f, 9.823f, 6.805f)
                curveToRelative(3.236f, 0.224f, 7.033f, 0f, 7.033f, 0f)
                reflectiveCurveToRelative(7.024f, 1.732f, 7.024f, 7.368f)
                verticalLineTo(63f)
                lineToRelative(3.195f, -0.014f)
                verticalLineToRelative(-5.571f)
                curveToRelative(0f, -6.857f, -10.053f, -7.567f, -10.053f, -7.567f)
                reflectiveCurveTo(11.957f, 41.979f, 0.013f, 44.716f)
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(51f, 30.326f)
                reflectiveCurveToRelative(-5.745f, 9.07f, -9.517f, 9.495f)
                curveToRelative(-3.1f, 0.348f, -6.542f, 0.107f, -8.12f, 0.262f)
                curveToRelative(-3.069f, 0.301f, -6.257f, 1.351f, -6.257f, 5.667f)
                verticalLineTo(63f)
                horizontalLineToRelative(-3.182f)
                verticalLineTo(44.546f)
                curveToRelative(0f, -0.964f, 0.006f, -5.235f, 7.093f, -6.584f)
                curveToRelative(1.208f, -0.232f, 3.688f, -0.281f, 4.913f, -0.281f)
                curveToRelative(0.001f, 0f, 4.521f, -7.73f, 15.07f, -7.355f)
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(4.031f, 16.042f)
                reflectiveCurveToRelative(0.669f, 3.435f, 2.899f, 6.315f)
                curveToRelative(2.232f, 2.878f, 4.147f, 4.891f, 6.489f, 4.891f)
                curveToRelative(2.344f, 0f, 6.208f, -0.01f, 7.68f, 0.868f)
                curveToRelative(1.837f, 1.095f, 2.803f, 3.213f, 2.803f, 5.373f)
                verticalLineTo(63f)
                horizontalLineToRelative(3.173f)
                verticalLineTo(33.489f)
                reflectiveCurveToRelative(-0.085f, -3.859f, -3.102f, -6.426f)
                curveToRelative(-1.651f, -1.405f, -2.911f, -2.141f, -5.294f, -2.141f)
                curveToRelative(-0.908f, 0f, -2.041f, -0.019f, -2.041f, -0.019f)
                reflectiveCurveTo(14.853f, 20.75f, 11.45f, 18.7f)
                curveToRelative(-3.404f, -2.049f, -7.419f, -2.658f, -7.419f, -2.658f)
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(39.967f, 0f)
                reflectiveCurveToRelative(0.803f, 7.891f, -2.625f, 11.654f)
                curveToRelative(-3.426f, 3.761f, -5.551f, 2.683f, -7.765f, 3.097f)
                curveToRelative(-1.969f, 0.369f, -2.479f, 1.772f, -2.479f, 3.984f)
                verticalLineToRelative(44.209f)
                horizontalLineToRelative(-3.101f)
                reflectiveCurveToRelative(-0.073f, -43.305f, -0.073f, -44.209f)
                reflectiveCurveToRelative(0.02f, -4.906f, 3.793f, -6.115f)
                curveToRelative(1.592f, -0.509f, 2.335f, -0.376f, 2.917f, -2.293f)
                curveTo(31.218f, 8.408f, 33.04f, 1.99f, 39.967f, 0f)
            }
            path(fill = SolidColor(Color(0xFF000000))) {
                moveTo(11.033f, 0f)
                reflectiveCurveToRelative(-0.802f, 7.891f, 2.625f, 11.654f)
                curveToRelative(3.426f, 3.761f, 5.55f, 2.683f, 7.765f, 3.097f)
                curveToRelative(1.969f, 0.369f, 2.479f, 1.772f, 2.479f, 3.984f)
                verticalLineToRelative(44.209f)
                horizontalLineToRelative(3.101f)
                reflectiveCurveToRelative(0.072f, -43.305f, 0.072f, -44.209f)
                reflectiveCurveToRelative(-0.019f, -4.906f, -3.792f, -6.115f)
                curveToRelative(-1.592f, -0.509f, -2.334f, -0.376f, -2.918f, -2.293f)
                curveTo(19.782f, 8.408f, 17.96f, 1.99f, 11.033f, 0f)
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
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.094f to Color(0xFFE7BD76),
                        0.33f to Color(0xFFC96C35),
                        0.524f to Color(0xFFC96E36),
                        0.614f to Color(0xFFCB7339),
                        0.681f to Color(0xFFCE7C40),
                        0.739f to Color(0xFFD2884A),
                        0.789f to Color(0xFFD99A59),
                        0.834f to Color(0xFFE2B26C),
                        0.844f to Color(0xFFE5B972),
                        1f to Color(0xFFF2D68B),
                    ),
                    start = Offset(1.627f, 28.701f),
                    end = Offset(31.5f, 58.575f),
                ),
            ) {
                moveTo(0f, 30.326f)
                reflectiveCurveToRelative(5.744f, 9.07f, 9.516f, 9.495f)
                curveToRelative(3.1f, 0.348f, 6.542f, 0.107f, 8.122f, 0.262f)
                curveToRelative(3.068f, 0.301f, 6.256f, 1.351f, 6.256f, 5.667f)
                verticalLineTo(63f)
                horizontalLineToRelative(3.181f)
                verticalLineTo(44.546f)
                curveToRelative(0f, -0.964f, -0.006f, -5.235f, -7.093f, -6.584f)
                curveToRelative(-1.207f, -0.232f, -3.687f, -0.281f, -4.913f, -0.281f)
                curveToRelative(-0.001f, 0f, -4.522f, -7.73f, -15.069f, -7.355f)
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.094f to Color(0xFFE7BD76),
                        0.226f to Color(0xFFC96C35),
                        0.376f to Color(0xFFC96E36),
                        0.491f to Color(0xFFCC753B),
                        0.596f to Color(0xFFD08143),
                        0.694f to Color(0xFFD59151),
                        0.786f to Color(0xFFDEA965),
                        0.834f to Color(0xFFE5B972),
                        1f to Color(0xFFF2D68B),
                    ),
                    start = Offset(44.384f, 14.55f),
                    end = Offset(18.29f, 59.746f),
                ),
            ) {
                moveTo(46.969f, 16.042f)
                reflectiveCurveToRelative(-0.669f, 3.435f, -2.898f, 6.315f)
                curveToRelative(-2.232f, 2.878f, -4.147f, 4.891f, -6.489f, 4.891f)
                curveToRelative(-2.344f, 0f, -6.208f, -0.01f, -7.68f, 0.868f)
                curveToRelative(-1.837f, 1.095f, -2.803f, 3.213f, -2.803f, 5.373f)
                verticalLineTo(63f)
                horizontalLineToRelative(-3.174f)
                verticalLineTo(33.489f)
                reflectiveCurveToRelative(0.086f, -3.859f, 3.103f, -6.426f)
                curveToRelative(1.651f, -1.405f, 2.911f, -2.141f, 5.295f, -2.141f)
                curveToRelative(0.907f, 0f, 2.041f, -0.019f, 2.041f, -0.019f)
                reflectiveCurveToRelative(1.785f, -4.153f, 5.187f, -6.203f)
                curveToRelative(3.403f, -2.049f, 7.418f, -2.658f, 7.418f, -2.658f)
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse icon with linear gradient and stroke`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/backing/LinearGradientWithStroke.kt",
            pathToBacking = "imagevector/kt/lazy/LinearGradientWithStroke.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "LinearGradientWithStroke",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.097f to Color(0xFF0095D5),
                        0.301f to Color(0xFF238AD9),
                        0.621f to Color(0xFF557BDE),
                        0.864f to Color(0xFF7472E2),
                        1f to Color(0xFF806EE3),
                    ),
                    start = Offset(6.384f, 29.605f),
                    end = Offset(17.723f, 18.267f),
                ),
            ) {
                moveTo(0f, 24f)
                lineTo(12.039f, 11.961f)
                lineTo(24f, 24f)
                close()
                moveTo(0f, 24f)
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.118f to Color(0xFF0095D5),
                        0.418f to Color(0xFF3C83DC),
                        0.696f to Color(0xFF6D74E1),
                        0.833f to Color(0xFF806EE3),
                    ),
                    start = Offset(1.684f, 4.824f),
                    end = Offset(8.269f, -1.762f),
                ),
            ) {
                moveTo(0f, 0f)
                lineTo(12.039f, 0f)
                lineTo(0f, 13f)
                close()
                moveTo(0f, 0f)
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.107f to Color(0xFFC757BC),
                        0.214f to Color(0xFFD0609A),
                        0.425f to Color(0xFFE1725C),
                        0.605f to Color(0xFFEE7E2F),
                        0.743f to Color(0xFFF58613),
                        0.823f to Color(0xFFF88909),
                    ),
                    start = Offset(-4.041f, 22.066f),
                    end = Offset(18.293f, -0.268f),
                ),
            ) {
                moveTo(12.039f, 0f)
                lineTo(0f, 12.68f)
                lineTo(0f, 24f)
                lineTo(24f, 0f)
                close()
                moveTo(12.039f, 0f)
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = ParseType::class)
    fun `parse linear gradient with clip path`(parseType: ParseType) = runInEdtAndGet {
        val ktFile = parseType.toKtFile(
            project = project,
            pathToLazy = "imagevector/kt/lazy/ClipPathGradient.kt",
            pathToBacking = "imagevector/kt/backing/ClipPathGradient.kt",
        )
        val imageVector = KtFileToImageVectorParser.parse(ktFile)

        val expected = ImageVector.Builder(
            name = "ClipPathGradient",
            defaultWidth = 5000.dp,
            defaultHeight = 2916.dp,
            viewportWidth = 5000f,
            viewportHeight = 2916f,
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(1026f, 918f)
                    curveTo(1877f, 1321.7f, 2427f, 602.7f, 2721.6f, 0f)
                    horizontalLineTo(0f)
                    verticalLineToRelative(698.6f)
                    curveToRelative(264.3f, -29.7f, 602.1f, 18.2f, 1026f, 219.3f)
                },
            ) {
                path(
                    fill = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF00CCFF),
                            1f to Color(0xFF000066),
                        ),
                        start = Offset(413f, 1501.2f),
                        end = Offset(2500.6f, -349.7f),
                    ),
                ) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(2721.6f)
                    verticalLineToRelative(1321.7f)
                    horizontalLineToRelative(-2721.6f)
                    close()
                }
            }
            group(
                clipPathData = PathData {
                    moveTo(2721.6f, 0f)
                    curveToRelative(-294.6f, 602.7f, -844.7f, 1321.7f, -1695.6f, 918f)
                    curveTo(602.1f, 716.9f, 264.3f, 668.9f, 0f, 698.6f)
                    verticalLineToRelative(573.2f)
                    curveToRelative(331f, -133.9f, 782.3f, -132.4f, 1266f, 406.1f)
                    curveToRelative(1219.8f, 1358.1f, 2464f, -1169f, 3734f, -1120.8f)
                    verticalLineTo(0f)
                    horizontalLineToRelative(-2278.4f)
                    close()
                },
            ) {
                path(
                    fill = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF00CCFF),
                            1f to Color(0xFF000066),
                        ),
                        start = Offset(717.4f, 2963.9f),
                        end = Offset(4849.2f, -387.4f),
                    ),
                ) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(5000f)
                    verticalLineToRelative(3036.1f)
                    horizontalLineToRelative(-5000f)
                    close()
                }
            }
            group(
                clipPathData = PathData {
                    moveTo(5000f, 557.2f)
                    curveToRelative(-1270f, -48.2f, -2514.2f, 2478.9f, -3734f, 1120.8f)
                    curveTo(782.3f, 1139.5f, 331f, 1138f, 0f, 1271.9f)
                    verticalLineToRelative(566.4f)
                    curveToRelative(354f, -194.6f, 925.2f, -246.3f, 1626f, 629.7f)
                    curveToRelative(157.9f, 197.4f, 309.2f, 344f, 455f, 448f)
                    horizontalLineToRelative(1220.8f)
                    curveToRelative(566.6f, -357.5f, 1094.3f, -1069.6f, 1698.3f, -1323.7f)
                    verticalLineTo(557.2f)
                    close()
                },
            ) {
                path(
                    fill = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF00CCFF),
                            1f to Color(0xFF000066),
                        ),
                        start = Offset(304.5f, 3342f),
                        end = Offset(4838.1f, 101.2f),
                    ),
                ) {
                    moveTo(0f, 509f)
                    horizontalLineToRelative(5000f)
                    verticalLineToRelative(2527.1f)
                    horizontalLineToRelative(-5000f)
                    close()
                }
            }
            group(
                clipPathData = PathData {
                    moveTo(5000f, 1592.3f)
                    curveToRelative(-604f, 254.1f, -1131.7f, 966.3f, -1698.3f, 1323.7f)
                    horizontalLineToRelative(1698.3f)
                    verticalLineToRelative(-1323.7f)
                    close()
                    moveTo(1626f, 2468f)
                    curveTo(925.2f, 1592f, 354f, 1643.7f, 0f, 1838.3f)
                    verticalLineToRelative(1077.7f)
                    horizontalLineToRelative(2081f)
                    curveToRelative(-145.8f, -104.1f, -297f, -250.6f, -455f, -448f)
                    close()
                },
            ) {
                path(
                    fill = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF00CCFF),
                            1f to Color(0xFF000066),
                        ),
                        start = Offset(1225.5f, 3877.8f),
                        end = Offset(3836.7f, 550.9f),
                    ),
                ) {
                    moveTo(0f, 1592f)
                    horizontalLineToRelative(5000f)
                    verticalLineToRelative(1324f)
                    horizontalLineToRelative(-5000f)
                    close()
                }
            }
        }.build()

        assertThat(imageVector).isEqualTo(expected)
    }
}
