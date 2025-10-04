package io.github.composegears.valkyrie.completion

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import javax.swing.Icon
import javax.swing.ImageIcon
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

class ImageVectorCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(
        parameters: CompletionParameters,
        result: CompletionResultSet,
    ) {
        result.runRemainingContributors(parameters) { completionResult ->
            val lookupElement = completionResult.lookupElement

            val newResult = lookupElement
                .psiElement
                ?.navigationElement
                ?.safeAs<KtProperty>()
                ?.let { element ->
                    if (!element.isVar) {
                        getOrCreateCachedIcon(element.containingKtFile)
                    } else {
                        null
                    }
                }
                ?.let { icon ->
                    ComposeColorLookupElementDecorator(
                        delegate = lookupElement,
                        icon = icon,
                    )
                }
                ?.let(completionResult::withLookupElement)
                ?: completionResult

            result.passResult(newResult)
        }
    }

    private fun getOrCreateCachedIcon(ktFile: KtFile): Icon? {
        val cachedValuesManager = CachedValuesManager.getManager(ktFile.project)

        return cachedValuesManager.getCachedValue(ktFile) {
            val icon = createIconFromKtFile(ktFile)
            CachedValueProvider.Result.create(icon, ktFile)
        }
    }

    private fun createIconFromKtFile(ktFile: KtFile): Icon? {
        return try {
            val irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile) ?: return null
            val errorLog = StringBuilder()

            val previewFromVectorXml = VdPreview.getPreviewFromVectorXml(
                VdPreview.TargetSize.createFromMaxDimension(16),
                irImageVector.toVectorXmlString(),
                errorLog,
            )
            ImageIcon(previewFromVectorXml)
        } catch (e: Exception) {
            Logger
                .getInstance(ImageVectorCompletionContributor::class.java)
                .error("Failed to create icon preview: ${ktFile.name}, error: ${e.message}", e)
            null
        }
    }

    private class ComposeColorLookupElementDecorator(
        delegate: LookupElement,
        private val icon: Icon,
    ) : LookupElementDecorator<LookupElement>(delegate) {

        override fun renderElement(presentation: LookupElementPresentation) {
            super.renderElement(presentation)
            presentation.icon = icon
        }
    }
}
