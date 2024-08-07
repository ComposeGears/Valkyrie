package io.github.composegears.valkyrie.settings

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.extension.or
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySettings {
    private val _settings = MutableStateFlow(value = PersistentSettings.persistentSettings.toValkyriesSettings())
    val settings = _settings.asStateFlow()

    var uiState: Map<String, Any?> = emptyMap()
        private set

    val current: ValkyriesSettings
        get() = settings.value

    fun updateGeneratePreview(generatePreview: Boolean) = updateSettings {
        PersistentSettings.persistentSettings.generatePreview = generatePreview
    }

    fun updateIconPackName(iconPackName: String) = updateSettings {
        PersistentSettings.persistentSettings.iconPackName = iconPackName
    }

    fun updateIconPackDestination(iconPackDestination: String) = updateSettings {
        PersistentSettings.persistentSettings.iconPackDestination = iconPackDestination
    }

    fun updateNestedPack(nestedPacks: List<String>) = updateSettings {
        if (nestedPacks.isEmpty()) {
            PersistentSettings.persistentSettings.nestedPacks = ""
        } else {
            PersistentSettings.persistentSettings.nestedPacks = nestedPacks.joinToString(separator = ",")
        }
    }

    fun updatePackageName(packageName: String) = updateSettings {
        PersistentSettings.persistentSettings.packageName = packageName
    }

    fun updateMode(mode: Mode) = updateSettings {
        PersistentSettings.persistentSettings.mode = mode.name
    }

    fun updateOutputFormat(outputFormat: OutputFormat) = updateSettings {
        PersistentSettings.persistentSettings.outputFormat = outputFormat.key
    }

    fun clear() = updateSettings {
        with(PersistentSettings.persistentSettings) {
            mode = Mode.Unspecified.name

            packageName = ""
            iconPackName = ""
            iconPackDestination = ""

            nestedPacks = ""

            outputFormat = OutputFormat.BackingProperty.key
            generatePreview = false
        }
    }

    private fun updateSettings(function: () -> Unit) {
        function()
        _settings.updateState { PersistentSettings.persistentSettings.toValkyriesSettings() }
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
)
