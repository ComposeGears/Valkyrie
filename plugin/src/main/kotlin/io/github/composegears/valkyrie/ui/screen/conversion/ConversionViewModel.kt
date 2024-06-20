package io.github.composegears.valkyrie.ui.screen.conversion

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.parser.Config
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.settings.ValkyrieSettings
import io.github.composegears.valkyrie.ui.screen.intro.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class ConversionViewModel : TiamatViewModel() {
    private val settingsService = ValkyrieSettings.instance

    private val _state = MutableStateFlow(ConversionState())
    val state = _state.asStateFlow()

    init {
        _state.updateState {
            copy(
                initialDirectory = settingsService.lastChoosePath ?: System.getProperty("user.home"),
                config = Config(
                    packName = settingsService.iconPackName!!,
                    packPackage = settingsService.packageName!!
                )
            )
        }
    }

    fun parseIcon(file: File) {
        val icon = IconParser.tryParse(file, state.value.config!!)
        _state.updateState { copy(iconContent = icon) }
    }

    fun resetIconContent() {
        _state.updateState { copy(iconContent = null) }
    }

    fun updateLastChoosePath(file: File) {
        val lastPath = file.parentFile.path

        settingsService.lastChoosePath = lastPath
        _state.updateState { copy(initialDirectory = lastPath) }
    }
}