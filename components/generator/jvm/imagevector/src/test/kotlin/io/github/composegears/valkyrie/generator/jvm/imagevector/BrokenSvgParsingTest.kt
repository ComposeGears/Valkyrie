package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertFailure
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
import java.io.EOFException
import org.junit.jupiter.api.Test

class BrokenSvgParsingTest {

    @Test
    fun `parsing broken svg`() {
        val icon = getResourcePath("imagevector/broken.svg").toIOPath()

        assertFailure {
            SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        }.isInstanceOf(EOFException::class)
    }
}
