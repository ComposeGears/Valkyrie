package io.github.composegears.valkyrie.parser.unified

import io.github.composegears.valkyrie.ir.IrImageVector
import kotlinx.io.files.Path

/**
 * Unified entry point to convert IrImageVector into Android VectorDrawable XML.
 * Implemented per platform; JVM and KMP delegate to their respective XML backends.
 */
expect fun IrImageVector.toVectorXmlString(): String

/**
 * Unified entry point to write IrImageVector to XML file.
 * Implemented per platform; JVM delegates to the JVM XML backend.
 */
expect fun IrImageVector.toVectorXmlFile(outputPath: Path)

/**
 * Unified entry point to write IrImageVector to XML file.
 * Implemented per platform; JVM delegates to the JVM XML backend.
 */
expect fun IrImageVector.toVectorXmlFile(outputFilePath: String)
