package io.github.composegears.valkyrie.sdk.intellij.psi.iconpack

import com.intellij.psi.PsiComment
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.core.tree.toMutableTree
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration

data class IconPackInfo(
    val packageName: String,
    val iconPackTree: IconPackTree,
    val license: String? = null,
)

object IconPackPsiParser {

    fun parse(ktFile: KtFile): IconPackInfo? {
        val topLevelObject = ktFile.declarations
            .filterIsInstance<KtObjectDeclaration>()
            .firstOrNull() ?: return null

        val iconPack = buildIconPack(topLevelObject)

        val license = ktFile.children
            .filterIsInstance<PsiComment>()
            .firstOrNull()
            ?.text

        return iconPack?.let {
            IconPackInfo(
                packageName = ktFile.packageFqName.asString(),
                iconPackTree = it,
                license = license,
            )
        }
    }

    private fun buildIconPack(objectDeclaration: KtObjectDeclaration): IconPackTree? {
        val name = objectDeclaration.name ?: return null

        val nestedObjects = objectDeclaration.body?.declarations
            ?.filterIsInstance<KtObjectDeclaration>()
            ?.mapNotNull { buildIconPack(it) }
            .orEmpty()

        return buildTree(name) {
            nestedObjects.forEach {
                child(it.toMutableTree())
            }
        }
    }
}
