package io.github.composegears.valkyrie.ir.xml

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.sdk.generator.xml.IrToXmlGenerator

fun IrImageVector.toVectorXmlString(): String = IrToXmlGenerator.generate(this)
