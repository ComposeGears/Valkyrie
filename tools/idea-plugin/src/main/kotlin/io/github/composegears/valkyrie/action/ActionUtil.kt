package io.github.composegears.valkyrie.action

import io.github.composegears.valkyrie.service.PersistentSettings
import io.github.composegears.valkyrie.shared.ValkyrieMode

val PersistentSettings.ValkyrieState.isIconPackRequired: Boolean
    get() = mode == ValkyrieMode.Unspecified || mode == ValkyrieMode.Simple
