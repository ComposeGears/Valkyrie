package io.github.composegears.valkyrie.parser.jvm.svg

import com.android.ide.common.vectordrawable.Svg2Vector
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText

object SvgToXmlParser {

    fun parse(input: String): String = parse(createTempFile().apply { writeText(input) })

    fun parse(input: Path): String = ByteArrayOutputStream().use {
        Svg2Vector.parseSvgToXml(input, it)
        it.toString()
    }
}
