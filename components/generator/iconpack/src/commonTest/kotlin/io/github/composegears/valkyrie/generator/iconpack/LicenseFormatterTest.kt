package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class LicenseFormatterTest {

    @Test
    fun `raw text is wrapped in block comment`() {
        val result = "Copyright (c) 2024 Test".asBlockComment()

        assertThat(result).isEqualTo(
            """
            /*
             * Copyright (c) 2024 Test
             */
            """.trimIndent(),
        )
    }

    @Test
    fun `multiline raw text is wrapped in block comment`() {
        val result = "Copyright (c) 2024 Test\nAll rights reserved.".asBlockComment()

        assertThat(result).isEqualTo(
            """
            /*
             * Copyright (c) 2024 Test
             * All rights reserved.
             */
            """.trimIndent(),
        )
    }

    @Test
    fun `existing block comment is returned as-is`() {
        val license = "/*\n * Copyright (c) 2024 Test\n */"
        val result = license.asBlockComment()

        assertThat(result).isEqualTo(license)
    }

    @Test
    fun `crlf line endings are normalized to lf in block comment`() {
        val result = "Copyright (c) 2024 Test\r\nAll rights reserved.".asBlockComment()

        assertThat(result).isEqualTo(
            """
            /*
             * Copyright (c) 2024 Test
             * All rights reserved.
             */
            """.trimIndent(),
        )
    }

    @Test
    fun `empty string produces empty block comment`() {
        val result = "".asBlockComment()

        assertThat(result).isEqualTo("/*\n * \n */")
    }
}
