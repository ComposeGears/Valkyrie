package io.github.composegears.valkyrie.ir.xml

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.unified.toVectorXmlString as unifiedToVectorXml

fun IrImageVector.toVectorXmlString(): String {
    return unifiedToVectorXml()
}
