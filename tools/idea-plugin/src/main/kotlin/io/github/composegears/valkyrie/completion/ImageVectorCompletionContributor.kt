package io.github.composegears.valkyrie.completion

import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import javax.swing.Icon
import javax.swing.ImageIcon
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
                        ImageVectorPsiParser.parseToIrImageVector(element.containingKtFile)
                    } else {
                        null
                    }
                }
                ?.let {
                    val errorLog = StringBuilder()

                    val previewFromVectorXml = VdPreview.getPreviewFromVectorXml(
                        VdPreview.TargetSize.createFromMaxDimension(16),
                        it.toVectorXmlString(),
                        errorLog,
                    )

                    ComposeColorLookupElementDecorator(
                        lookupElement,
                        ImageIcon(previewFromVectorXml),
                    )
                }
                ?.let(completionResult::withLookupElement)
                ?: completionResult

            result.passResult(newResult)
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
