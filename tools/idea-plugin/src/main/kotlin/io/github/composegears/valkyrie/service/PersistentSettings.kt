package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.jewel.tooling.GlobalPreviewState
import io.github.composegears.valkyrie.service.PersistentSettings.ValkyrieState
import io.github.composegears.valkyrie.shared.ValkyrieMode
import io.github.composegears.valkyrie.ui.domain.model.PreviewType

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
        var mode: ValkyrieMode by enum(ValkyrieMode.Unspecified)
        var previewType by enum(PreviewType.Pixel)

        var packageName: String? by string()
        var iconPackPackage: String? by string()
        var iconPackName: String? by string()
        var iconPackDestination: String? by string()

        var nestedPacks: String? by string()

        var generatePreview: Boolean by property(false)

        var outputFormat: String? by string()
        var useComposeColors: Boolean by property(true)
        var flatPackage: Boolean by property(false)
        var useExplicitMode: Boolean by property(false)
        var addTrailingComma: Boolean by property(false)
        var usePathDataString: Boolean by property(false)

        var showImageVectorPreview: Boolean by property(true)
        var showIconsInProjectView: Boolean by property(true)
        var indentSize: Int by property(4)
    }

    companion object {
        @JvmStatic
        val Project.persistentSettings: PersistentSettings
            get() {
                return if (GlobalPreviewState.isPreview) {
                    PersistentSettings()
                } else {
                    service<PersistentSettings>()
                }
            }
    }
}
