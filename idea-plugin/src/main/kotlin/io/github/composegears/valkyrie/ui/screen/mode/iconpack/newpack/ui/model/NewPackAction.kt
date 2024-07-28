package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import java.nio.file.Path

sealed interface NewPackAction {
    data class SelectDestinationFolder(val path: Path) : NewPackAction
    data object SaveDestination : NewPackAction

    data object AddNestedPack : NewPackAction
    data class RemoveNestedPack(val nestedPack: NestedPack) : NewPackAction

    data object PreviewPackObject : NewPackAction
    data object SavePack : NewPackAction
}
