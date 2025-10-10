package io.github.composegears.valkyrie.psi.iconpack

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

data class IconPackInfo(
    val packageName: String,
    val iconPack: String,
    val nestedPacks: List<String>,
)

object IconPackPsiParser {

    fun extractIconPack(path: Path, project: Project): IconPackInfo? {
        val ktFile = PsiManager.getInstance(project)
            .findFile(LightVirtualFile(path.name, KotlinFileType.INSTANCE, path.readText()))
            .safeAs<KtFile>() ?: return null

        var iconPackName: String? = null
        val nestedPacks = mutableListOf<String>()

        ktFile.accept(
            object : KtTreeVisitorVoid() {
                override fun visitObjectDeclaration(declaration: KtObjectDeclaration) {
                    super.visitObjectDeclaration(declaration)

                    if (declaration.isTopLevel()) {
                        iconPackName = declaration.name
                    } else {
                        declaration.name?.let {
                            nestedPacks.add(it)
                        }
                    }
                }
            },
        )

        return when {
            iconPackName != null -> {
                IconPackInfo(
                    packageName = ktFile.packageFqName.asString(),
                    iconPack = iconPackName!!,
                    nestedPacks = nestedPacks,
                )
            }
            else -> null
        }
    }
}
