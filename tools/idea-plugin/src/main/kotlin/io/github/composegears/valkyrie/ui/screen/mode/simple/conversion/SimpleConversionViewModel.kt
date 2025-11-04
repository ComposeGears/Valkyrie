package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

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
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.FileBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.StringBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SimpleConversionViewModel(
    savedState: MutableSavedState,
    params: SimpleConversionParamsSource,
) : ViewModel() {

    val inMemorySettings = inject(DI.core.inMemorySettings)

    private val stateRecord = savedState.recordOf<SimpleConversionState?>(
        key = "conversionState",
        initialValue = null,
    )
    val state = stateRecord.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    init {
        when (params) {
            is SimpleConversionParamsSource.PathSource -> selectPath(params.path)
            is SimpleConversionParamsSource.TextSource -> fromText(text = params.text, name = params.name)
        }
        inMemorySettings.settings
            .onEach {
                val currentState = stateRecord.value ?: return@onEach

                when (val source = currentState.iconSource) {
                    is FileBasedIcon -> {
                        val output = parseIcon(
                            path = source.path,
                            iconName = currentState.iconContent.name,
                        )
                        if (output != null) {
                            stateRecord.value = SimpleConversionState(
                                iconSource = FileBasedIcon(source.path),
                                iconContent = output,
                            )
                        }
                    }
                    is StringBasedIcon -> {
                        val output = parseIcon(
                            text = source.text,
                            iconName = currentState.iconContent.name,
                        )

                        if (output != null) {
                            stateRecord.value = SimpleConversionState(
                                iconSource = StringBasedIcon(source.text),
                                iconContent = output,
                            )
                        }
                    }
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun selectPath(path: Path) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(path)

        if (output == null) {
            _events.emit("Failed to parse icon")
        } else {
            stateRecord.value = SimpleConversionState(
                iconSource = FileBasedIcon(path),
                iconContent = output,
            )
        }
    }

    fun fromText(text: String, name: String) = pasteFromClipboard(text = text, iconName = name)

    fun pasteFromClipboard(
        text: String,
        iconName: String = "IconName",
    ) = viewModelScope.launch(Dispatchers.Default) {
        val output = parseIcon(text = text, iconName = iconName)

        if (output == null) {
            _events.emit("Failed to parse icon from clipboard")
        } else {
            stateRecord.value = SimpleConversionState(
                iconSource = StringBasedIcon(text),
                iconContent = output,
            )
        }
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.Default) {
        val conversionState = stateRecord.value ?: return@launch

        when (conversionState.iconSource) {
            is FileBasedIcon -> {
                val output = parseIcon(
                    path = conversionState.iconSource.path,
                    iconName = name,
                )

                if (output != null) {
                    stateRecord.value = SimpleConversionState(
                        iconSource = FileBasedIcon(conversionState.iconSource.path),
                        iconContent = output,
                    )
                }
            }
            is StringBasedIcon -> {
                val output = parseIcon(
                    text = conversionState.iconSource.text,
                    iconName = name,
                )

                if (output != null) {
                    stateRecord.value = SimpleConversionState(
                        iconSource = StringBasedIcon(conversionState.iconSource.text),
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
}
