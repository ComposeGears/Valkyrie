package io.github.composegears.valkyrie.ui.screen.conversion

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.ValkyrieSettings
import io.github.composegears.valkyrie.ui.screen.intro.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConversionViewModel : TiamatViewModel() {
    private val settingsService = ValkyrieSettings.instance

    private val _state = MutableStateFlow(ConversionState())
    val state = _state.asStateFlow()

    init {
        _state.updateState {
            copy(initialDirectory = settingsService.lastChoosePath ?: System.getProperty("user.home"))
        }
    }

    fun updateLastChoosePath(path: String) {
        println(path)
        settingsService.lastChoosePath = path
        _state.updateState { copy(initialDirectory = path) }
    }
}