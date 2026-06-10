package io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.core.tree.get
import kotlin.test.Test
import kotlin.test.assertEquals

class IconPackTreeTest {

    @Test
    fun `toString should handle empty IconPack`() {
        val iconPack = iconPackOf("")
        val expected = ""

        assertEquals(expected, iconPack.toPrettyString())
    }

    @Test
    fun `toString should return formatted tree structure`() {
        val iconPack = buildTree("Root") {
            child("Child1") {
                child("Grandchild1")
                child("Grandchild2")
            }
            child("Child2")
        }

        val expected = """

            Root:
            ├── Child1
            │	├── Grandchild1
            │	└── Grandchild2
            └── Child2

        """.trimIndent()

        assertEquals(expected, iconPack.toPrettyString())
    }

    @Test
    fun `toString should handle deeply nested IconPack`() {
        val iconPack = buildTree("Root") {
            child("Child1") {
                child("Grandchild1") {
                    child("GreatGrandchild1") {
                        child("GreatGreatGrandchild1")
                    }
                }
            }
            child("Child2") {
                child("Grandchild2")
            }
        }

        val expected = """

            Root:
            ├── Child1
            │	└── Grandchild1
            │		└── GreatGrandchild1
            │			└── GreatGreatGrandchild1
            └── Child2
            	└── Grandchild2

        """.trimIndent()

        assertEquals(expected, iconPack.toPrettyString())
    }

    @Test
    fun `fromString parsing empty string`() {
        val input = ""
        val pack = iconPackOf(input)

        assertThat(pack.data).isEqualTo("")
        assertThat(pack.children).isEmpty()

        assertThat(pack.encode()).isEqualTo(input)
    }

    @Test
    fun `fromString parsing with multiple roots`() {
        val input = "RootA.Child1,RootB.Child2"

        assertFailure {
            iconPackOf(input)
        }.isInstanceOf(IllegalStateException::class)
    }

    @Test
    fun `fromString parsing single segment`() {
        val input = "Root"
        val result = iconPackOf(input)

        assertThat(result.data).isEqualTo("Root")
        assertThat(result.children).isEmpty()

        assertThat(result.encode()).isEqualTo(input)
    }

    @Test
    fun `fromString parsing L1 hierarchy`() {
        val input = "Root.Child1,Root.Child2"
        val result = iconPackOf(input)

        assertThat(result.data).isEqualTo("Root")
        assertThat(result.children).hasSize(2)
        assertThat(result["Child1"].children).isEmpty()
        assertThat(result["Child2"].children).isEmpty()

        assertThat(result.encode()).isEqualTo(input)
    }

    @Test
    fun `fromString parsing deeper hierarchy`() {
        val input = "Root.Child1,Root.Child1.GrandChild1,Root.Child2"
        val result = iconPackOf(input)

        assertThat(result.data).isEqualTo("Root")
        assertThat(result.children).hasSize(2)
        assertThat(result["Child1"].children).hasSize(1)
        assertThat(result["Child1"]["GrandChild1"].children).isEmpty()
        assertThat(result["Child2"].children).isEmpty()

        assertThat(result.encode()).isEqualTo("Root.Child1.GrandChild1,Root.Child2")
    }

    @Test
    fun `fromString parsing multiple levels deep`() {
        val input = "Root.Child1.GrandChild1.GreatGrandChild1,Root.Child2,Root.Child1"
        val result = iconPackOf(input)

        assertThat(result.data).isEqualTo("Root")
        assertThat(result.children).hasSize(2)
        assertThat(result["GrandChild1"].data).isEqualTo("GrandChild1")
        assertThat(result["GreatGrandChild1"].data).isEqualTo("GreatGrandChild1")
        assertThat(result["GreatGrandChild1"].children).isEmpty()

        assertThat(result.encode()).isEqualTo("Root.Child1.GrandChild1.GreatGrandChild1,Root.Child2")
    }

    @Test
    fun `fromString parsing with complex nested structure`() {
        val input = "Root.A.X,Root.A.Y,Root.B.Z,Root.B.W.V"
        val result = iconPackOf(input)

        assertThat(result.data).isEqualTo("Root")
        assertThat(result.children).hasSize(2)
        assertThat(result["A"].children).hasSize(2)
        assertThat(result["A"]["X"].children).isEmpty()
        assertThat(result["A"]["Y"].children).isEmpty()
        assertThat(result["B"].children).hasSize(2)
        assertThat(result["B"]["Z"].children).isEmpty()
        assertThat(result["B"]["W"].children).hasSize(1)
        assertThat(result["B"]["W"]["V"].children).isEmpty()

        assertThat(result.encode()).isEqualTo(input)
    }

    @Test
    fun `fromString parsing with shared paths`() {
        val input = "AAA.BB,AAA.CC,AAA.BB.FF,AAA.BB.FF.CC.AAA,AAA.CC.BB"
        val result = iconPackOf(input)
        val rootLevelBb = result.children.first { it.data == "BB" }
        val cc = result.children.first { it.data == "CC" }
        val ff = rootLevelBb.children.first { it.data == "FF" }
        val nestedCc = ff.children.first { it.data == "CC" }
        val nestedAaa = nestedCc.children.first { it.data == "AAA" }
        val ccLevelBb = cc.children.first { it.data == "BB" }

        assertThat(result.data).isEqualTo("AAA")
        assertThat(result.children).hasSize(2)
        assertThat(rootLevelBb.children).hasSize(1)
        assertThat(ff.children).hasSize(1)
        assertThat(nestedCc.children).hasSize(1)
        assertThat(nestedAaa.children).isEmpty()
        assertThat(cc.children).hasSize(1)
        assertThat(ccLevelBb.children).isEmpty()

        assertThat(result.encode()).isEqualTo("AAA.BB.FF.CC.AAA,AAA.CC.BB")
    }
}
