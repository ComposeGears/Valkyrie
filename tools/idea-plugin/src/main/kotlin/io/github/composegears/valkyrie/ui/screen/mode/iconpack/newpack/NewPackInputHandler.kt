package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

class NewPackInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(initialState = settings.newPackInputState) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = settings.newPackInputState

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
