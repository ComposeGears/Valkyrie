package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState

class ExistingPackInputHandler : BasicInputHandler(initialState = InputFieldState.Empty) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = InputFieldState.Empty
}
