package io.github.composegears.valkyrie.converter.figma

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FigmaConverterTest {

    @Test
    fun `normalizeIconName formats raw file names`() {
        assertEquals("CommentAdd03", normalizeIconName("comment add-03.svg"))
    }

    @Test
    fun `convertSvg returns success payload for valid svg`() {
        val result = parseJson(
            convertSvg(
                svg = """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M3 12h18"/>
                </svg>
                """.trimIndent(),
                iconName = "ic_test_icon",
                packageName = "com.example.icons",
            ),
        )

        assertEquals("success", result["type"]?.jsonPrimitive?.content)
        assertEquals("TestIcon.kt", result["fileName"]?.jsonPrimitive?.content)
        assertEquals(result["code"]?.jsonPrimitive?.content?.contains("ImageVector"), true)
    }

    @Test
    fun `convertSvg returns error payload for invalid svg`() {
        val result = parseJson(
            convertSvg(
                svg = "this is not svg",
                iconName = "broken_icon",
                packageName = "com.example.icons",
            ),
        )

        assertEquals("error", result["type"]?.jsonPrimitive?.content)
        assertEquals(null, result["code"])
        assertEquals(result["error"]?.jsonPrimitive?.content?.isNotBlank(), true)
    }

    private fun parseJson(value: String) = Json.parseToJsonElement(value).jsonObject
}
