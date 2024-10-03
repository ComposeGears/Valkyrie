package io.github.composegears.valkyrie.settings

import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.service.PersistentSettings
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.extension.or
import io.github.composegears.valkyrie.ui.extension.updateState
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

    var uiState: Map<String, Any?> = emptyMap()
        private set

    val current: ValkyriesSettings
        get() = _settings.value

    fun update(action: PersistentSettings.ValkyrieState.() -> Unit) {
        action(persistentSettings.state)
        _settings.updateState { persistentSettings.state.toValkyriesSettings() }
    }

    fun clear() = update {
        updateMode(Mode.Unspecified)
        packageName = ""
        iconPackName = ""
        iconPackDestination = ""
        updateNestedPack(packs = emptyList())
        updateOutputFormat(OutputFormat.BackingProperty)
        generatePreview = false
        flatPackage = false
        useExplicitMode = false
        addTrailingComma = false
        showImageVectorPreview = true
    }

    fun updateUIState(uiState: Map<String, Any?>) {
        this.uiState = uiState
    }

    private fun PersistentSettings.ValkyrieState.toValkyriesSettings() =
        ValkyriesSettings(
            mode = Mode.valueOf(mode!!),

            packageName = packageName.or("io.github.composegears.valkyrie"),
            iconPackName = iconPackName.or("ValkyrieIcons"),
            iconPackDestination = iconPackDestination.or(""),

            nestedPacks = nestedPacks.orEmpty()
                .split(",")
                .filter { it.isNotEmpty() },

            outputFormat = OutputFormat.from(outputFormat),
            generatePreview = generatePreview,
            flatPackage = flatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,

            showImageVectorPreview = showImageVectorPreview,
        )
}

data class ValkyriesSettings(
    val mode: Mode,

    val packageName: String,
    val iconPackName: String,
    val iconPackDestination: String,

    val nestedPacks: List<String>,

    val outputFormat: OutputFormat,
    val generatePreview: Boolean,
    val flatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,

    val showImageVectorPreview: Boolean,
)

fun PersistentSettings.ValkyrieState.updateNestedPack(packs: List<String>) {
    nestedPacks = when {
        packs.isEmpty() -> ""
        else -> packs.joinToString(separator = ",")
    }
}

fun PersistentSettings.ValkyrieState.updateMode(mode: Mode) {
    this.mode = mode.name
}

fun PersistentSettings.ValkyrieState.updateOutputFormat(format: OutputFormat) {
    outputFormat = format.key
}
