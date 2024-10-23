package io.github.composegears.valkyrie.action

import io.github.composegears.valkyrie.service.PersistentSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode

val PersistentSettings.ValkyrieState.isIconPackRequired: Boolean
    get() = mode == Mode.Unspecified.name || mode == Mode.Simple.name