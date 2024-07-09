package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import java.io.File

class SimpleConversionViewModel(inMemorySettings: InMemorySettings) : TiamatViewModel() {

    private val _state = MutableStateFlow(SimpleConversionState())
    val state = _state.asStateFlow()

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
        val output = runCatching {
            val parserOutput = IconParser.toVector(file)
            ImageVectorGenerator.convert(
                vector = parserOutput.vector,
                kotlinName = parserOutput.kotlinName,
                config = ImageVectorGeneratorConfig(
                    packageName = valkyriesSettings.packageName,
                    packName = "",
                    nestedPackName = "",
                    generatePreview = valkyriesSettings.generatePreview
                )
            ).content
        }.getOrElse {
            it.message.orEmpty()
        }

        _state.updateState { copy(iconContent = output) }
    }

    fun selectFile(file: File) {
        _state.updateState { copy(lastFile = file) }
    }

    fun reset() {
        _state.updateState { copy(iconContent = null, lastFile = null) }
    }
}