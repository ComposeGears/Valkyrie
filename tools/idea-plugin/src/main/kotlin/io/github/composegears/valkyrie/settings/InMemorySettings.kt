package io.github.composegears.valkyrie.settings

import com.composegears.tiamat.navigation.SavedState
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.service.PersistentSettings
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings
import io.github.composegears.valkyrie.shared.Mode
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.extension.or
import io.github.composegears.valkyrie.ui.extension.updateState
import java.util.Collections.emptyList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySettings(project: Project) {
    private val persistentSettings = project.persistentSettings

    init {
        persistentSettings.addListener {
            _settings.updateState { persistentSettings.state.toValkyriesSettings() }
        }
    }

    private val _settings = MutableStateFlow(persistentSettings.state.toValkyriesSettings())
    val settings = _settings.asStateFlow()

    var uiState: SavedState? = null
        private set

    val current: ValkyriesSettings
        get() = _settings.value

    fun update(action: PersistentSettings.ValkyrieState.() -> Unit) {
        action(persistentSettings.state)
        _settings.updateState { persistentSettings.state.toValkyriesSettings() }
    }

    fun clear() = update {
        mode = Mode.Unspecified
        previewType = PreviewType.Pixel
        useMaterialPack = false
        packageName = ""
        iconPackPackage = ""
        iconPackName = ""
        iconPackDestination = ""
        updateNestedPack(packs = emptyList())
        updateOutputFormat(OutputFormat.BackingProperty)
        useComposeColors = true
        generatePreview = false
        updatePreviewAnnotationType(PreviewAnnotationType.AndroidX)
        flatPackage = false
        useExplicitMode = false
        addTrailingComma = false
        showImageVectorPreview = true
        showIconsInProjectView = true
        indentSize = 4
    }

    fun updateUIState(uiState: SavedState) {
        this.uiState = uiState
    }

    private fun PersistentSettings.ValkyrieState.toValkyriesSettings(): ValkyriesSettings {
        return ValkyriesSettings(
            mode = mode,
            previewType = previewType,
            useMaterialPack = useMaterialPack,

            packageName = packageName.or("io.github.composegears.valkyrie"),
            iconPackName = iconPackName.or("ValkyrieIcons"),
            iconPackPackage = iconPackPackage.or(packageName.or("io.github.composegears.valkyrie")),
            iconPackDestination = iconPackDestination.or(""),

            nestedPacks = nestedPacks.orEmpty()
                .split(",")
                .filter { it.isNotEmpty() },

            generatePreview = generatePreview,
            previewAnnotationType = PreviewAnnotationType.from(previewAnnotationType),

            outputFormat = OutputFormat.from(outputFormat),
            useComposeColors = useComposeColors,
            indentSize = indentSize,
            flatPackage = flatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,

            showImageVectorPreview = showImageVectorPreview,
            showIconsInProjectView = showIconsInProjectView,
        )
    }
}

data class ValkyriesSettings(
    val mode: Mode,
    val previewType: PreviewType,
    val useMaterialPack: Boolean,

    val packageName: String,
    val iconPackName: String,
    val iconPackPackage: String,
    val iconPackDestination: String,

    val nestedPacks: List<String>,

    val generatePreview: Boolean,
    val previewAnnotationType: PreviewAnnotationType,

    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val indentSize: Int,
    val flatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,

    val showImageVectorPreview: Boolean,
    val showIconsInProjectView: Boolean,
)

fun PersistentSettings.ValkyrieState.updateNestedPack(packs: List<String>) {
    nestedPacks = when {
        packs.isEmpty() -> ""
        else -> packs.joinToString(separator = ",")
    }
}

fun PersistentSettings.ValkyrieState.updateOutputFormat(format: OutputFormat) {
    outputFormat = format.key
}

fun PersistentSettings.ValkyrieState.updatePreviewAnnotationType(type: PreviewAnnotationType) {
    previewAnnotationType = type.key
}
