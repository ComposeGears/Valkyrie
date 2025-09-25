package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.ir.IrImageVector

/**
 * Unified entry point to convert IrImageVector into Android VectorDrawable XML.
 * Implemented per platform; JVM and KMP delegate to their respective XML backends.
 */
expect fun IrImageVector.toVectorXmlString(): String
