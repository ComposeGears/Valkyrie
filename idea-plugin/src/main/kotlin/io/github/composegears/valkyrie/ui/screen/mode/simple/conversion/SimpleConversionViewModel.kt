package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import com.composegears.tiamat.Saveable
import com.composegears.tiamat.SavedState
import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.util.getOrNull
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class SimpleConversionViewModel(
    private val inMemorySettings: InMemorySettings,
    private val savedState: SavedState,
) : TiamatViewModel(),
    Saveable {

    private val _state = MutableStateFlow(initialState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    init {
        _state
            .combine(inMemorySettings.settings) { state, settings ->
                if (state.lastPath != null) {
                    updateIcon(state.lastPath, settings)
                }
            }
            .launchIn(viewModelScope)
    }

    override fun saveToSaveState(): SavedState {
        return mapOf("last_path" to _state.value.lastPath)
    }

    private fun initialState(): SimpleConversionState {
        val restoredPath = savedState.getOrNull<Path>(key = "last_path")

        return SimpleConversionState(lastPath = restoredPath)
    }

    private fun updateIcon(path: Path, valkyriesSettings: ValkyriesSettings) {
        val output = runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(path)
            ImageVectorGenerator.convert(
                vector = parserOutput.vector,
                kotlinName = parserOutput.kotlinName,
                config = ImageVectorGeneratorConfig(
                    packageName = valkyriesSettings.packageName,
                    iconPackPackage = valkyriesSettings.packageName,
                    packName = "",
                    nestedPackName = "",
                    outputFormat = valkyriesSettings.outputFormat,
                    generatePreview = valkyriesSettings.generatePreview,
                    useFlatPackage = false,
                    useExplicitMode = valkyriesSettings.useExplicitMode,
                    addTrailingComma = valkyriesSettings.addTrailingComma,
                ),
            ).content
        }.getOrElse {
            it.message.orEmpty()
        }

        _state.updateState { copy(iconContent = output) }
    }

    fun selectPath(path: Path) {
        _state.updateState { copy(lastPath = path) }
    }

    fun reset() {
        _state.updateState { copy(iconContent = null, lastPath = null) }
    }

    fun pasteFromClipboard(text: String) = viewModelScope.launch(Dispatchers.Default) {
        val output = runCatching {
            val valkyriesSettings = inMemorySettings.current

            val parserOutput = SvgXmlParser.toIrImageVector(text)

            ImageVectorGenerator.convert(
                vector = parserOutput.vector,
                kotlinName = parserOutput.kotlinName,
                config = ImageVectorGeneratorConfig(
                    packageName = valkyriesSettings.packageName,
                    iconPackPackage = valkyriesSettings.packageName,
                    packName = "",
                    nestedPackName = "",
                    outputFormat = valkyriesSettings.outputFormat,
                    generatePreview = valkyriesSettings.generatePreview,
                    useFlatPackage = false,
                    useExplicitMode = valkyriesSettings.useExplicitMode,
                    addTrailingComma = valkyriesSettings.addTrailingComma,
                ),
            ).content
        }.getOrNull()

        if (output.isNullOrEmpty()) {
            _events.emit("Failed to parse icon from clipboard")
        } else {
            _state.updateState { copy(iconContent = output) }
        }
    }
}
