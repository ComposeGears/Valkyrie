package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.FileBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.StringBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionEvent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionEvent.CopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionEvent.ExportKtFile
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState.ConversionState
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimpleConversionViewModel(
    savedState: SavedStateHandle,
    params: SimpleConversionParamsSource,
) : ViewModel() {

    val inMemorySettings = inject(DI.core.inMemorySettings)

    private val _state = savedState.getMutableStateFlow<SimpleConversionState>(
        key = "SimpleConversionViewModel",
        initialValue = SimpleConversionState.Initial,
    )
    val state = _state.asStateFlow()

    private val _events = Channel<SimpleConversionEvent>()
    val events = _events.receiveAsFlow()

    init {
        if (_state.value is SimpleConversionState.Initial) {
            when (params) {
                is SimpleConversionParamsSource.PathSource -> selectPath(params.path)
                is SimpleConversionParamsSource.TextSource -> fromText(text = params.text, name = params.name)
            }
        }
        inMemorySettings.settings
            .onEach {
                val currentState = _state.value.safeAs<ConversionState>() ?: return@onEach

                when (val source = currentState.iconSource) {
                    is FileBasedIcon -> {
                        parseIcon(
                            path = source.path,
                            iconName = currentState.iconContent.name,
                        ).onSuccess {
                            _state.value = ConversionState(
                                iconSource = FileBasedIcon(source.path),
                                iconContent = it,
                            )
                        }
                    }
                    is StringBasedIcon -> {
                        parseIcon(
                            text = source.text,
                            iconName = currentState.iconContent.name,
                        ).onSuccess {
                            _state.value = ConversionState(
                                iconSource = StringBasedIcon(source.text),
                                iconContent = it,
                            )
                        }
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onAction(action: SimpleConversionAction) {
        val state = _state.value.safeAs<ConversionState>() ?: return

        viewModelScope.launch {
            when (action) {
                is SimpleConversionAction.OnExport -> {
                    _events.send(
                        ExportKtFile(
                            fileName = "${state.iconContent.name}.kt",
                            content = action.text,
                        ),
                    )
                }
                is SimpleConversionAction.OnCopyInClipboard -> {
                    _events.send(CopyInClipboard(action.text))
                }
                is SimpleConversionAction.OnIconNameChange -> {
                    changeIconName(action.name)
                }
            }
        }
    }

    fun selectPath(path: Path) = viewModelScope.launch(Dispatchers.Default) {
        parseIcon(path)
            .onFailure {
                _state.value = SimpleConversionState.Error(
                    message = "Failed to parse icon",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                _state.value = ConversionState(
                    iconSource = FileBasedIcon(path),
                    iconContent = it,
                )
            }
    }

    fun fromText(text: String, name: String) = pasteFromClipboard(text = text, iconName = name)

    fun pasteFromClipboard(
        text: String,
        iconName: String = "IconName",
    ) = viewModelScope.launch(Dispatchers.Default) {
        parseIcon(text = text, iconName = iconName)
            .onFailure {
                _state.value = SimpleConversionState.Error(
                    message = "Failed to parse icon from clipboard",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                _state.value = ConversionState(
                    iconSource = StringBasedIcon(text),
                    iconContent = it,
                )
            }
    }

    private suspend fun changeIconName(name: String) = withContext(Dispatchers.Default) {
        val conversionState = _state.value.safeAs<ConversionState>() ?: return@withContext

        val output = ImageVectorGenerator.convert(
            vector = conversionState.iconContent.irImageVector,
            iconName = name,
            config = createGeneratorConfig(),
        )

        _state.value = ConversionState(
            iconSource = conversionState.iconSource,
            iconContent = IconContent(
                name = name,
                code = output.content,
                irImageVector = conversionState.iconContent.irImageVector,
            ),
        )
    }

    private fun parseIcon(
        path: Path,
        iconName: String? = null,
    ): Result<IconContent> {
        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path.toIOPath())
            val name = iconName ?: parserOutput.iconName

            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = name,
                config = createGeneratorConfig(),
            )
            IconContent(
                name = name,
                code = output.content,
                irImageVector = parserOutput.irImageVector,
            )
        }
    }

    private fun parseIcon(
        text: String,
        iconName: String,
    ): Result<IconContent> {
        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, text, iconName)

            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = iconName,
                config = createGeneratorConfig(),
            )
            IconContent(
                name = iconName,
                code = output.content,
                irImageVector = parserOutput.irImageVector,
            )
        }
    }

    private fun createGeneratorConfig(): ImageVectorGeneratorConfig {
        val valkyriesSettings = inMemorySettings.current

        return ImageVectorGeneratorConfig(
            // don't add package name for single icon conversion, let user decide where to put it
            packageName = "",
            iconPackPackage = "",
            packName = "",
            nestedPackName = "",
            outputFormat = valkyriesSettings.outputFormat,
            useComposeColors = valkyriesSettings.useComposeColors,
            generatePreview = valkyriesSettings.generatePreview,
            useFlatPackage = false,
            useExplicitMode = valkyriesSettings.useExplicitMode,
            addTrailingComma = valkyriesSettings.addTrailingComma,
            usePathDataString = valkyriesSettings.usePathDataString,
            indentSize = valkyriesSettings.indentSize,
            suppressUnusedReceiverWarning = valkyriesSettings.suppressUnusedReceiverWarning,
        )
    }
}
