package io.github.composegears.valkyrie.psi.extension

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

inline fun <reified T : PsiElement> PsiElement.childrenOfType(): Collection<T> =
    PsiTreeUtil.findChildrenOfType(this, T::class.java)

inline fun <reified T : PsiElement> PsiElement.childOfType(): T? =
    PsiTreeUtil.findChildOfType(this, T::class.java)
