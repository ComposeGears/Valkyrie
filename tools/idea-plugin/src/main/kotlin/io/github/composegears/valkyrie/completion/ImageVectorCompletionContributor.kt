package io.github.composegears.valkyrie.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.util.getOrCreateCachedIcon
import javax.swing.Icon
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
                        element.containingKtFile.getOrCreateCachedIcon()
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
