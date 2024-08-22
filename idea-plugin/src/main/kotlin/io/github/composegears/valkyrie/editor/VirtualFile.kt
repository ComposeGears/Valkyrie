package io.github.composegears.valkyrie.editor

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFileFactory
import io.github.composegears.valkyrie.extensions.safeAs
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile

internal fun VirtualFile.toKtFile(project: Project): KtFile? {
    val fileContent = contentsToByteArray().toString(Charsets.UTF_8)

    return PsiFileFactory.getInstance(project)
        .createFileFromText(name, KotlinFileType.INSTANCE, fileContent)
        .safeAs<KtFile>()
}
