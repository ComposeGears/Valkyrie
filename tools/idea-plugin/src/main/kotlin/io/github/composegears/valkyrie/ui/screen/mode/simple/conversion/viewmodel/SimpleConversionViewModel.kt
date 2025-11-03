package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreenParams
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.PickerState
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class SimpleConversionViewModel(
    savedState: MutableSavedState,
    params: SimpleConversionScreenParams?,
) : ViewModel() {

    val inMemorySettings = inject(DI.core.inMemorySettings)

    private val stateRecord = savedState.recordOf<SimpleConversionState>(
        key = "conversionState",
        initialValue = PickerState,
    )
    val state = stateRecord.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    init {
        state
            .combine(inMemorySettings.settings) { state, _ ->
                if (state is ConversionState) {
                    when (val icon = state.iconSource) {
                        is IconSource.FileBasedIcon -> {
                            val output = parseIcon(path = icon.path, iconName = state.iconContent.name)

                            if (output != null) {
                                stateRecord.value = ConversionState(
                                    iconSource = IconSource.FileBasedIcon(icon.path),
                                    iconContent = output,
                                    mode = state.mode,
                                )
                            }
                        }
                        is IconSource.StringBasedIcon -> {
                            val output = parseIcon(text = icon.text, iconName = state.iconContent.name)

                            if (output != null) {
                                stateRecord.value = ConversionState(
                                    iconSource = IconSource.StringBasedIcon(icon.text),
                                    iconContent = output,
                                    mode = state.mode,
                                )
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)

        if (params != null) {
            fromText(
                text = params.iconContent,
                name = IconNameFormatter.format(params.iconName),
            )
        }
    }

    fun selectPath(path: Path) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(path)

        if (output == null) {
            _events.emit("Failed to parse icon")
        } else {
            stateRecord.value = ConversionState(
                iconSource = IconSource.FileBasedIcon(path),
                iconContent = output,
            )
        }
        saveCurrentMode()
    }

    fun reset() {
        stateRecord.value = PickerState
    }

    fun fromText(text: String, name: String) = pasteFromClipboard(
        text = text,
        iconName = name,
        mode = Mode.StringParse,
    )

    fun pasteFromClipboard(
        text: String,
        iconName: String = "IconName",
        mode: Mode = Mode.Picker,
    ) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(text = text, iconName = iconName)

        if (output == null) {
            _events.emit("Failed to parse icon from clipboard")
        } else {
            stateRecord.value = ConversionState(
                iconSource = IconSource.StringBasedIcon(text),
                iconContent = output,
                mode = mode,
            )
        }

        saveCurrentMode()
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.Default) {
        val conversionState = stateRecord.value.safeAs<ConversionState>() ?: return@launch

        when (conversionState.iconSource) {
            is IconSource.FileBasedIcon -> {
                val output = parseIcon(
                    path = conversionState.iconSource.path,
                    iconName = name,
                )

                if (output != null) {
                    stateRecord.value = ConversionState(
                        iconSource = IconSource.FileBasedIcon(conversionState.iconSource.path),
                        iconContent = output,
                    )
                }
            }
            is IconSource.StringBasedIcon -> {
                val output = parseIcon(
                    text = conversionState.iconSource.text,
                    iconName = name,
                )

                if (output != null) {
                    stateRecord.value = ConversionState(
                        iconSource = IconSource.StringBasedIcon(conversionState.iconSource.text),
                        iconContent = output,
                    )
                }
            }
        }
    }

    private fun parseIcon(
        path: Path,
        iconName: String? = null,
    ): IconContent? {
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
        }.getOrNull()
    }

    private fun parseIcon(
        text: String,
        iconName: String,
    ): IconContent? {
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
        }.getOrNull()
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
            previewAnnotationType = valkyriesSettings.previewAnnotationType,
            useFlatPackage = false,
            useExplicitMode = valkyriesSettings.useExplicitMode,
            addTrailingComma = valkyriesSettings.addTrailingComma,
            indentSize = valkyriesSettings.indentSize,
        )
    }

    private fun saveCurrentMode() {
        inMemorySettings.update {
            mode = io.github.composegears.valkyrie.shared.Mode.Simple
        }
    }
}

// TODO: split into separate screens
enum class Mode {
    Picker,
    StringParse,
}
