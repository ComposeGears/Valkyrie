package io.github.composegears.valkyrie.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.util.createImageVectorIcon
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
            val icon = ktFile.createImageVectorIcon()
            CachedValueProvider.Result.create(icon, ktFile)
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
