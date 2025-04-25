package io.github.composegears.valkyrie.parser.jvm.svg

import com.android.ide.common.vectordrawable.Svg2Vector
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream
import kotlin.io.path.readText
import kotlin.io.path.writeText

object SvgToXmlParser {

    fun parse(path: Path): String {
        val tmpPath = createTempFile()
        parse(path, tmpPath)
        return tmpPath.readText()
    }

    fun parse(input: String): String {
        val tmpInPath = createTempFile().apply { writeText(input) }
        val tmpOutPath = createTempFile()

        parse(tmpInPath, tmpOutPath)
        return tmpOutPath.readText()
    }

    private fun parse(input: Path, output: Path) {
        Svg2Vector.parseSvgToXml(input, output.outputStream())
    }
}
