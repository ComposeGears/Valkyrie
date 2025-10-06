package io.github.composegears.valkyrie.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.ide.util.EditSourceUtil
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import io.github.composegears.valkyrie.completion.ImageVectorIcon
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import javax.swing.Icon
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtProperty

class ImageVectorGutterProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: List<PsiElement?>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        // Process ImageVector property definitions
        elements.filterIsInstance<KtProperty>()
            .filter { it.isImageVector() }
            .forEach { property ->
                val icon = getOrCreateGutterIcon(property)

                icon?.let {
                    property.nameIdentifier?.let { identifier ->
                        // nameIdentifier is already a leaf (LeafPsiElement)
                        createGutterIcon(
                            element = identifier,
                            icon = icon,
                            name = property.name.orEmpty(),
                            navigationTarget = null,
                        ).let(result::add)
                    }
                }
            }

        // Process ImageVector references
        elements.filterIsInstance<KtNameReferenceExpression>()
            .mapNotNull { reference ->
                val referencedProperty = reference.references
                    .mapNotNull { it.resolve().safeAs<KtProperty>() }
                    .firstOrNull { it.isImageVector() }
                    ?: return@mapNotNull null

                val icon = getOrCreateGutterIcon(referencedProperty) ?: return@mapNotNull null

                // Get the leaf element (identifier) from the reference expression
                // due to LineMarker is supposed to be registered for leaf elements only
                val leafElement = reference.firstChild ?: reference

                createGutterIcon(
                    element = leafElement,
                    icon = icon,
                    name = referencedProperty.name.orEmpty(),
                    navigationTarget = referencedProperty,
                )
            }
            .forEach(result::add)
    }

    private fun getOrCreateGutterIcon(ktProperty: KtProperty): Icon? {
        val cachedValuesManager = CachedValuesManager.getManager(ktProperty.project)

        return cachedValuesManager.getCachedValue(ktProperty) {
            val icon = ktProperty.createIcon()

            CachedValueProvider.Result.create(icon, ktProperty)
        }
    }

    private fun KtProperty.createIcon(): Icon? {
        val irImageVector = parseImageVectorProperty(this) ?: return null
        val vectorXml = irImageVector.toVectorXmlString()

        return ImageVectorIcon(vectorXml = vectorXml)
    }

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

    private fun <T : PsiElement> createGutterIcon(
        element: T,
        icon: Icon,
        name: String,
        navigationTarget: T?,
    ): LineMarkerInfo<T> = LineMarkerInfo(
        element,
        element.textRange,
        icon,
        { "ImageVector Icon: $name" },
        { _, _ ->
            navigationTarget?.createSmartPointer()?.let { target ->
                target.element
                    ?.let(EditSourceUtil::getDescriptor)
                    ?.takeIf(Navigatable::canNavigate)
                    ?.navigate(true)
            }
        },
        GutterIconRenderer.Alignment.LEFT,
        { "ImageVector Icon: $name" },
    )

    private companion object {
        private const val COMPILED_CODE_MARKER = "/* compiled code */"

        private val IMAGE_VECTOR_TYPES = setOf(
            "ImageVector",
            "androidx.compose.ui.graphics.vector.ImageVector",
        )

        private fun KtProperty.isImageVector(): Boolean = typeReference?.text in IMAGE_VECTOR_TYPES
    }
}
