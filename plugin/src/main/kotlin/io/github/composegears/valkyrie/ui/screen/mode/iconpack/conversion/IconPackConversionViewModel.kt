package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecOutput
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.processing.writter.FileWriter
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ConversionEvent.OpenPreview
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchFilesProcessing
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.util.toPainterOrNull
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension

class IconPackConversionViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val _state = MutableStateFlow<IconPackConversionState>(IconsPickering)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ConversionEvent>()
    val events = _events.asSharedFlow()

    private val valkyriesSettings = inMemorySettings.settings

    fun pickerEvent(events: PickerEvent) {
        when (events) {
            is PickerEvent.PickDirectory -> events.path.listDirectoryEntries().processFiles()
            is PickerEvent.PickFiles -> events.paths.processFiles()
        }
    }

    fun deleteIcon(iconName: IconName) {
        _state.updateState {
            when (this) {
                is IconsPickering -> this
                is BatchFilesProcessing -> {
                    val iconsToProcess = iconsToProcess.filter { it.iconName != iconName }

                    if (iconsToProcess.isEmpty()) {
                        _state.updateState { IconsPickering }
                        this
                    } else {
                        copy(iconsToProcess = iconsToProcess)
                    }
                }
            }
        }
    }

    fun updateIconPack(batchIcon: BatchIcon, nestedPack: String) {
        _state.updateState {
            when (this) {
                is IconsPickering -> this
                is BatchFilesProcessing -> {
                    copy(
                        iconsToProcess = iconsToProcess.map { icon ->
                            if (icon.iconName == batchIcon.iconName && icon is BatchIcon.Valid) {
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
                    )
                }
            }
        }
    }

    fun showPreview(iconName: IconName) = onReadBatchScope {
        val icon = iconsToProcess.first { it.iconName == iconName } as BatchIcon.Valid

        val iconResult = runCatching {
            val parserOutput = IconParser.toVector(icon.path)

            ImageVectorGenerator.convert(
                vector = parserOutput.vector,
                kotlinName = iconName.value,
                config = ImageVectorGeneratorConfig(
                    packageName = icon.iconPack.iconPackage,
                    packName = valkyriesSettings.value.iconPackName,
                    nestedPackName = icon.iconPack.currentNestedPack,
                    generatePreview = valkyriesSettings.value.generatePreview
                )
            )
        }.getOrDefault(ImageVectorSpecOutput.empty)

        viewModelScope.launch {
            _events.emit(OpenPreview(iconResult.content))
        }
    }

    fun export() {
        onReadBatchScope {
            val settings = inMemorySettings.current

            iconsToProcess
                .filterIsInstance<BatchIcon.Valid>()
                .forEach { icon ->
                    when (val iconPack = icon.iconPack) {
                        is IconPack.Nested -> {
                            val parserOutput = IconParser.toVector(icon.path)
                            val vectorSpecOutput = ImageVectorGenerator.convert(
                                vector = parserOutput.vector,
                                kotlinName = icon.iconName.value,
                                config = ImageVectorGeneratorConfig(
                                    packageName = icon.iconPack.iconPackage,
                                    packName = valkyriesSettings.value.iconPackName,
                                    nestedPackName = iconPack.currentNestedPack,
                                    generatePreview = valkyriesSettings.value.generatePreview
                                )
                            )

                            FileWriter.writeToFile(
                                content = vectorSpecOutput.content,
                                outDirectory = "${settings.iconPackDestination}/${iconPack.currentNestedPack.lowercase()}",
                                fileName = vectorSpecOutput.name
                            )
                        }
                        is IconPack.Single -> {
                            val parserOutput = IconParser.toVector(icon.path)
                            val vectorSpecOutput = ImageVectorGenerator.convert(
                                vector = parserOutput.vector,
                                kotlinName = parserOutput.kotlinName,
                                config = ImageVectorGeneratorConfig(
                                    packageName = icon.iconPack.iconPackage,
                                    packName = valkyriesSettings.value.iconPackName,
                                    nestedPackName = "",
                                    generatePreview = valkyriesSettings.value.generatePreview
                                )
                            )

                            FileWriter.writeToFile(
                                content = vectorSpecOutput.content,
                                outDirectory = settings.iconPackDestination,
                                fileName = vectorSpecOutput.name
                            )
                        }
                    }
                }

            viewModelScope.launch {
                _events.emit(ConversionEvent.ExportCompleted)
                reset()
            }
        }
    }

    fun renameIcon(batchIcon: BatchIcon, newName: IconName) {
        _state.updateState {
            when (this) {
                is IconsPickering -> this
                is BatchFilesProcessing -> {
                    copy(
                        iconsToProcess = iconsToProcess.map { icon ->
                            if (icon.iconName == batchIcon.iconName) {
                                when (icon) {
                                    is BatchIcon.Broken -> icon
                                    is BatchIcon.Valid -> icon.copy(iconName = newName)
                                }
                            } else {
                                icon
                            }
                        }
                    )
                }
            }
        }
    }

    fun reset() {
        _state.updateState { IconsPickering }
    }

    private fun List<Path>.processFiles() {
        val paths = filter { it.isRegularFile() && (it.extension == "xml" || it.extension == "svg") }

        if (paths.isNotEmpty()) {
            _state.updateState {
                BatchFilesProcessing(
                    iconsToProcess = paths
                        .sortedBy { it.name }
                        .map { path ->
                            when (val painter = path.toPainterOrNull()) {
                                null -> BatchIcon.Broken(
                                    iconName = IconName(path.nameWithoutExtension),
                                    extension = path.extension
                                )
                                else -> BatchIcon.Valid(
                                    iconName = IconName(IconParser.getIconName(path.name)),
                                    extension = path.extension,
                                    iconPack = inMemorySettings.current.buildDefaultIconPack(),
                                    path = path,
                                    painter = painter
                                )
                            }
                        }
                )
            }
        }
    }

    private fun ValkyriesSettings.buildDefaultIconPack(): IconPack {
        return when {
            nestedPacks.isEmpty() -> {
                IconPack.Single(
                    iconPackName = iconPackName,
                    iconPackage = packageName
                )
            }
            else -> IconPack.Nested(
                iconPackName = iconPackName,
                iconPackage = packageName,
                currentNestedPack = nestedPacks.first(),
                nestedPacks = nestedPacks
            )
        }
    }

    private fun onReadBatchScope(action: BatchFilesProcessing.() -> Unit) {
        when (val state = _state.value) {
            is IconsPickering -> Unit
            is BatchFilesProcessing -> {
                action(state)
            }
        }
    }
}

sealed interface ConversionEvent {
    data class OpenPreview(val iconContent: String) : ConversionEvent
    data object ExportCompleted : ConversionEvent
}

sealed interface PickerEvent {
    data class PickDirectory(val path: Path) : PickerEvent
    data class PickFiles(val paths: List<Path>) : PickerEvent
}