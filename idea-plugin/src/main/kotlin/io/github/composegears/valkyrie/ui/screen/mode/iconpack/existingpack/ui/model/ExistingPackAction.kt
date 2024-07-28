package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import java.nio.file.Path

sealed interface ExistingPackAction {
    data class SelectKotlinFile(val path: Path) : ExistingPackAction

    data object AddNestedPack : ExistingPackAction
    data class RemoveNestedPack(val nestedPack: NestedPack) : ExistingPackAction

    data object PreviewPackObject : ExistingPackAction
    data object SavePack : ExistingPackAction
}
