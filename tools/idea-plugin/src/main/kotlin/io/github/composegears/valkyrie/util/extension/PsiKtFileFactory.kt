package io.github.composegears.valkyrie.util.extension

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile

object PsiKtFileFactory {

    /**
     * Creates a temporary [KtFile] from the given text.
     *
     * This method is thread-safe and can be called from any thread (EDT, background, etc.).
     * It creates an in-memory PSI structure without modifying the project or filesystem.
     *
     * Reading PSI from the created file (e.g., analyzing structure, traversing elements) is also
     * thread-safe and can be done from any thread. Only write operations to real project PSI
     * require write-safe context.
     */
    fun createFromText(project: Project, name: String, text: String): KtFile? {
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, KotlinFileType.INSTANCE, text)
            .safeAs<KtFile>()
    }
}
