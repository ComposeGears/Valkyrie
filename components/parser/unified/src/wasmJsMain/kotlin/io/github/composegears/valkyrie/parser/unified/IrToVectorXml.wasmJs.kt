package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.kmp.xml.ImageVectorToXmlParser
import kotlinx.io.files.Path

actual fun IrImageVector.toVectorXmlString(): String = ImageVectorToXmlParser.parse(this)

actual fun IrImageVector.toVectorXmlFile(outputPath: Path) {
    // File writing is not supported in wasmJs
    throw UnsupportedOperationException("File writing is not supported in wasmJs platform")
}

actual fun IrImageVector.toVectorXmlFile(outputFilePath: String) {
    // File writing is not supported in wasmJs
    throw UnsupportedOperationException("File writing is not supported in wasmJs platform")
}
