package io.github.composegears.valkyrie.settings

import io.github.composegears.valkyrie.ui.screen.intro.updateState
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

    fun updatePackageName(packageName: String) = updateSettings {
        PersistentSettings.persistentSettings.packageName = packageName
    }

    fun updateFirstLaunch(isFirstLaunch: Boolean) = updateSettings {
        PersistentSettings.persistentSettings.isFirstLaunch = isFirstLaunch
    }

    fun clear() = updateSettings {
        with(PersistentSettings.persistentSettings) {
            isFirstLaunch = true
            iconPackName = ""
            packageName = ""
            initialDirectory = ""
            generatePreview = false
        }
    }

    private fun updateSettings(function: () -> Unit) {
        function()
        _settings.updateState { PersistentSettings.persistentSettings.toValkyriesSettings() }
    }

    private fun PersistentSettings.ValkyrieState.toValkyriesSettings() =
        ValkyriesSettings(
            iconPackName = iconPackName.orEmpty(),
            packageName = packageName.orEmpty(),
            generatePreview = generatePreview,

            isFirstLaunch = isFirstLaunch,
            initialDirectory = initialDirectory ?: System.getProperty("user.home"),
        )
}

data class ValkyriesSettings(
    val iconPackName: String,
    val packageName: String,
    val generatePreview: Boolean,
    val initialDirectory: String,
    val isFirstLaunch: Boolean
)
