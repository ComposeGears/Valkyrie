package io.github.composegears.valkyrie.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import io.github.composegears.valkyrie.ir.aspectRatio
import io.github.composegears.valkyrie.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import javax.swing.Icon
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
        val irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)
        val vectorXml = irImageVector?.toVectorXmlString() ?: return null

        return ImageVectorIcon(
            vectorXml = vectorXml,
            aspectRatio = irImageVector.aspectRatio,
        )
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
