package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertFailure
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import java.io.EOFException
import org.junit.jupiter.api.Test

class BrokenSvgParsingTest {

    @Test
    fun `parsing broken svg`() {
        val icon = getResourcePath("imagevector/broken.svg")

        assertFailure { SvgXmlParser.toIrImageVector(icon) }.isInstanceOf(EOFException::class)
    }
}
