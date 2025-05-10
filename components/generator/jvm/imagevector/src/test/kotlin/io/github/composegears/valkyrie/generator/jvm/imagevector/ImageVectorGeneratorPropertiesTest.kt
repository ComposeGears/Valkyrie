package io.github.composegears.valkyrie.generator.jvm.imagevector

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ImageVectorGeneratorPropertiesTest {

    @Test
    fun `OutputFormat from function with valid key`() {
        val result = OutputFormat.from("backing_property")
        assertEquals(OutputFormat.BackingProperty, result)

        val resultLazy = OutputFormat.from("lazy_property")
        assertEquals(OutputFormat.LazyProperty, resultLazy)
    }

    @Test
    fun `OutputFormat from function with invalid key`() {
        val result = OutputFormat.from("invalid_key")
        assertEquals(OutputFormat.BackingProperty, result)
    }

    @Test
    fun `OutputFormat from function with null key`() {
        val result = OutputFormat.from(null)
        assertEquals(OutputFormat.BackingProperty, result)
    }

    @Test
    fun `PreviewAnnotationType from function with valid key`() {
        val result = PreviewAnnotationType.from("androidx")
        assertEquals(PreviewAnnotationType.AndroidX, result)

        val resultJetbrains = PreviewAnnotationType.from("jetbrains")
        assertEquals(PreviewAnnotationType.Jetbrains, resultJetbrains)
    }

    @Test
    fun `PreviewAnnotationType from function with invalid key`() {
        val result = PreviewAnnotationType.from("invalid_key")
        assertEquals(PreviewAnnotationType.AndroidX, result)
    }

    @Test
    fun `PreviewAnnotationType from function with null key`() {
        val result = PreviewAnnotationType.from(null)
        assertEquals(PreviewAnnotationType.AndroidX, result)
    }
}
