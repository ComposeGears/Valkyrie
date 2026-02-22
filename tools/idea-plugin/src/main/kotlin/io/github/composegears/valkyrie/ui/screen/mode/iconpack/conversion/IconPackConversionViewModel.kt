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
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.checkImportIssues
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
                            importIssues = restoredState.checkImportIssues(useFlatPackage = isFlatPackage),
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
                            copy(importIssues = icons.checkImportIssues(useFlatPackage = settings.flatPackage))
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
                            importIssues = iconsToProcess.checkImportIssues(useFlatPackage = isFlatPackage),
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
                        importIssues = updatedIcons.checkImportIssues(useFlatPackage = isFlatPackage),
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
                        importIssues = icons.checkImportIssues(useFlatPackage = isFlatPackage),
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

        val processedIcons = creationState.icons
            .filterIsInstance<BatchIcon.Valid>()
            .map { icon ->
                val name = icon.iconName.name

                when {
                    name.isEmpty() -> icon.copy(iconName = IconName("IconName"))
                    name.contains(" ") -> icon.copy(iconName = IconName(name.replace(" ", "")))
                    else -> icon
                }
            }

        // Group icons by their output location (considering useFlatPackage)
        val iconsByLocation = processedIcons.groupBy { icon ->
            when (val pack = icon.iconPack) {
                is IconPack.Single -> pack.iconPackName
                is IconPack.Nested -> when {
                    isFlatPackage -> pack.iconPackName // All in same location when flat
                    else -> "${pack.iconPackName}.${pack.currentNestedPack}" // Separate locations
                }
            }
        }

        // Process duplicates within each location group
        val resolvedIcons = iconsByLocation.flatMap { (_, iconsInLocation) ->
            // Track all committed names to ensure uniqueness across both passes
            val committedNames = mutableSetOf<String>()

            // First, resolve exact duplicates
            val nameGroups = iconsInLocation.groupBy { it.iconName.name }
            val nameCounters = mutableMapOf<String, Int>()

            val iconsWithResolvedExactDuplicates = iconsInLocation.map { icon ->
                val originalName = icon.iconName.name
                val group = nameGroups[originalName]

                if (group != null && group.size > 1) {
                    val counter = nameCounters.getOrDefault(originalName, 0) + 1
                    nameCounters[originalName] = counter

                    if (counter > 1) {
                        // Generate unique name by incrementing suffix until not in committedNames
                        var suffix = counter - 1
                        var candidateName = "$originalName$suffix"
                        while (committedNames.any { it.equals(candidateName, ignoreCase = true) }) {
                            suffix++
                            candidateName = "$originalName$suffix"
                        }
                        committedNames.add(candidateName)
                        icon.copy(iconName = IconName(candidateName))
                    } else {
                        committedNames.add(originalName)
                        icon
                    }
                } else {
                    committedNames.add(originalName)
                    icon
                }
            }

            // Then, resolve case-insensitive duplicates
            val lowercaseGroups = iconsWithResolvedExactDuplicates.groupBy { it.iconName.name.lowercase() }
            val lowercaseCounters = mutableMapOf<String, Int>()

            iconsWithResolvedExactDuplicates.map { icon ->
                val currentName = icon.iconName.name
                val lowercaseKey = currentName.lowercase()
                val group = lowercaseGroups[lowercaseKey]

                // Only process if there are multiple icons with same lowercase name but different actual names
                if (group != null && group.size > 1 && group.map { it.iconName.name }.distinct().size > 1) {
                    val counter = lowercaseCounters.getOrDefault(lowercaseKey, 0) + 1
                    lowercaseCounters[lowercaseKey] = counter

                    if (counter > 1) {
                        // Generate unique name by incrementing suffix until not in committedNames
                        var suffix = counter - 1
                        var candidateName = "$currentName$suffix"
                        while (committedNames.any { it.equals(candidateName, ignoreCase = true) }) {
                            suffix++
                            candidateName = "$currentName$suffix"
                        }
                        committedNames.add(candidateName)
                        icon.copy(iconName = IconName(candidateName))
                    } else {
                        // First in group - name is already in committedNames from exact duplicate pass
                        icon
                    }
                } else {
                    icon
                }
            }
        }

        if (resolvedIcons.isEmpty()) {
            _events.send(ConversionEvent.NothingToImport)
            reset()
        } else {
            _state.updateState {
                creationState.copy(
                    icons = resolvedIcons,
                    importIssues = resolvedIcons.checkImportIssues(useFlatPackage = isFlatPackage),
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
                importIssues = icons.checkImportIssues(useFlatPackage = isFlatPackage),
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
                    importIssues = icons.checkImportIssues(useFlatPackage = isFlatPackage),
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
