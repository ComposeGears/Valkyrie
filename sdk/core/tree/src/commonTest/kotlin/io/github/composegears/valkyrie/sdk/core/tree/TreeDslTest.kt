package io.github.composegears.valkyrie.sdk.core.tree

import kotlin.test.Test
import kotlin.test.assertEquals

class TreeDslTest {

    @Test
    fun `buildTree should create nested children`() {
        val tree = buildTree("root") {
            child("first")
            child("second") {
                child("nested")
            }
        }

        assertEquals("root", tree.data)
        assertEquals(2, tree.children.size)
        assertEquals(listOf("first", "second"), tree.children.map { it.data })
        assertEquals(1, tree.children[1].children.size)
        assertEquals(listOf("nested"), tree.children[1].children.map { it.data })
    }
}
