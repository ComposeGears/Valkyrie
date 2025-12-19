package io.github.composegears.valkyrie.util

import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import io.github.composegears.valkyrie.completion.ImageVectorIcon
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.util.aspectRatio
import io.github.composegears.valkyrie.sdk.ir.util.dominantShadeColor
import io.github.composegears.valkyrie.sdk.ir.xml.toVectorXmlString
import javax.swing.Icon
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

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
 * Gets or creates a cached icon for a Kotlin file containing an ImageVector.
 *
 * Uses the PSI caching mechanism to avoid recreating the icon repeatedly
 * for the same file, improving performance.
 */
fun KtFile.getOrCreateCachedIcon(): Icon? {
    return CachedValuesManager.getCachedValue(this) {
        val icon = createImageVectorIcon()
        CachedValueProvider.Result.create(icon, this)
    }
}

/**
 * Gets or creates a cached gutter icon for an ImageVector property.
 *
 * This function uses the PSI caching mechanism to avoid recreating icons repeatedly
 * for the same property, improving performance.
 */
fun KtProperty.getOrCreateGutterIcon(): Icon? {
    return CachedValuesManager.getCachedValue(this) {
        val icon = createIcon()

        CachedValueProvider.Result.create(icon, this)
    }
}

/**
 * Creates an icon representation from this Kotlin file containing an ImageVector.
 *
 * This function parses the ImageVector definition and renders it as a Swing Icon
 * suitable for display in the IDE UI (project view, gutter, completion, etc.).
 */
private fun KtFile.createImageVectorIcon(): Icon? = ImageVectorPsiParser.parseToIrImageVector(this)
    ?.toIcon()

private fun KtProperty.createIcon(): Icon? = parseImageVectorProperty(this)
    ?.toIcon()

private fun IrImageVector.toIcon(): Icon = ImageVectorIcon(
    vectorXml = toVectorXmlString(),
    aspectRatio = aspectRatio,
    dominantShade = dominantShadeColor,
)

private fun parseImageVectorProperty(property: KtProperty): IrImageVector? {
    // Try parsing the current file
    val containingFile = property.containingKtFile
    ImageVectorPsiParser.parseToIrImageVector(containingFile)?.let { return it }

    // For properties from libraries, navigate to decompiled/attached source
    val navigationElement = property.navigationElement
    if (navigationElement is KtProperty && navigationElement != property) {
        val sourceFile = navigationElement.containingKtFile

        // Only parse if we have actual source code (not a stub)
        if (COMPILED_CODE_MARKER !in sourceFile.text) {
            return ImageVectorPsiParser.parseToIrImageVector(sourceFile)
        }
    }

    return null
}

private val IMAGE_VECTOR_TYPES = setOf(
    "ImageVector",
    "androidx.compose.ui.graphics.vector.ImageVector",
)

private const val COMPILED_CODE_MARKER = "/* compiled code */"
