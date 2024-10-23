package io.github.composegears.valkyrie.completion

import androidx.compose.ui.unit.dp
import com.android.ide.common.vectordrawable.VdPreview
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.parser.ktfile.util.toComposeImageVector
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
                        val icon = ImageVectorPsiParser.parseToIrImageVector(element)

                        icon?.toComposeImageVector(defaultHeight = 18.dp, defaultWidth = 18.dp)
                    } else {
                        null
                    }
                }
                ?.let {
                    val errorLog = StringBuilder()

                    val previewFromVectorXml = VdPreview.getPreviewFromVectorXml(
                        VdPreview.TargetSize.createFromMaxDimension(16),
                        testxml,
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

    val testxml = """
        <vector xmlns:android="http://schemas.android.com/apk/res/android"
            android:width="24dp"
            android:height="24dp"
            android:viewportWidth="24"
            android:viewportHeight="24">
          <path
              android:pathData="M12.413,3.565C11.97,2.675 10.647,2.704 10.243,3.613L3.411,18.958C2.959,19.974 4.01,21.005 5.016,20.533C6.869,19.664 10.429,17.88 11.614,17.618C12.942,17.767 16.565,19.644 18.494,20.503C19.522,20.961 20.559,19.878 20.056,18.871L12.413,3.565Z"
              android:fillColor="#2ca5d1"/>
        </vector>

    """.trimIndent()


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
