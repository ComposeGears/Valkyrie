package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import com.composegears.tiamat.Saveable
import com.composegears.tiamat.SavedState
import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.isSvg
import io.github.composegears.valkyrie.parser.unified.ext.isXml
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ConversionEvent.OpenPreview
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.util.getOrNull
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IconPackConversionViewModel(
    savedState: SavedState?,
    paths: List<Path>,
) : TiamatViewModel(),
    Saveable {

    val inMemorySettings by DI.core.inMemorySettings

    private val _state = MutableStateFlow<IconPackConversionState>(IconsPickering)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ConversionEvent>()
    val events = _events.asSharedFlow()

    init {
        val restoredState = savedState?.getOrNull<List<BatchIcon>>(key = "icons")

        when {
            restoredState != null -> {
                if (restoredState.isEmpty()) {
                    _state.updateState { IconsPickering }
                } else {
                    _state.updateState {
                        BatchProcessing.IconPackCreationState(
                            icons = restoredState,
                            exportEnabled = restoredState.isAllIconsValid(),
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
    }

    override fun saveToSaveState(): SavedState {
        return when (val state = _state.value) {
            is BatchProcessing.IconPackCreationState -> mapOf("icons" to state.icons)
            else -> mapOf("icons" to emptyList<List<BatchIcon>>())
        }
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
                            exportEnabled = iconsToProcess.isAllIconsValid(),
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
                    copy(
                        icons = icons.map { icon ->
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
                        },
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
            config = ImageVectorGeneratorConfig(
                packageName = icon.iconPack.iconPackage,
                iconPackPackage = settings.iconPackPackage,
                packName = settings.iconPackName,
                nestedPackName = icon.iconPack.currentNestedPack,
                outputFormat = settings.outputFormat,
                useComposeColors = settings.useComposeColors,
                generatePreview = settings.generatePreview,
                previewAnnotationType = settings.previewAnnotationType,
                useFlatPackage = settings.flatPackage,
                useExplicitMode = settings.useExplicitMode,
                addTrailingComma = settings.addTrailingComma,
                indentSize = settings.indentSize,
            ),
        )

        _events.emit(OpenPreview(output.content))
    }

    fun export() = viewModelScope.launch(Dispatchers.Default) {
        val icons = when (val state = _state.value) {
            is BatchProcessing.IconPackCreationState -> state.icons
            else -> return@launch
        }

        _state.updateState { BatchProcessing.ExportingState }

        val settings = inMemorySettings.current

        icons
            .filterIsInstance<BatchIcon.Valid>()
            .forEach { icon ->
                when (val iconPack = icon.iconPack) {
                    is IconPack.Nested -> {
                        val vectorSpecOutput = ImageVectorGenerator.convert(
                            vector = icon.irImageVector,
                            iconName = icon.iconName.name,
                            config = ImageVectorGeneratorConfig(
                                packageName = icon.iconPack.iconPackage,
                                iconPackPackage = settings.iconPackPackage,
                                packName = settings.iconPackName,
                                nestedPackName = iconPack.currentNestedPack,
                                outputFormat = settings.outputFormat,
                                useComposeColors = settings.useComposeColors,
                                generatePreview = settings.generatePreview,
                                previewAnnotationType = settings.previewAnnotationType,
                                useFlatPackage = settings.flatPackage,
                                useExplicitMode = settings.useExplicitMode,
                                addTrailingComma = settings.addTrailingComma,
                                indentSize = settings.indentSize,
                            ),
                        )

                        vectorSpecOutput.content.writeToKt(
                            outputDir = when {
                                settings.flatPackage -> settings.iconPackDestination
                                else -> "${settings.iconPackDestination}/${iconPack.currentNestedPack.lowercase()}"
                            },
                            nameWithoutExtension = vectorSpecOutput.name,
                        )
                    }
                    is IconPack.Single -> {
                        val vectorSpecOutput = ImageVectorGenerator.convert(
                            vector = icon.irImageVector,
                            iconName = icon.iconName.name,
                            config = ImageVectorGeneratorConfig(
                                packageName = icon.iconPack.iconPackage,
                                iconPackPackage = settings.iconPackPackage,
                                packName = settings.iconPackName,
                                nestedPackName = "",
                                outputFormat = settings.outputFormat,
                                useComposeColors = settings.useComposeColors,
                                generatePreview = settings.generatePreview,
                                previewAnnotationType = settings.previewAnnotationType,
                                useFlatPackage = settings.flatPackage,
                                useExplicitMode = settings.useExplicitMode,
                                addTrailingComma = settings.addTrailingComma,
                                indentSize = settings.indentSize,
                            ),
                        )

                        vectorSpecOutput.content.writeToKt(
                            outputDir = settings.iconPackDestination,
                            nameWithoutExtension = vectorSpecOutput.name,
                        )
                    }
                }
            }

        _events.emit(ConversionEvent.ExportCompleted)
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
                        exportEnabled = icons.isAllIconsValid(),
                    )
                }
                else -> this
            }
        }
    }

    fun reset() {
        _state.updateState { IconsPickering }
    }

    private fun processText(text: String) = viewModelScope.launch(Dispatchers.Default) {
        val iconName = ""
        val output =
            runCatching { SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, text, iconName) }.getOrNull()

        val icon = when (output) {
            null -> BatchIcon.Broken(iconName = IconName(iconName))
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
                exportEnabled = icons.isAllIconsValid(),
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
                            null -> BatchIcon.Broken(iconName = IconName(path.name))
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
                    exportEnabled = icons.isAllIconsValid(),
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

    private fun List<BatchIcon>.isAllIconsValid() = isNotEmpty() &&
        all { it is BatchIcon.Valid } &&
        all { it.iconName.name.isNotEmpty() && !it.iconName.name.contains(" ") } &&
        hasNoDuplicates()

    private fun List<BatchIcon>.hasNoDuplicates() = map { it.iconName.name }.toSet().size == size
}

sealed interface ConversionEvent {
    data class OpenPreview(val iconContent: String) : ConversionEvent
    data object ExportCompleted : ConversionEvent
}
