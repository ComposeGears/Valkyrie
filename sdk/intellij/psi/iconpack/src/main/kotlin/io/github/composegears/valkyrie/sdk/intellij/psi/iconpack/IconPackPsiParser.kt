package io.github.composegears.valkyrie.sdk.intellij.psi.iconpack

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration

data class IconPackInfo(
    val packageName: String,
    val iconPack: IconPack,
)

object IconPackPsiParser {

    fun parse(ktFile: KtFile): IconPackInfo? {
        val topLevelObject = ktFile.declarations
            .filterIsInstance<KtObjectDeclaration>()
            .firstOrNull() ?: return null

        val iconPack = buildIconPack(topLevelObject)

        return iconPack?.let {
            IconPackInfo(
                packageName = ktFile.packageFqName.asString(),
                iconPack = it,
            )
        }
    }

    private fun buildIconPack(objectDeclaration: KtObjectDeclaration): IconPack? {
        val name = objectDeclaration.name ?: return null

        val nestedObjects = objectDeclaration.body?.declarations
            ?.filterIsInstance<KtObjectDeclaration>()
            ?.mapNotNull { buildIconPack(it) }
            .orEmpty()

        return IconPack(
            name = name,
            nested = nestedObjects,
        )
    }

    @Deprecated("Use ktFile version instead")
    fun extractIconPack(path: Path, project: Project): IconPackInfo? {
        val ktFile = PsiManager.getInstance(project)
            .findFile(LightVirtualFile(path.name, KotlinFileType.INSTANCE, path.readText()))
            .safeAs<KtFile>() ?: return null

        return parse(ktFile)
    }
}
