package io.github.composegears.valkyrie.processing.parser

import com.android.ide.common.vectordrawable.Svg2Vector
import java.io.File
import java.nio.file.Path
import kotlin.io.path.outputStream

object SvgToXmlParser {

    fun parse(file: File, outPath: Path) {
        Svg2Vector.parseSvgToXml(file.toPath(), outPath.outputStream())
    }
}