package io.github.composegears.valkyrie.converter.figma

import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.Test
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FigmaConverterTest {

    @Test
    fun `normalizeIconName formats raw file names`() {
        assertThat(normalizeIconName("comment add-03.svg")).isEqualTo("CommentAdd03")
    }

    @Test
    fun `convertSvg returns success payload for valid svg`() {
        val json = convertSvg(
            svg = """
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <path d="M3 12h18"/>
            </svg>
            """.trimIndent(),
            iconName = "ic_test_icon",
            packageName = "com.example.icons",
        )
        val result = Json.decodeFromString<ConverterResult>(json)

        assertThat(result).isInstanceOf<ConverterResult.Success>()
        assertThat(json.jsonType()).isEqualTo("success")

        val success = result as ConverterResult.Success
        assertThat(success.iconName).isEqualTo("TestIcon")
        assertThat(success.fileName).isEqualTo("TestIcon.kt")
        assertThat(success.code).all {
            contains("package com.example.icons")
            contains("val TestIcon: ImageVector")
        }
    }

    @Test
    fun `convertSvg returns error payload for invalid svg`() {
        val json = convertSvg(
            svg = "this is not svg",
            iconName = "broken_icon",
            packageName = "com.example.icons",
        )
        val result = Json.decodeFromString<ConverterResult>(json)

        assertThat(result).isInstanceOf<ConverterResult.Error>()
        assertThat(json.jsonType()).isEqualTo("error")
        val error = result as ConverterResult.Error
        assertThat(error.error).isEqualTo("Unsupported icon type")
        assertThat(error.iconName).isEqualTo("broken_icon")
    }

    @Test
    fun `convertSvg returns error payload for unsupported output format`() {
        val result = Json.decodeFromString<ConverterResult>(
            convertSvg(
                svg = """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M3 12h18"/>
                </svg>
                """.trimIndent(),
                iconName = "ic_test_icon",
                packageName = "com.example.icons",
                outputFormat = "unsupported",
            ),
        )

        assertThat(result).isInstanceOf<ConverterResult.Error>()

        val error = result as ConverterResult.Error
        assertThat(error.error).contains("Unsupported outputFormat")
        assertThat(error.iconName).isEqualTo("ic_test_icon")
    }

    private fun String.jsonType(): String = Json.parseToJsonElement(this).jsonObject.getValue("type").jsonPrimitive.content
}
