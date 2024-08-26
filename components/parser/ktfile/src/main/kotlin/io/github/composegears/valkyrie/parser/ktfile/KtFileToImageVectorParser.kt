package io.github.composegears.valkyrie.parser.ktfile

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import org.jetbrains.kotlin.psi.KtFile

object KtFileToImageVectorParser {

    fun parse(ktFile: KtFile): ImageVector? {
        return ImageVectorPsiParser.parseToIrImageVector(ktFile)
            ?.toComposeImageVector()
    }
}
