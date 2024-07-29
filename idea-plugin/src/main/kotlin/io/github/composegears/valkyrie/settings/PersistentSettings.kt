package io.github.composegears.valkyrie.settings

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import io.github.composegears.valkyrie.settings.PersistentSettings.ValkyrieState
import io.github.composegears.valkyrie.ui.domain.model.Mode

@Service
@State(name = "Valkyrie.Settings", storages = [Storage("valkyrie_settings.xml")])
class PersistentSettings : SimplePersistentStateComponent<ValkyrieState>(ValkyrieState()) {

    class ValkyrieState : BaseState() {
        var mode: String? by string(Mode.Unspecified.name)

        var packageName: String? by string()
        var iconPackName: String? by string()
        var iconPackDestination: String? by string()

        var nestedPacks: String? by string()

        var generatePreview: Boolean by property(false)
        var outputFormat: String? by string()
    }

    companion object {
        @JvmStatic
        val persistentSettings: ValkyrieState
            get() = service<PersistentSettings>().state
    }
}
