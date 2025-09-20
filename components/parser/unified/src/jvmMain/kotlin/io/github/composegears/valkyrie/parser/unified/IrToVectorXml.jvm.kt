package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.kmp.xml.ImageVectorToXmlParser

actual fun IrImageVector.toVectorXmlString(): String = ImageVectorToXmlParser.parse(this)
