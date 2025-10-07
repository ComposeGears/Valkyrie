package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.ui.di.DI
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
                                )
                            }
                        }
                        is IconSource.StringBasedIcon -> {
                            val output = parseIcon(text = icon.text, iconName = state.iconContent.name)

                            if (output != null) {
                                stateRecord.value = ConversionState(
                                    iconSource = IconSource.StringBasedIcon(icon.text),
                                    iconContent = output,
                                )
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
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
    }

    fun reset() {
        stateRecord.value = PickerState
    }

    fun pasteFromClipboard(text: String) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(text = text, iconName = "IconName")

        if (output == null) {
            _events.emit("Failed to parse icon from clipboard")
        } else {
            stateRecord.value = ConversionState(
                iconSource = IconSource.StringBasedIcon(text),
                iconContent = output,
            )
        }
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
        val valkyriesSettings = inMemorySettings.current

        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path.toIOPath())
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
                    useComposeColors = valkyriesSettings.useComposeColors,
                    generatePreview = valkyriesSettings.generatePreview,
                    previewAnnotationType = valkyriesSettings.previewAnnotationType,
                    useFlatPackage = false,
                    useExplicitMode = valkyriesSettings.useExplicitMode,
                    addTrailingComma = valkyriesSettings.addTrailingComma,
                    indentSize = valkyriesSettings.indentSize,
                ),
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
        val valkyriesSettings = inMemorySettings.current

        return runCatching {
            val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, text, iconName)

            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = iconName,
                config = ImageVectorGeneratorConfig(
                    packageName = valkyriesSettings.packageName,
                    iconPackPackage = valkyriesSettings.packageName,
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
                ),
            )
            IconContent(
                name = iconName,
                code = output.content,
                irImageVector = parserOutput.irImageVector,
            )
        }.getOrNull()
    }
}
