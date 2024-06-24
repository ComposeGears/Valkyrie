package io.github.composegears.valkyrie.ui.screen.settings

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings

class SettingsViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    val settings = inMemorySettings.settings

    fun updateGeneratePreview(generatePreview: Boolean) {
        inMemorySettings.updateGeneratePreview(generatePreview)
    }

    fun clearSettings() {
        inMemorySettings.clear()
    }
}