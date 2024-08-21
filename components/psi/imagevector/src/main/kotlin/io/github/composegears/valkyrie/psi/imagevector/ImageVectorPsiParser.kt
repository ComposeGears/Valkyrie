package io.github.composegears.valkyrie.psi.imagevector

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.composegears.valkyrie.psi.imagevector.parser.MaterialImageVectorPsiParser
import io.github.composegears.valkyrie.psi.imagevector.parser.RegularImageVectorPsiParser
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportList

object ImageVectorPsiParser {

    fun parseToImageVector(ktFile: KtFile): ImageVector? {
        val isMaterial = ktFile.importList?.isMaterial() ?: return null

        return when {
            isMaterial -> MaterialImageVectorPsiParser.parse(ktFile)
            else -> RegularImageVectorPsiParser.parse(ktFile)
        }
    }

    private fun KtImportList.isMaterial(): Boolean {
        return imports.any {
            val fqName = it.importedFqName

            fqName?.asString() == "androidx.compose.material.icons.materialPath" ||
                fqName?.asString() == "androidx.compose.material.icons.materialFilled"
        }
    }
}
