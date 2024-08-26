package io.github.composegears.valkyrie.parser.svgxml

import com.android.ide.common.vectordrawable.Svg2Vector
import java.nio.file.Path
import kotlin.io.path.outputStream

object SvgToXmlParser {

    fun parse(input: Path, output: Path) {
        Svg2Vector.parseSvgToXml(input, output.outputStream())
    }
}
