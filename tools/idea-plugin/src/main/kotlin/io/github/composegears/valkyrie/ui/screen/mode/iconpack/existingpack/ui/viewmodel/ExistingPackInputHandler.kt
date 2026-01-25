package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.viewmodel

import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

class ExistingPackInputHandler : BasicInputHandler(initialState = existingPackInputFieldState) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = existingPackInputFieldState

    companion object {
        private val existingPackInputFieldState: InputFieldState
            get() = InputFieldState(
                iconPackName = InputState(),
                packageName = InputState(),
                nestedPacks = emptyList(),
            )
    }
}
