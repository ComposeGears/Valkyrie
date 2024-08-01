package io.github.composegears.valkyrie.psi.imagevector

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.psi.extension.childOfType
import io.github.composegears.valkyrie.psi.extension.childrenOfType
import io.github.composegears.valkyrie.psi.imagevector.block.parseApplyBlock
import io.github.composegears.valkyrie.psi.imagevector.block.parseImageVectorParams
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtProperty

object ImageVectorPsiParser {

    fun parseToImageVector(ktFile: KtFile): ImageVector? {
        val imports = ktFile.childOfType<KtImportList>()
            ?.childrenOfType<KtImportDirective>()
            ?.mapNotNull { it.importedFqName?.asString() }

        // TODO: add logic to check if imports contain androidx.compose.ui.graphics.vector.ImageVector

        val property = ktFile.childOfType<KtProperty>() ?: return null
        val blockBody = property.getter?.bodyBlockExpression ?: return null

        val ktImageVector = blockBody.parseImageVectorParams() ?: return null
        val path = blockBody.parseApplyBlock()

        return ImageVector.Builder(
            name = ktImageVector.name.ifEmpty { property.name.orEmpty() },
            defaultWidth = ktImageVector.defaultWidth.dp,
            defaultHeight = ktImageVector.defaultHeight.dp,
            viewportWidth = ktImageVector.viewportWidth,
            viewportHeight = ktImageVector.viewportHeight,
        ).apply {
            // TODO: add path
        }.build()
    }
}
