package io.github.composegears.valkyrie.sdk.intellij.psi.iconpack

import com.intellij.psi.PsiComment
import io.github.composegears.valkyrie.generator.core.IconPack
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration

data class IconPackInfo(
    val packageName: String,
    val iconPack: IconPack,
    val license: String? = null,
)

object IconPackPsiParser {

    fun parse(ktFile: KtFile): IconPackInfo? {
        val topLevelObject = ktFile.declarations
            .filterIsInstance<KtObjectDeclaration>()
            .firstOrNull() ?: return null

        val iconPack = buildIconPack(topLevelObject)

        val license = ktFile.children
            .takeWhile { it is PsiComment }
            .filterIsInstance<PsiComment>()
            .joinToString("\n") { it.text }
            .takeIf { it.isNotEmpty() }

        return iconPack?.let {
            IconPackInfo(
                packageName = ktFile.packageFqName.asString(),
                iconPack = it,
                license = license,
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
}