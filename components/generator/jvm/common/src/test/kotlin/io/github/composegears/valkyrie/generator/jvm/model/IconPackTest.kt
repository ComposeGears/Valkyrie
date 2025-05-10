package io.github.composegears.valkyrie.generator.jvm.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IconPackTest {

    @Test
    fun `toString should handle empty IconPack`() {
        val iconPack = IconPack(name = "")
        val expected = ""

        assertEquals(expected, iconPack.toString())
    }

    @Test
    fun `toString should return formatted tree structure`() {
        val iconPack = IconPack(
            name = "Root",
            nested = listOf(
                IconPack(
                    name = "Child1",
                    nested = listOf(
                        IconPack(name = "Grandchild1"),
                        IconPack(name = "Grandchild2"),
                    ),
                ),
                IconPack(name = "Child2"),
            ),
        )

        val expected = """

            Root:
            ├── Child1
            │	├── Grandchild1
            │	└── Grandchild2
            └── Child2

        """.trimIndent()

        assertEquals(expected, iconPack.toString())
    }

    @Test
    fun `toString should handle deeply nested IconPack`() {
        val iconPack = IconPack(
            name = "Root",
            nested = listOf(
                IconPack(
                    name = "Child1",
                    nested = listOf(
                        IconPack(
                            name = "Grandchild1",
                            nested = listOf(
                                IconPack(
                                    name = "GreatGrandchild1",
                                    nested = listOf(
                                        IconPack(name = "GreatGreatGrandchild1"),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
                IconPack(
                    name = "Child2",
                    nested = listOf(
                        IconPack(name = "Grandchild2"),
                    ),
                ),
            ),
        )

        val expected = """

            Root:
            ├── Child1
            │	└── Grandchild1
            │		└── GreatGrandchild1
            │			└── GreatGreatGrandchild1
            └── Child2
            	└── Grandchild2

        """.trimIndent()

        assertEquals(expected, iconPack.toString())
    }
}
