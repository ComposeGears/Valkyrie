package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material

import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack

class MaterialInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(initialState = settings.materialInputState) {

    override fun invalidateInputFieldState(
        currentState: InputFieldState,
        settings: ValkyriesSettings,
    ): InputFieldState {
        // Preserve user's packageName input, only update if it's empty (initial state)
        val packageNameText = currentState.packageName.text.ifEmpty {
            PackageExtractor.getFrom(path = settings.iconPackDestination).orEmpty()
        }

        return currentState.copy(packageName = currentState.packageName.copy(text = packageNameText))
    }

    companion object {
        private val ValkyriesSettings.materialInputState: InputFieldState
            get() = InputFieldState(
                packageName = InputState(
                    text = PackageExtractor.getFrom(path = iconPackDestination).orEmpty(),
                ),
                iconPackPackage = InputState(text = "androidx.compose.material.icons", enabled = false),
                iconPackName = InputState(text = "Icons", enabled = false),
                nestedPacks = listOf(
                    NestedPack(id = "0", inputFieldState = InputState(text = "Filled", enabled = false)),
                    NestedPack(id = "1", inputFieldState = InputState(text = "Outlined", enabled = false)),
                    NestedPack(id = "2", inputFieldState = InputState(text = "Rounded", enabled = false)),
                    NestedPack(id = "3", inputFieldState = InputState(text = "TwoTone", enabled = false)),
                    NestedPack(id = "4", inputFieldState = InputState(text = "Sharp", enabled = false)),
                ),
                allowEditing = false,
            )
    }
}
