package io.github.composegears.valkyrie.service

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.jewel.tooling.GlobalPreviewState
import io.github.composegears.valkyrie.sdk.shared.ValkyrieMode
import io.github.composegears.valkyrie.service.PersistentSettings.ValkyrieState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings.Companion.DEFAULT_FILL
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings.Companion.DEFAULT_GRADE
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings.Companion.DEFAULT_OPTICAL_SIZE
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings.Companion.DEFAULT_WEIGHT
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings.Companion.DEFAULT_SIZE

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

        // MaterialSymbols
        var materialFontFill: Boolean by property(DEFAULT_FILL)
        var materialFontWeight: Int by property(DEFAULT_WEIGHT)
        var materialFontGrade: Int by property(DEFAULT_GRADE)
        var materialFontOpticalSize: Float by property(DEFAULT_OPTICAL_SIZE)

        // Lucide
        var lucideSize: Int by property(DEFAULT_SIZE)

        // Bootstrap
        var bootstrapSize: Int by property(DEFAULT_SIZE)

        // Remix
        var remixSize: Int by property(DEFAULT_SIZE)
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
