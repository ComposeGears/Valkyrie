package io.github.composegears.valkyrie.sdk.ir.xml

import io.github.composegears.valkyrie.sdk.generator.xml.IrToXmlGenerator
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

fun IrImageVector.toVectorXmlString(): String = IrToXmlGenerator.generate(this)
