package io.github.composegears.valkyrie.ui.screen.conversion

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.parser.ParserConfig
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import java.io.File

class ConversionViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val _state = MutableStateFlow(ConversionState())
    val state = _state.asStateFlow()

    val valkyriesSettings = inMemorySettings.settings

    init {
        _state
            .combine(inMemorySettings.settings) { state, settings ->
                if (state.lastFile != null) {
                    updateIcon(state.lastFile, settings)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateIcon(file: File, valkyriesSettings: ValkyriesSettings) {
        val icon = IconParser.tryParse(
            file = file,
            config = ParserConfig(
                packPackage = valkyriesSettings.packageName,
                packName = valkyriesSettings.iconPackName,
                nestedPackName = valkyriesSettings.currentNestedPack,
                generatePreview = valkyriesSettings.generatePreview
            )
        )
        _state.updateState { copy(iconContent = icon) }
    }

    fun selectFile(file: File) {
        _state.updateState { copy(lastFile = file) }
    }

    fun reset() {
        _state.updateState { copy(iconContent = null, lastFile = null) }
    }

    fun updateLastChoosePath(file: File) {
        inMemorySettings.updateInitialDirectory(file.parentFile.path)
    }

    fun selectNestedPack(nestedPack: String) {
        inMemorySettings.updateCurrentNestedPack(nestedPack)
    }
}