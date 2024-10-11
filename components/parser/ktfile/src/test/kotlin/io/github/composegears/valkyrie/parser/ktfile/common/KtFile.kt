package io.github.composegears.valkyrie.parser.ktfile.common

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import io.github.composegears.valkyrie.extensions.cast
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile

fun Project.createKtFile(from: String): KtFile {
    return PsiManager.getInstance(this)
        .findFile(LightVirtualFile("", KotlinFileType.INSTANCE, getResourceText(from)))
        .cast<KtFile>()
}
