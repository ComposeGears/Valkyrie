package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

class ExistingPackInputHandler : BasicInputHandler(initialState = existingPackInputState) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = existingPackInputState

    companion object {
        private val existingPackInputState = InputFieldState(
            iconPackName = InputState(),
            packageName = InputState(),
            nestedPacks = emptyList(),
        )
    }
}
