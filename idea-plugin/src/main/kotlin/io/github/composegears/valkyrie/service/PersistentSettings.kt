package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.service.PersistentSettings.ValkyrieState
import io.github.composegears.valkyrie.ui.domain.model.Mode

@State(name = "Valkyrie.Settings", storages = [Storage("valkyrie_settings.xml")])
class PersistentSettings : SimplePersistentStateComponent<ValkyrieState>(ValkyrieState()) {

    private val listeners = mutableListOf<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        listeners += listener
    }

    private fun notifyListeners() = listeners.forEach { it() }

    fun stateWithNotify(action: ValkyrieState.() -> Unit) {
        state.action()
        notifyListeners()
    }

    class ValkyrieState : BaseState() {
        var mode: String? by string(Mode.Unspecified.name)
        var useMaterialPack: Boolean by property(false)

        var packageName: String? by string()
        var iconPackPackage: String? by string()
        var iconPackName: String? by string()
        var iconPackDestination: String? by string()

        var nestedPacks: String? by string()

        var generatePreview: Boolean by property(false)
        var outputFormat: String? by string()
        var flatPackage: Boolean by property(false)
        var useExplicitMode: Boolean by property(false)
        var addTrailingComma: Boolean by property(false)

        var showImageVectorPreview: Boolean by property(true)
        var indentSize: Int by property(4)
    }

    companion object {
        @JvmStatic
        val Project.persistentSettings: PersistentSettings
            get() = service<PersistentSettings>()
    }
}
