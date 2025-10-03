package io.github.composegears.valkyrie.gutter

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.PsiElement
import com.intellij.ui.awt.RelativePoint
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtProperty

class ImageVectorGutterProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: List<PsiElement?>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        val propertyCache = mutableMapOf<KtProperty, IrImageVector?>()

        // Process ImageVector property definitions
        elements.filterIsInstance<KtProperty>()
            .filter { it.isImageVector() }
            .forEach { property ->
                val irImageVector = parseImageVectorProperty(property)
                propertyCache[property] = irImageVector

                irImageVector?.let {
                    property.nameIdentifier?.let { identifier ->
                        createGutterIcon(identifier, it, property.name.orEmpty())?.let(result::add)
                    }
                }
            }

        // Process ImageVector references
        elements.filterIsInstance<KtNameReferenceExpression>()
            .mapNotNull { reference ->
                val referencedProperty = reference.references
                    .mapNotNull { it.resolve() as? KtProperty }
                    .firstOrNull { it.isImageVector() }
                    ?: return@mapNotNull null

                val irImageVector = propertyCache.getOrPut(referencedProperty) {
                    parseImageVectorProperty(referencedProperty)
                } ?: return@mapNotNull null

                createGutterIcon(reference, irImageVector, referencedProperty.name.orEmpty())
            }
            .forEach(result::add)
    }

    private fun KtProperty.isImageVector(): Boolean = typeReference?.text in IMAGEVECTOR_TYPES

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
        irImageVector: IrImageVector,
        name: String,
    ): LineMarkerInfo<T>? {
        val icon = createIconFromImageVector(irImageVector) ?: return null

        return LineMarkerInfo(
            element,
            element.textRange,
            icon,
            { "Vector Icon: $name" },
            { event, _ ->
                showIconPreviewPopup(event, irImageVector, name)
            },
            GutterIconRenderer.Alignment.LEFT,
            { "Vector Icon: $name" },
        )
    }

    private fun createIconFromImageVector(irImageVector: IrImageVector): Icon? = try {
        VdPreview.getPreviewFromVectorXml(
            VdPreview.TargetSize.createFromMaxDimension(GUTTER_ICON_SIZE),
            irImageVector.toVectorXmlString(),
            StringBuilder(),
        )?.let(::ImageIcon)
    } catch (_: Exception) {
        null
    }

    private fun showIconPreviewPopup(
        event: MouseEvent,
        irImageVector: IrImageVector,
        name: String,
    ) {
        val previewImage = try {
            VdPreview.getPreviewFromVectorXml(
                VdPreview.TargetSize.createFromMaxDimension(POPUP_ICON_SIZE),
                irImageVector.toVectorXmlString(),
                StringBuilder(),
            )
        } catch (_: Exception) {
            null
        } ?: return

        val panel = JPanel(BorderLayout()).apply {
            add(JLabel(ImageIcon(previewImage)), BorderLayout.CENTER)
            add(JLabel(name), BorderLayout.SOUTH)
            border = BorderFactory.createEmptyBorder(POPUP_PADDING, POPUP_PADDING, POPUP_PADDING, POPUP_PADDING)
        }

        JBPopupFactory.getInstance()
            .createComponentPopupBuilder(panel, null)
            .setTitle("Vector Icon Preview")
            .setResizable(false)
            .setMovable(false)
            .setRequestFocus(true)
            .createPopup()
            .show(RelativePoint(event))
    }

    private companion object {
        const val GUTTER_ICON_SIZE = 16
        const val POPUP_ICON_SIZE = 64
        const val POPUP_PADDING = 5
        const val COMPILED_CODE_MARKER = "/* compiled code */"

        val IMAGEVECTOR_TYPES = setOf(
            "ImageVector",
            "androidx.compose.ui.graphics.vector.ImageVector",
        )
    }
}
