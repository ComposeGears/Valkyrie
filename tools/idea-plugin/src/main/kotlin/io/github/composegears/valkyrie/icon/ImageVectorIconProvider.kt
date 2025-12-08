package io.github.composegears.valkyrie.icon

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import io.github.composegears.valkyrie.util.getOrCreateCachedIcon
import io.github.composegears.valkyrie.util.hasImageVectorProperties
import javax.swing.Icon
import org.jetbrains.kotlin.psi.KtFile

class ImageVectorIconProvider :
    IconProvider(),
    DumbAware {

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val ktFile = when (element) {
            is KtFile -> element
            is PsiFile -> null
            else -> element.containingFile as? KtFile
        } ?: return null

        if (!ktFile.hasImageVectorProperties()) {
            return null
        }

        return ktFile.getOrCreateCachedIcon()
    }
}
