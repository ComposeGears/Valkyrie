package io.github.composegears.valkyrie.icon

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings
import io.github.composegears.valkyrie.util.getOrCreateCachedIcon
import io.github.composegears.valkyrie.util.getOrCreateGutterIcon
import io.github.composegears.valkyrie.util.hasImageVectorProperties
import io.github.composegears.valkyrie.util.isImageVector
import javax.swing.Icon
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty

class ImageVectorIconProvider :
    IconProvider(),
    DumbAware {

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val project = element.project
        if (project.isDisposed) return null

        if (!project.persistentSettings.state.showIconsInProjectView) return null

        if (element is KtProperty && element.isImageVector()) {
            return element.getOrCreateGutterIcon()
        }

        val ktFile = when (element) {
            is KtFile -> element
            is PsiFile -> null
            else -> element.containingFile as? KtFile
        } ?: return null

        return when {
            ktFile.hasImageVectorProperties() -> ktFile.getOrCreateCachedIcon()
            else -> null
        }
    }
}
