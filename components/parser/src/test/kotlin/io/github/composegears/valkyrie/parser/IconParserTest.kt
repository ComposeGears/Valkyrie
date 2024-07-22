package io.github.composegears.valkyrie.parser

import kotlin.test.Test
import kotlin.test.assertEquals

class IconParserTest {

    private data class IconTest(
        val fileName: String,
        val expected: String,
    )

    @Test
    fun `test icon name`() {
        val fileNames = listOf(
            IconTest(fileName = "ic_test_icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon.xml", expected = "TestIcon"),
            IconTest(fileName = "test_icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon2.svg", expected = "TestIcon2"),
            IconTest(fileName = "ic_test_icon_name.svg", expected = "TestIconName"),
            IconTest(fileName = "ic_testicon.svg", expected = "Testicon"),
            IconTest(fileName = "ic_test_icon_name_with_underscores.svg", expected = "TestIconNameWithUnderscores"),
            IconTest(fileName = "ic_TESTIcon.svg", expected = "Testicon"),
            IconTest(fileName = "ic-test-icon.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test@icon!.svg", expected = "TestIcon"),
            IconTest(fileName = "ic_test_icon123.xml", expected = "TestIcon123"),
            IconTest(fileName = "my_icon.xml", expected = "MyIcon"),
            IconTest(fileName = "Ic_TeSt123Icon.svg", expected = "Test123icon"),
            IconTest(fileName = "ic_special@#\$%^&*()icon.svg", expected = "SpecialIcon"),
            IconTest(fileName = "ic--test__icon---name.svg", expected = "TestIconName"),
            IconTest(fileName = "@#$%.svg", expected = ""),
            IconTest(fileName = "", expected = ""),
            IconTest(fileName = "-_ic_test_icon_-.svg", expected = "TestIcon"),
            IconTest(fileName = "pos_1", expected = "Pos1"),
            IconTest(fileName = "1", expected = "1"),
        )

        fileNames.forEach {
            val iconName = IconParser.getIconName(it.fileName)

            assertEquals(expected = it.expected, actual = iconName)
        }
    }
}
