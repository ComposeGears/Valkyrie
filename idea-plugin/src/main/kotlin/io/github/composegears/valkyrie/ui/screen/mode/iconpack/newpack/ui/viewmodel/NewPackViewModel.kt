package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.generator.model.IconPack
import io.github.composegears.valkyrie.parser.svgxml.PackageExtractor
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util.IconPackWriter
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.AddNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.RemoveNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SaveDestination
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SavePack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.UseMaterialPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackEvent.OnSettingsUpdated
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackEvent.PreviewIconPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.ChooseExportDirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.PickedState
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.invariantSeparatorsPathString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewPackViewModel : TiamatViewModel() {

    private val inMemorySettings by DI.core.inMemorySettings

    private var inputHandler = NewPackInputHandler(inMemorySettings.current)

    private val _events = MutableSharedFlow<NewPackEvent>()
    val events = _events.asSharedFlow()

    private val currentState: NewPackModeState
        get() = state.value

    private val _state = MutableStateFlow<NewPackModeState>(inMemorySettings.current.toChooseDirectoryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            inputHandler.state.collect { inputFieldState ->
                _state.updateState {
                    if (this is PickedState) {
                        copy(
                            packEditState = packEditState.copy(inputFieldState = inputFieldState),
                            nextAvailable = inputFieldState.isValid,
                        )
                    } else {
                        this
                    }
                }
            }
        }
        inMemorySettings.settings
            .onEach { settings ->
                inputHandler.invalidate(settings)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NewPackAction) {
        when (action) {
            is SelectDestinationFolder -> updateDestinationPath(action.path)
            is SaveDestination -> {
                saveDestination()
                initDefaultPack()
            }
            is AddNestedPack -> inputHandler.addNestedPack()
            is RemoveNestedPack -> inputHandler.removeNestedPack(action.nestedPack)
            is PreviewPackObject -> previewIconPackObject()
            is SavePack -> saveIconPack()
            is UseMaterialPack -> {
                inMemorySettings.update {
                    useMaterialPack = action.value
                    flatPackage = action.value
                }

                _state.updateState {
                    if (this is PickedState) {
                        copy(useMaterialPack = action.value)
                    } else {
                        this
                    }
                }
            }
        }
    }

    fun onValueChange(change: InputChange) = viewModelScope.launch {
        inputHandler.handleInput(change)
    }

    private fun updateDestinationPath(path: Path) {
        _state.updateState {
            ChooseExportDirectoryState(
                iconPackDestination = path.absolutePathString(),
                predictedPackage = PackageExtractor.getFrom(path.invariantSeparatorsPathString).orEmpty(),
                nextAvailable = true,
            )
        }
    }

    private fun saveDestination() {
        val directoryState = currentState.safeAs<ChooseExportDirectoryState>() ?: return

        inMemorySettings.update {
            iconPackDestination = directoryState.iconPackDestination
        }
    }

    private fun initDefaultPack() {
        _state.updateState {
            PickedState(
                packEditState = PackEditState(
                    inputFieldState = inputHandler.state.value,
                ),
                useMaterialPack = inMemorySettings.current.useMaterialPack,
            )
        }
    }

    private fun previewIconPackObject() = viewModelScope.launch {
        val editState = currentState.safeAs<PickedState>()?.packEditState ?: return@launch
        val inputFieldState = editState.inputFieldState

        val iconPackCode = IconPackGenerator.create(
            config = IconPackGeneratorConfig(
                packageName = inputFieldState.packageName.text,
                iconPack = IconPack(
                    name = inputFieldState.iconPackName.text,
                    nested = inputFieldState.nestedPacks.map { IconPack(it.inputFieldState.text) },
                ),
                useExplicitMode = inMemorySettings.current.useExplicitMode,
                indentSize = inMemorySettings.current.indentSize,
            ),
        ).content
        _events.emit(PreviewIconPackObject(code = iconPackCode))
    }

    private fun saveIconPack() {
        val pickedState = currentState.safeAs<PickedState>() ?: return

        viewModelScope.launch {
            IconPackWriter.savePack(
                writeToFile = !pickedState.useMaterialPack,
                inMemorySettings = inMemorySettings,
                inputFieldState = pickedState.packEditState.inputFieldState,
            )

            _events.emit(OnSettingsUpdated)
        }
    }

    private fun ValkyriesSettings.toChooseDirectoryState() = ChooseExportDirectoryState(
        iconPackDestination = iconPackDestination,
        predictedPackage = PackageExtractor.getFrom(path = iconPackDestination).orEmpty(),
        nextAvailable = iconPackDestination.isNotEmpty(),
    )
}
