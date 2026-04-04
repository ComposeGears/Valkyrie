package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model

import androidx.compose.runtime.Stable

@Stable
data class InputFieldState(
    val license: InputState,
    val iconPackName: InputState,
    val packageName: InputState,
    val nestedPacks: List<NestedPack>,
    val iconPackPackage: InputState = packageName,
    val allowEditing: Boolean = true,
) {
    val isValid: Boolean = iconPackName.isValid() &&
        packageName.isValid() &&
        nestedPacks.all { it.inputFieldState.isValid() } &&
        !hasDuplicateNestedPacks()

    private fun InputState.isValid() = text.isNotEmpty() && isValid

    private fun hasDuplicateNestedPacks(): Boolean {
        val packs = nestedPacks.map { it.inputFieldState.text }
        return packs.size != packs.distinct().size
    }

    companion object {
        val Empty = InputFieldState(
            license = InputState(),
            iconPackName = InputState(),
            packageName = InputState(),
            nestedPacks = emptyList(),
        )
    }
}

data class NestedPack(
    val id: String,
    val inputFieldState: InputState,
)
