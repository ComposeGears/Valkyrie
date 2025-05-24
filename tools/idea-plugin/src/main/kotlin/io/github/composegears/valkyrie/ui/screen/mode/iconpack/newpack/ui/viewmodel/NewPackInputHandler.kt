package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel

import io.github.composegears.valkyrie.parser.svgxml.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack

class NewPackInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(initialState = settings.newPackInputFieldState) {

    override fun provideInputFieldState(
        settings: ValkyriesSettings,
    ): InputFieldState = settings.newPackInputFieldState

    companion object {
        private val ValkyriesSettings.newPackInputFieldState: InputFieldState
            get() {
                return if (useMaterialPack) {
                    InputFieldState(
                        packageName = InputState(
                            text = PackageExtractor.getFrom(path = iconPackDestination) ?: packageName,
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
                        allowAddNestedPack = false,
                    )
                } else {
                    InputFieldState(
                        iconPackName = InputState(text = iconPackName),
                        packageName = InputState(
                            text = PackageExtractor.getFrom(path = iconPackDestination) ?: packageName,
                        ),
                        nestedPacks = nestedPacks.mapIndexed { index, nestedPack ->
                            NestedPack(
                                id = index.toString(),
                                inputFieldState = InputState(text = nestedPack),
                            )
                        },
                    )
                }
            }
    }
}
