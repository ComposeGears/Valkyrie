package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model

import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import java.nio.file.Path

sealed interface ExistingPackAction {
    data class SelectKotlinFile(val path: Path, val project: Project) : ExistingPackAction

    data object AddNestedPack : ExistingPackAction
    data class RemoveNestedPack(val nestedPack: NestedPack) : ExistingPackAction

    data object PreviewPackObject : ExistingPackAction
    data object SavePack : ExistingPackAction
}
