package io.github.composegears.valkyrie.parser.unified.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class IconNameFormatterTest {

    private data class IconTest(
        val fileName: String,
        val expected: String,
    )

    @Test
    fun `check icon name formatting`() {
        val fileNames = listOf(
            IconTest(fileName = "_ic_test_icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon.xml", expected = "TestIcon"),
            IconTest(fileName = "test_icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon2.svg", expected = "TestIcon2"),
            IconTest(fileName = "ic_test_icon_name.svg", expected = "TestIconName"),
            IconTest(fileName = "ic_testicon.svg", expected = "Testicon"),
            IconTest(fileName = "ic_test_icon_name_with_underscores.svg", expected = "TestIconNameWithUnderscores"),
            IconTest(fileName = "ic_SVGIcon.svg", expected = "SVGIcon"),
            IconTest(fileName = "ic-test-icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test@icon!.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon123.xml", expected = "TestIcon123"),
            IconTest(fileName = "my_icon.xml", expected = "MyIcon"),
            IconTest(fileName = "Ic_TeSt123Icon.svg", expected = "TeSt123Icon"),
            IconTest(fileName = "ic_special@#\$%^&*()icon.svg", expected = "SpecialIcon"),
            IconTest(fileName = "ic--test__icon---name.svg", expected = "TestIconName"),
            IconTest(fileName = "@#$%.svg", expected = ""),
            IconTest(fileName = "", expected = ""),
            IconTest(fileName = "-_ic_test_icon_-.svg", expected = "TestIcon"),
            IconTest(fileName = "pos_1", expected = "Pos1"),
            IconTest(fileName = "1", expected = "1"),
            IconTest(fileName = "Ic_TempSvg123Icon.svg", expected = "TempSvg123Icon"),
            IconTest(fileName = "fitContent", expected = "FitContent"),
            IconTest(fileName = "fitContent_dark", expected = "FitContentDark"),
            IconTest(fileName = "stub@20x20", expected = "Stub20X20"),
            IconTest(fileName = "comment add-03.svg", expected = "CommentAdd03"),
            IconTest(fileName = "image-flip  -horizontal.svg", expected = "ImageFlipHorizontal"),
        )

        fileNames.forEach {
            val iconName = IconNameFormatter.format(it.fileName)

            assertThat(iconName).isEqualTo(it.expected)
        }
    }

    @Test
    fun `check case-insensitive collision detection`() {
        // These files produce different icon names that collide on case-insensitive file systems
        val testCases = listOf(
            IconTest(fileName = "test-icon.svg", expected = "TestIcon"),
            IconTest(fileName = "testicon.svg", expected = "Testicon"),
        )

        testCases.forEach {
            val iconName = IconNameFormatter.format(it.fileName)
            assertThat(iconName).isEqualTo(it.expected)
        }
    }
}
