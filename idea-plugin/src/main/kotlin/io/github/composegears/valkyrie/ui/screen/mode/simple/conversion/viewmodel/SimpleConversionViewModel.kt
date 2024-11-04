package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel

import com.composegears.tiamat.Saveable
import com.composegears.tiamat.SavedState
import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.PickerState
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
    private val savedState: SavedState?,
) : TiamatViewModel(),
    Saveable {

    private val inMemorySettings by DI.core.inMemorySettings

    private val _state = MutableStateFlow(initialState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    init {
        _state
            .combine(inMemorySettings.settings) { state, _ ->
                if (state is ConversionState) {
                    when (val icon = state.iconSource) {
                        is IconSource.FileBasedIcon -> {
                            val output = parseIcon(path = icon.path, iconName = state.iconContent.name)

                            if (output != null) {
                                _state.updateState {
                                    ConversionState(
                                        iconSource = IconSource.FileBasedIcon(icon.path),
                                        iconContent = output,
                                    )
                                }
                            }
                        }
                        is IconSource.StringBasedIcon -> {
                            val output = parseIcon(text = icon.text, iconName = state.iconContent.name)

                            if (output != null) {
                                _state.updateState {
                                    ConversionState(
                                        iconSource = IconSource.StringBasedIcon(icon.text),
                                        iconContent = output,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun saveToSaveState(): SavedState {
        val conversionState = _state.value.safeAs<ConversionState>() ?: return emptyMap()

        return mapOf("conversionState" to conversionState)
    }

    fun selectPath(path: Path) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(path)

        if (output == null) {
            _events.emit("Failed to parse icon")
        } else {
            _state.updateState {
                ConversionState(
                    iconSource = IconSource.FileBasedIcon(path),
                    iconContent = output,
                )
            }
        }
    }

    fun reset() = _state.updateState { PickerState }

    fun pasteFromClipboard(text: String) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(text = text, iconName = "IconName")

        if (output == null) {
            _events.emit("Failed to parse icon from clipboard")
        } else {
            _state.updateState {
                ConversionState(
                    iconSource = IconSource.StringBasedIcon(text),
                    iconContent = output,
                )
            }
        }
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.Default) {
        val conversionState = _state.value.safeAs<ConversionState>() ?: return@launch

        when (conversionState.iconSource) {
            is IconSource.FileBasedIcon -> {
                val output = parseIcon(
                    path = conversionState.iconSource.path,
                    iconName = name,
                )

                if (output != null) {
                    _state.updateState {
                        ConversionState(
                            iconSource = IconSource.FileBasedIcon(conversionState.iconSource.path),
                            iconContent = output,
                        )
                    }
                }
            }
            is IconSource.StringBasedIcon -> {
                val output = parseIcon(
                    text = conversionState.iconSource.text,
                    iconName = name,
                )

                if (output != null) {
                    _state.updateState {
                        ConversionState(
                            iconSource = IconSource.StringBasedIcon(conversionState.iconSource.text),
                            iconContent = output,
                        )
                    }
                }
            }
        }
    }

    private fun initialState(): SimpleConversionState {
        return savedState?.getOrNull<ConversionState>(key = "conversionState") ?: PickerState
    }

    private fun parseIcon(
        path: Path,
        iconName: String? = null,
    ): IconContent? {
        val valkyriesSettings = inMemorySettings.current

        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(path)
            val name = iconName ?: parserOutput.iconName

            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = name,
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
                    indentSize = valkyriesSettings.indentSize,
                ),
            )
            IconContent(
                name = name,
                code = output.content,
                iconType = parserOutput.iconType,
            )
        }.getOrNull()
    }

    private fun parseIcon(
        text: String,
        iconName: String,
    ): IconContent? {
        val valkyriesSettings = inMemorySettings.current

        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(text, iconName)

            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = iconName,
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
                    indentSize = valkyriesSettings.indentSize,
                ),
            )
            IconContent(
                name = iconName,
                code = output.content,
                iconType = parserOutput.iconType,
            )
        }.getOrNull()
    }
}
