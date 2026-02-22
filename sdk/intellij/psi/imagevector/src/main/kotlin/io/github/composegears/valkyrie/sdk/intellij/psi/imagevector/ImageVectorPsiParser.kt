package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector

import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.parser.MaterialImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.parser.RegularImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportList
import org.jetbrains.kotlin.psi.KtProperty

object ImageVectorPsiParser {

    fun parseToIrImageVector(ktFile: KtFile): IrImageVector? {
        val isMaterial = ktFile.importList?.isMaterial() ?: return null

        return when {
            isMaterial -> MaterialImageVectorPsiParser.parse(ktFile)
            else -> RegularImageVectorPsiParser.parse(ktFile)
        }
    }

    fun parseToIrImageVector(ktProperty: KtProperty): IrImageVector? {
        val ktFile = ktProperty.containingKtFile
        val isMaterial = ktFile.importList?.isMaterial() ?: return null

        return when {
            isMaterial -> MaterialImageVectorPsiParser.parse(ktProperty)
            else -> RegularImageVectorPsiParser.parse(ktProperty)
        }
    }

    private fun KtImportList.isMaterial(): Boolean {
        return imports.any {
            val fqName = it.importedFqName

            fqName?.asString() == "androidx.compose.material.icons.materialPath" ||
                fqName?.asString() == "androidx.compose.material.icons.materialIcon"
        }
    }
}
