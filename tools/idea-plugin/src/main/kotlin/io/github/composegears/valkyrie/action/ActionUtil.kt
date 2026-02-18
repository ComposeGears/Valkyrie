package io.github.composegears.valkyrie.action

import io.github.composegears.valkyrie.sdk.shared.ValkyrieMode
import io.github.composegears.valkyrie.service.PersistentSettings

val PersistentSettings.ValkyrieState.isIconPackRequired: Boolean
    get() = mode == ValkyrieMode.Unspecified || mode == ValkyrieMode.Simple
