package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model

data class PackEditState(
    val inputFieldState: InputFieldState = InputFieldState(
        iconPackName = InputState(),
        packageName = InputState(),
        nestedPacks = emptyList(),
    ),
)

data class InputFieldState(
    val iconPackName: InputState,
    val packageName: InputState,
    val nestedPacks: List<NestedPack>,
    val iconPackPackage: InputState = packageName,
    val allowAddNestedPack: Boolean = true,
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
}

data class NestedPack(
    val id: String,
    val inputFieldState: InputState,
)
