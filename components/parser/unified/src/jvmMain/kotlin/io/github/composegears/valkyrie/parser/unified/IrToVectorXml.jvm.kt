package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.jvm.xml.ImageVectorToXmlParser
import java.nio.file.Path as JavaPath
import kotlinx.io.files.Path

actual fun IrImageVector.toVectorXmlString(): String = ImageVectorToXmlParser.parse(this)

actual fun IrImageVector.toVectorXmlFile(outputPath: Path) {
    ImageVectorToXmlParser.parse(this, JavaPath.of(outputPath.toString()))
}

actual fun IrImageVector.toVectorXmlFile(outputFilePath: String) {
    ImageVectorToXmlParser.parse(this, outputFilePath)
}
