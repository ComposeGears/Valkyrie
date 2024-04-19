package io.github.composegears.valkyrie.settings

import com.intellij.openapi.components.*
import io.github.composegears.valkyrie.settings.ValkyrieSettings.ValkyrieState

@Service
@State(name = "Valkyrie.Settings", storages = [Storage("valkyrie_settings.xml")])
class ValkyrieSettings : SimplePersistentStateComponent<ValkyrieState>(ValkyrieState()) {

    class ValkyrieState : BaseState() {
        var isFirstStart by property(true)
        var iconPackName by string("")
        var packageName by string("")
    }

    companion object {
        @JvmStatic
        val instance = service<ValkyrieSettings>().state
    }
}
