package io.github.composegears.valkyrie.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.ide.util.EditSourceUtil
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.util.getOrCreateGutterIcon
import io.github.composegears.valkyrie.util.isImageVector
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
                val icon = property.getOrCreateGutterIcon()
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

                val icon = referencedProperty.getOrCreateGutterIcon() ?: return@mapNotNull null

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

    private fun <T : PsiElement> createGutterIcon(
        element: T,
        icon: Icon,
        name: String,
        navigationTarget: T?,
    ): LineMarkerInfo<T> {
        val tooltipName = name.takeIf { it.isNotBlank() } ?: "ImageVector Icon"

        return LineMarkerInfo(
            element,
            element.textRange,
            icon,
            { tooltipName },
            { _, _ ->
                navigationTarget?.createSmartPointer()?.let { target ->
                    target.element
                        ?.let(EditSourceUtil::getDescriptor)
                        ?.takeIf(Navigatable::canNavigate)
                        ?.navigate(true)
                }
            },
            GutterIconRenderer.Alignment.LEFT,
            { tooltipName },
        )
    }
}
