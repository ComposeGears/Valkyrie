package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class ImageVectorXmlViewModelTest {

    @Test
    fun `sanitizeResourceName lowercases uppercase filename`() {
        assertThat(sanitizeResourceName("Kotlin")).isEqualTo("kotlin")
    }

    @Test
    fun `sanitizeResourceName replaces hyphen with underscore`() {
        assertThat(sanitizeResourceName("my-icon")).isEqualTo("my_icon")
    }

    @Test
    fun `sanitizeResourceName replaces punctuation with underscore`() {
        assertThat(sanitizeResourceName("My.Icon")).isEqualTo("my_icon")
        assertThat(sanitizeResourceName("My Icon-Name!")).isEqualTo("my_icon_name")
    }

    @Test
    fun `sanitizeResourceName normalizes Material icon name with dot separator`() {
        // irImageVector.name coming from an Icons.Filled.WithoutPath declaration
        assertThat(sanitizeResourceName("Filled.WithoutPath")).isEqualTo("filled_withoutpath")
    }

    @Test
    fun `sanitizeResourceName keeps already valid name`() {
        assertThat(sanitizeResourceName("icon")).isEqualTo("icon")
        assertThat(sanitizeResourceName("my_icon_2")).isEqualTo("my_icon_2")
    }

    @Test
    fun `sanitizeResourceName lowercases mixed-case names`() {
        assertThat(sanitizeResourceName("UPPER_CASE")).isEqualTo("upper_case")
        assertThat(sanitizeResourceName("OutlinedIcon")).isEqualTo("outlinedicon")
    }

    @Test
    fun `sanitizeResourceName falls back to icon for empty name`() {
        assertThat(sanitizeResourceName("")).isEqualTo("icon")
    }

    @Test
    fun `sanitizeResourceName preserves digits-only name`() {
        // digits are allowed (in [a-z0-9]), so the name survives unchanged with no fallback
        assertThat(sanitizeResourceName("123")).isEqualTo("123")
    }

    @Test
    fun `sanitizeResourceName falls back to icon for punctuation-only name`() {
        assertThat(sanitizeResourceName("---...")).isEqualTo("icon")
    }

    @Test
    fun `preferred name is ic_ plus sanitized filename for uppercase source`() {
        assertThat("ic_${sanitizeResourceName("Kotlin")}").isEqualTo("ic_kotlin")
    }

    @Test
    fun `preferred name is ic_ plus sanitized filename for punctuation source`() {
        assertThat("ic_${sanitizeResourceName("my-icon")}").isEqualTo("ic_my_icon")
    }
}
