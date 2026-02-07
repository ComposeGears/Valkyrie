package io.github.composegears.valkyrie.util.extension

import com.intellij.openapi.application.edtWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import io.github.composegears.valkyrie.parser.unified.ext.isSvgExtension
import io.github.composegears.valkyrie.parser.unified.ext.isXmlExtension
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import java.nio.file.Path
import org.jetbrains.kotlin.psi.KtFile

inline val VirtualFile.isSvg: Boolean
    get() = extension?.isSvgExtension ?: false

inline val VirtualFile.isXml: Boolean
    get() = extension?.isXmlExtension ?: false

fun VirtualFile.resolveKtFile(project: Project): KtFile? {
    return PsiManager.getInstance(project).findFile(this).safeAs<KtFile>()
}

suspend fun Path.resolveKtFile(project: Project): KtFile? {
    return edtWriteAction {
        VfsUtil.findFile(this, true)?.resolveKtFile(project)
    }
}
