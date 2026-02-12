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
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.FileBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource.StringBasedIcon
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState.ConversionState
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SimpleConversionViewModel(
    savedState: MutableSavedState,
    params: SimpleConversionParamsSource,
) : ViewModel() {

    val inMemorySettings = inject(DI.core.inMemorySettings)

    private val stateRecord = savedState.recordOf<SimpleConversionState>(
        key = "conversionState",
        initialValue = SimpleConversionState.Loading,
    )
    val state = stateRecord.asStateFlow()

    init {
        when (params) {
            is SimpleConversionParamsSource.PathSource -> selectPath(params.path)
            is SimpleConversionParamsSource.TextSource -> fromText(text = params.text, name = params.name)
        }
        inMemorySettings.settings
            .onEach {
                val currentState = stateRecord.value.safeAs<ConversionState>() ?: return@onEach

                when (val source = currentState.iconSource) {
                    is FileBasedIcon -> {
                        parseIcon(
                            path = source.path,
                            iconName = currentState.iconContent.name,
                        ).onSuccess {
                            stateRecord.value = ConversionState(
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
                            stateRecord.value = ConversionState(
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

    fun selectPath(path: Path) = viewModelScope.launch(Dispatchers.Default) {
        parseIcon(path)
            .onFailure {
                stateRecord.value = SimpleConversionState.Error(
                    message = "Failed to parse icon",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ConversionState(
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
                stateRecord.value = SimpleConversionState.Error(
                    message = "Failed to parse icon from clipboard",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ConversionState(
                    iconSource = StringBasedIcon(text),
                    iconContent = it,
                )
            }
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.Default) {
        val conversionState = stateRecord.value.safeAs<ConversionState>() ?: return@launch

        when (val source = conversionState.iconSource) {
            is FileBasedIcon -> {
                parseIcon(path = source.path, iconName = name)
                    .onSuccess {
                        stateRecord.value = ConversionState(
                            iconSource = FileBasedIcon(conversionState.iconSource.path),
                            iconContent = it,
                        )
                    }
            }
            is StringBasedIcon -> {
                parseIcon(text = source.text, iconName = name)
                    .onSuccess {
                        stateRecord.value = ConversionState(
                            iconSource = StringBasedIcon(conversionState.iconSource.text),
                            iconContent = it,
                        )
                    }
            }
        }
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
        )
    }
}
