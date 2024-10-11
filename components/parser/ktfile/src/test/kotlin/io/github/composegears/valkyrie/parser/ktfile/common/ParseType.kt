package io.github.composegears.valkyrie.parser.ktfile.common

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtFile

enum class ParseType {
    Lazy,
    Backing,
}

fun ParseType.toKtFile(
    project: Project,
    pathToLazy: String,
    pathToBacking: String,
): KtFile = when (this) {
    ParseType.Lazy -> project.createKtFile(pathToLazy)
    ParseType.Backing -> project.createKtFile(pathToBacking)
}
