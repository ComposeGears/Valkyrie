package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model

import com.intellij.openapi.project.Project
import java.nio.file.Path

sealed interface MaterialPackAction {
    data class SelectDestinationFolder(val path: Path) : MaterialPackAction
    data class UpdateFlatPackageStructure(val value: Boolean) : MaterialPackAction
    data object SaveDestination : MaterialPackAction

    data class SavePack(val project: Project) : MaterialPackAction
}
