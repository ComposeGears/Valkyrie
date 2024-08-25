package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel

import io.github.composegears.valkyrie.parser.svgxml.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack

class NewPackInputHandler(
    settings: ValkyriesSettings,
) : BasicInputHandler(
    InputFieldState(
        iconPackName = InputState(text = settings.iconPackName),
        packageName = InputState(
            text = PackageExtractor.getFrom(path = settings.iconPackDestination) ?: settings.packageName,
        ),
        nestedPacks = settings.nestedPacks.mapIndexed { index, nestedPack ->
            NestedPack(
                id = index.toString(),
                inputFieldState = InputState(text = nestedPack),
            )
        },
    ),
)
