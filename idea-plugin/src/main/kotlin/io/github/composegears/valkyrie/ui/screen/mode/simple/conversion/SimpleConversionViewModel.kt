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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class SimpleConversionViewModel(
    inMemorySettings: InMemorySettings,
    savedState: SavedState?,
) : TiamatViewModel(),
    Saveable {

    private val _state = MutableStateFlow(SimpleConversionState())
    val state = _state.asStateFlow()

    init {
        _state
            .combine(inMemorySettings.settings) { state, settings ->
                if (state.lastPath != null) {
                    updateIcon(state.lastPath, settings)
                }
            }
            .launchIn(viewModelScope)

        val restoredPath = savedState?.getOrNull<Path>(key = "last_path")
        if (restoredPath != null) {
            selectPath(restoredPath)
        }
    }

    override fun saveToSaveState(): SavedState {
        return mapOf("last_path" to _state.value.lastPath)
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
}
