package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

class NewPackInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(initialState = settings.newPackInputState) {

    override fun invalidateInputFieldState(
        currentState: InputFieldState,
        settings: ValkyriesSettings,
    ): InputFieldState {
        // Preserve user's input, only update packageName if empty
        val packageNameText = currentState.packageName.text.ifEmpty {
            PackageExtractor.getFrom(path = settings.iconPackDestination).orEmpty()
        }

        return currentState.copy(packageName = currentState.packageName.copy(text = packageNameText))
    }

    companion object {
        private val ValkyriesSettings.newPackInputState: InputFieldState
            get() = InputFieldState(
                iconPackName = InputState(),
                packageName = InputState(
                    text = PackageExtractor.getFrom(path = iconPackDestination).orEmpty(),
                ),
                nestedPacks = emptyList(),
            )
    }
}
