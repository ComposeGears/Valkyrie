package io.github.composegears.valkyrie.settings

import com.intellij.openapi.components.*
import io.github.composegears.valkyrie.settings.PersistentSettings.ValkyrieState

@Service
@State(name = "Valkyrie.Settings", storages = [Storage("valkyrie_settings.xml")])
class PersistentSettings : SimplePersistentStateComponent<ValkyrieState>(ValkyrieState()) {

    class ValkyrieState : BaseState() {
        var isFirstLaunch by property(true)
        var iconPackName: String? by string("")
        var packageName by string("")
        var initialDirectory by string("")
        var generatePreview by property(false)
    }

    companion object {
        @JvmStatic
        val persistentSettings = service<PersistentSettings>().state
    }
}