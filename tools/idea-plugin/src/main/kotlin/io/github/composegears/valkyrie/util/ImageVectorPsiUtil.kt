package io.github.composegears.valkyrie.util

import io.github.composegears.valkyrie.completion.ImageVectorIcon
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.ir.util.aspectRatio
import io.github.composegears.valkyrie.sdk.ir.util.dominantShadeColor
import io.github.composegears.valkyrie.sdk.ir.xml.toVectorXmlString
import javax.swing.Icon
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

val IMAGE_VECTOR_TYPES = setOf(
    "ImageVector",
    "androidx.compose.ui.graphics.vector.ImageVector",
)

/**
 * Checks if this property is an ImageVector based on its type reference.
 */
fun KtProperty.isImageVector(): Boolean {
    val typeText = typeReference?.text ?: return false
    return typeText in IMAGE_VECTOR_TYPES
}

/**
 * Checks if this Kotlin file contains ImageVector properties.
 *
 * A file is considered to contain ImageVector if it has at least one top-level
 * property with an ImageVector type annotation.
 */
fun KtFile.hasImageVectorProperties(): Boolean {
    val properties = declarations.filterIsInstance<KtProperty>()
    if (properties.isEmpty()) return false

    return properties.any { it.isImageVector() }
}

/**
 * Creates an icon representation from this Kotlin file containing an ImageVector.
 *
 * This function parses the ImageVector definition and renders it as a Swing Icon
 * suitable for display in the IDE UI (project view, gutter, completion, etc.).
 */
fun KtFile.createImageVectorIcon(): Icon? {
    val irImageVector = ImageVectorPsiParser.parseToIrImageVector(this) ?: return null
    val vectorXml = irImageVector.toVectorXmlString()

    return ImageVectorIcon(
        vectorXml = vectorXml,
        aspectRatio = irImageVector.aspectRatio,
        dominantShade = irImageVector.dominantShadeColor,
    )
}
