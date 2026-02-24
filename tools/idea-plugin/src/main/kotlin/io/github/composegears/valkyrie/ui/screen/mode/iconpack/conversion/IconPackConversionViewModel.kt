package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.isSvg
import io.github.composegears.valkyrie.parser.unified.ext.isXml
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ConversionEvent.OpenPreview
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsIssuesResolver.resolve
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsValidator.validate
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IconPackConversionViewModel(
    savedState: MutableSavedState,
    paths: List<Path>,
) : ViewModel() {

    val inMemorySettings = inject(DI.core.inMemorySettings)

    private val iconsRecord = savedState.recordOf<List<BatchIcon>?>("icons", null)

    private val _state = MutableStateFlow<IconPackConversionState>(IconsPickering)
    val state = _state.asStateFlow()

    private val _events = Channel<ConversionEvent>()
    val events = _events.receiveAsFlow()

    private val isFlatPackage: Boolean
        get() = inMemorySettings.current.flatPackage

    init {
        val restoredState = iconsRecord.value

        when {
            restoredState != null -> {
                if (restoredState.isEmpty()) {
                    _state.updateState { IconsPickering }
                } else {
                    _state.updateState {
                        BatchProcessing.IconPackCreationState(
                            icons = restoredState,
                            importIssues = validate(batchIcons = restoredState, useFlatPackage = isFlatPackage),
                        )
                    }
                }
            }
            paths.isNotEmpty() -> {
                if (paths.size == 1 && paths.first().isDirectory()) {
                    pickerEvent(PickerEvent.PickDirectory(paths.first()))
                } else {
                    pickerEvent(PickerEvent.PickFiles(paths))
                }
            }
        }
        _state
            .onEach {
                iconsRecord.value = when (val state = _state.value) {
                    is BatchProcessing.IconPackCreationState -> state.icons
                    else -> emptyList()
                }
            }
            .launchIn(viewModelScope)

        inMemorySettings.settings
            .onEach { settings ->
                _state.updateState {
                    when (this) {
                        is BatchProcessing.IconPackCreationState -> {
                            copy(importIssues = validate(batchIcons = icons, useFlatPackage = settings.flatPackage))
                        }
                        else -> this
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun pickerEvent(events: PickerEvent) {
        when (events) {
            is PickerEvent.PickDirectory -> events.path.listDirectoryEntries().processFiles()
            is PickerEvent.PickFiles -> events.paths.processFiles()
            is PickerEvent.ClipboardText -> processText(events.text)
        }
    }

    fun deleteIcon(iconId: IconId) = viewModelScope.launch(Dispatchers.Default) {
        _state.updateState {
            when (this) {
                is BatchProcessing.IconPackCreationState -> {
                    val iconsToProcess = icons.filter { it.id != iconId }

                    if (iconsToProcess.isEmpty()) {
                        IconsPickering
                    } else {
                        copy(
                            icons = iconsToProcess,
                            importIssues = validate(batchIcons = iconsToProcess, useFlatPackage = isFlatPackage),
                        )
                    }
                }
                else -> this
            }
        }
    }

    fun updateIconPack(batchIcon: BatchIcon, nestedPack: String) = viewModelScope.launch(Dispatchers.Default) {
        _state.updateState {
            when (this) {
                is BatchProcessing.IconPackCreationState -> {
                    val updatedIcons = icons.map { icon ->
                        if (icon.id == batchIcon.id && icon is BatchIcon.Valid) {
                            icon.copy(
                                iconPack = when (icon.iconPack) {
                                    is IconPack.Nested -> icon.iconPack.copy(currentNestedPack = nestedPack)
                                    is IconPack.Single -> icon.iconPack
                                },
                            )
                        } else {
                            icon
                        }
                    }
                    copy(
                        icons = updatedIcons,
                        importIssues = validate(batchIcons = updatedIcons, useFlatPackage = isFlatPackage),
                    )
                }
                else -> this
            }
        }
    }

    fun showPreview(icon: BatchIcon.Valid) = viewModelScope.launch(Dispatchers.Default) {
        val settings = inMemorySettings.current
        val output = ImageVectorGenerator.convert(
            vector = icon.irImageVector,
            iconName = icon.iconName.name,
            config = settings.toImageVectorConfig(
                packageName = icon.iconPack.iconPackage,
                nestedPackName = icon.iconPack.currentNestedPack,
            ),
        )

        _events.send(OpenPreview(output.content))
    }

    fun import() = viewModelScope.launch(Dispatchers.Default) {
        val icons = when (val state = _state.value) {
            is BatchProcessing.IconPackCreationState -> state.icons
            else -> return@launch
        }

        _state.updateState { BatchProcessing.ImportingState }

        val settings = inMemorySettings.current

        icons
            .filterIsInstance<BatchIcon.Valid>()
            .forEach { icon ->
                when (val iconPack = icon.iconPack) {
                    is IconPack.Nested -> {
                        val vectorSpecOutput = ImageVectorGenerator.convert(
                            vector = icon.irImageVector,
                            iconName = icon.iconName.name,
                            config = settings.toImageVectorConfig(
                                packageName = icon.iconPack.iconPackage,
                                nestedPackName = iconPack.currentNestedPack,
                            ),
                        )

                        withContext(Dispatchers.IO) {
                            vectorSpecOutput.content.writeToKt(
                                outputDir = when {
                                    settings.flatPackage -> settings.iconPackDestination
                                    else -> "${settings.iconPackDestination}/${iconPack.currentNestedPack.lowercase()}"
                                },
                                nameWithoutExtension = vectorSpecOutput.name,
                            )
                        }
                    }
                    is IconPack.Single -> {
                        val vectorSpecOutput = ImageVectorGenerator.convert(
                            vector = icon.irImageVector,
                            iconName = icon.iconName.name,
                            config = settings.toImageVectorConfig(
                                packageName = icon.iconPack.iconPackage,
                                nestedPackName = "",
                            ),
                        )

                        withContext(Dispatchers.IO) {
                            vectorSpecOutput.content.writeToKt(
                                outputDir = settings.iconPackDestination,
                                nameWithoutExtension = vectorSpecOutput.name,
                            )
                        }
                    }
                }
            }

        _events.send(ConversionEvent.ImportCompleted)
        reset()
    }

    fun renameIcon(batchIcon: BatchIcon, newName: IconName) = viewModelScope.launch(Dispatchers.Default) {
        _state.updateState {
            when (this) {
                is BatchProcessing.IconPackCreationState -> {
                    val icons = icons.map { icon ->
                        if (icon.id == batchIcon.id) {
                            when (icon) {
                                is BatchIcon.Broken -> icon
                                is BatchIcon.Valid -> icon.copy(iconName = newName)
                            }
                        } else {
                            icon
                        }
                    }
                    copy(
                        icons = icons,
                        importIssues = validate(icons, useFlatPackage = isFlatPackage),
                    )
                }
                else -> this
            }
        }
    }

    fun reset() {
        _state.updateState { IconsPickering }
    }

    fun resolveImportIssues() = viewModelScope.launch {
        val creationState = _state.value.safeAs<BatchProcessing.IconPackCreationState>() ?: return@launch

        val resolvedIcons = resolve(batchIcons = creationState.icons, useFlatPackage = isFlatPackage)

        if (resolvedIcons.isEmpty()) {
            _events.send(ConversionEvent.NothingToImport)
            reset()
        } else {
            _state.updateState {
                creationState.copy(
                    icons = resolvedIcons,
                    importIssues = validate(batchIcons = resolvedIcons, useFlatPackage = isFlatPackage),
                )
            }
        }
    }

    private fun processText(text: String) = viewModelScope.launch(Dispatchers.Default) {
        val iconName = ""
        val output = runCatching { SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, text, iconName) }.getOrNull()

        val icon = when (output) {
            null -> BatchIcon.Broken(
                iconName = IconName(iconName),
                iconSource = IconSource.Clipboard,
            )
            else -> BatchIcon.Valid(
                iconName = IconName(output.iconName),
                iconType = output.iconType,
                iconPack = inMemorySettings.current.buildDefaultIconPack(),
                irImageVector = output.irImageVector,
            )
        }
        _state.updateState {
            val existingIcons = safeAs<BatchProcessing.IconPackCreationState>()?.icons.orEmpty()
            val icons = existingIcons + icon

            BatchProcessing.IconPackCreationState(
                icons = icons,
                importIssues = validate(batchIcons = icons, useFlatPackage = isFlatPackage),
            )
        }
    }

    private fun List<Path>.processFiles() = viewModelScope.launch(Dispatchers.Default) {
        val paths = filter { it.isRegularFile() && (it.toIOPath().isXml || it.toIOPath().isSvg) }
        val lastIcons = _state.value.safeAs<BatchProcessing.IconPackCreationState>()?.icons.orEmpty()

        if (paths.isNotEmpty()) {
            _state.updateState { BatchProcessing.ImportValidationState }
            _state.updateState {
                val newIcons = paths
                    .sortedBy { it.name }
                    .map { path ->
                        val output = runCatching {
                            SvgXmlParser.toIrImageVector(
                                parser = ParserType.Jvm,
                                path = path.toIOPath(),
                            )
                        }.getOrNull()

                        when (output) {
                            null -> BatchIcon.Broken(
                                iconName = IconName(path.name),
                                iconSource = IconSource.File,
                            )
                            else -> BatchIcon.Valid(
                                iconName = IconName(output.iconName),
                                iconType = output.iconType,
                                iconPack = inMemorySettings.current.buildDefaultIconPack(),
                                irImageVector = output.irImageVector,
                            )
                        }
                    }
                val icons = lastIcons + newIcons

                BatchProcessing.IconPackCreationState(
                    icons = icons,
                    importIssues = validate(batchIcons = icons, useFlatPackage = isFlatPackage),
                )
            }
        }
    }

    private fun ValkyriesSettings.buildDefaultIconPack(): IconPack {
        return when {
            nestedPacks.isEmpty() -> {
                IconPack.Single(
                    iconPackName = iconPackName,
                    iconPackage = packageName,
                )
            }
            else -> IconPack.Nested(
                iconPackName = iconPackName,
                iconPackage = packageName,
                currentNestedPack = nestedPacks.first(),
                nestedPacks = nestedPacks,
            )
        }
    }

    private fun ValkyriesSettings.toImageVectorConfig(
        packageName: String,
        nestedPackName: String = "",
    ): ImageVectorGeneratorConfig {
        return ImageVectorGeneratorConfig(
            packageName = packageName,
            iconPackPackage = iconPackPackage,
            packName = iconPackName,
            nestedPackName = nestedPackName,
            outputFormat = outputFormat,
            useComposeColors = useComposeColors,
            generatePreview = generatePreview,
            useFlatPackage = flatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            usePathDataString = usePathDataString,
            indentSize = indentSize,
        )
    }
}

sealed interface ConversionEvent {
    data class OpenPreview(val iconContent: String) : ConversionEvent
    data object ImportCompleted : ConversionEvent
    data object NothingToImport : ConversionEvent
}
