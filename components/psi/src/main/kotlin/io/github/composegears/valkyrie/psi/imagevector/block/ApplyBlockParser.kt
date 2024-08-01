package io.github.composegears.valkyrie.psi.imagevector.block

import io.github.composegears.valkyrie.psi.extension.childrenOfType
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression

fun KtBlockExpression.parseApplyBlock(): Any? {
    val applyBlock = childrenOfType<KtCallExpression>().firstOrNull {
        it.calleeExpression?.text == "apply"
    } ?: return null

    return applyBlock
}
