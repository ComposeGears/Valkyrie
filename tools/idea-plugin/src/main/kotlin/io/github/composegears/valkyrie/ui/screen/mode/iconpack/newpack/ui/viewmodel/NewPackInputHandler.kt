package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel

import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

class NewPackInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(initialState = settings.newPackInputFieldState) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = settings.newPackInputFieldState

    companion object {
        private val ValkyriesSettings.newPackInputFieldState: InputFieldState
            get() = InputFieldState(
                iconPackName = InputState(),
                packageName = InputState(
                    text = PackageExtractor.getFrom(path = iconPackDestination).orEmpty(),
                ),
                nestedPacks = emptyList(),
            )
    }
}
