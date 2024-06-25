package io.github.composegears.valkyrie.settings

import io.github.composegears.valkyrie.ui.extension.or
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.intro.Mode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySettings {
    private val _settings = MutableStateFlow(value = PersistentSettings.persistentSettings.toValkyriesSettings())
    val settings = _settings.asStateFlow()

    fun updateGeneratePreview(generatePreview: Boolean) = updateSettings {
        PersistentSettings.persistentSettings.generatePreview = generatePreview
    }

    fun updateInitialDirectory(initialDirectory: String) = updateSettings {
        PersistentSettings.persistentSettings.initialDirectory = initialDirectory
    }

    fun updateIconPackName(iconPackName: String) = updateSettings {
        PersistentSettings.persistentSettings.iconPackName = iconPackName
    }

    fun updateNestedPack(nestedPacks: List<String>) = updateSettings {
        PersistentSettings.persistentSettings.nestedPacks = nestedPacks.joinToString(separator = ",")
        PersistentSettings.persistentSettings.currentNestedPack = nestedPacks.first()
    }

    fun updateCurrentNestedPack(currentNestedPack: String) = updateSettings {
        PersistentSettings.persistentSettings.currentNestedPack = currentNestedPack
    }

    fun updatePackageName(packageName: String) = updateSettings {
        PersistentSettings.persistentSettings.packageName = packageName
    }

    fun updateMode(mode: Mode) = updateSettings {
        PersistentSettings.persistentSettings.mode = mode.name
    }

    fun clear() = updateSettings {
        with(PersistentSettings.persistentSettings) {
            mode = Mode.Unspecified.name

            packageName = ""
            iconPackName = ""

            nestedPacks = ""
            currentNestedPack = ""

            generatePreview = false

            initialDirectory = ""
        }
    }

    private fun updateSettings(function: () -> Unit) {
        function()
        _settings.updateState { PersistentSettings.persistentSettings.toValkyriesSettings() }
    }

    private fun PersistentSettings.ValkyrieState.toValkyriesSettings() =
        ValkyriesSettings(
            mode = Mode.valueOf(mode!!),

            packageName = packageName.or("io.github.composegears.valkyrie"),
            iconPackName = iconPackName.or("ValkyrieIcons"),

            nestedPacks = nestedPacks.orEmpty()
                .split(",")
                .filter { it.isNotEmpty() },
            currentNestedPack = currentNestedPack.orEmpty(),

            generatePreview = generatePreview,

            initialDirectory = initialDirectory ?: System.getProperty("user.home"),
        )
}

data class ValkyriesSettings(
    val mode: Mode,

    val packageName: String,
    val iconPackName: String,

    val nestedPacks: List<String>,
    val currentNestedPack: String,

    val generatePreview: Boolean,

    val initialDirectory: String,
)
