package io.github.composegears.valkyrie.gutter

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
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
    override fun getLineMarkerInfo(p0: PsiElement): LineMarkerInfo<*>? {
        return null
    }

    override fun collectSlowLineMarkers(elements: List<PsiElement?>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val propertyCache = mutableMapOf<KtProperty, IrImageVector?>()
        val vectorProperties = elements
            .filterIsInstance<KtProperty>()
            .filter { isImageVector(it) }

        for (property in vectorProperties) {
            val irImageVector = parseImageVectorProperty(property)
            propertyCache[property] = irImageVector

            if (irImageVector != null) {
                val nameIdentifier = property.nameIdentifier
                nameIdentifier?.let {
                    createGutterIcon(
                        element = nameIdentifier,
                        irImageVector = irImageVector,
                        name = property.name ?: "",
                    )?.let {
                        result.add(it)
                    }
                }
            }
        }

        val references = elements.filterIsInstance<KtNameReferenceExpression>()
        for (reference in references) {
            val referencedProperty = reference.references
                .mapNotNull { it.resolve() as? KtProperty }
                .firstOrNull { isImageVector(it) }
                ?: continue

            val irImageVector = propertyCache.getOrPut(referencedProperty) {
                parseImageVectorProperty(referencedProperty)
            } ?: continue

            createGutterIcon(
                element = reference,
                irImageVector = irImageVector,
                name = referencedProperty.name ?: "",
            )?.let {
                result.add(it)
            }
        }
    }

    private fun isImageVector(property: KtProperty): Boolean = property.typeReference?.text == "ImageVector" ||
        property.typeReference?.text == "androidx.compose.ui.graphics.vector.ImageVector"

    private fun <T : PsiElement> createGutterIcon(
        element: T,
        irImageVector: IrImageVector,
        name: String,
    ): LineMarkerInfo<T>? {
        val icon = createIconFromImageVector(irImageVector) ?: return null

        val navigationHandler = GutterIconNavigationHandler<T> { event, _ ->
            showIconPreviewPopup(event, irImageVector, name)
        }
        return LineMarkerInfo(
            element,
            element.textRange,
            icon,
            { "Vector Icon: $name" },
            navigationHandler,
            GutterIconRenderer.Alignment.LEFT,
            { "Vector Icon: $name" },
        )
    }

    private fun createIconFromImageVector(irImageVector: IrImageVector): Icon? {
        return try {
            val errorLog = StringBuilder()
            val previewImage = VdPreview.getPreviewFromVectorXml(
                VdPreview.TargetSize.createFromMaxDimension(16),
                irImageVector.toVectorXmlString(),
                errorLog,
            )

            if (previewImage != null) {
                ImageIcon(previewImage)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun showIconPreviewPopup(event: MouseEvent, irImageVector: IrImageVector, name: String) {
        val previewImage = try {
            val errorLog = StringBuilder()
            VdPreview.getPreviewFromVectorXml(
                VdPreview.TargetSize.createFromMaxDimension(64),
                irImageVector.toVectorXmlString(),
                errorLog,
            )
        } catch (_: Exception) {
            null
        }

        previewImage?.let {
            val panel = JPanel(BorderLayout()).apply {
                add(JLabel(ImageIcon(previewImage)), BorderLayout.CENTER)
                add(JLabel(name), BorderLayout.SOUTH)
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
            }

            val popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, null)
                .setTitle("Vector Icon Preview")
                .setResizable(false)
                .setMovable(false)
                .setRequestFocus(true)
                .createPopup()

            popup.show(RelativePoint(event))
        }
    }

    private fun parseImageVectorProperty(property: KtProperty): IrImageVector? {
        // Try parsing the file containing this property
        val containingFile = property.containingKtFile
        val fileParse = ImageVectorPsiParser.parseToIrImageVector(containingFile)
        if (fileParse != null) {
            return fileParse
        }

        // For properties from a library, we need to try to navigate to the actual source/decompiled file
        // navigationElement gives us access to the real source, not just the stub
        val navigationElement = property.navigationElement

        if (navigationElement is KtProperty && navigationElement != property) {
            val sourceFile = navigationElement.containingKtFile

            // Check if the file has actual parseable source (not just a stub)
            if (!sourceFile.text.contains("/* compiled code */")) {
                // Try parsing the source file
                val sourceParse = ImageVectorPsiParser.parseToIrImageVector(sourceFile)
                if (sourceParse != null) {
                    return sourceParse
                }
            }
        }
        return null
    }
}
