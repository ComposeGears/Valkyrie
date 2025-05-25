package io.github.composegears.valkyrie.action

import io.github.composegears.valkyrie.service.PersistentSettings
import io.github.composegears.valkyrie.shared.Mode

val PersistentSettings.ValkyrieState.isIconPackRequired: Boolean
    get() = mode == Mode.Unspecified || mode == Mode.Simple
