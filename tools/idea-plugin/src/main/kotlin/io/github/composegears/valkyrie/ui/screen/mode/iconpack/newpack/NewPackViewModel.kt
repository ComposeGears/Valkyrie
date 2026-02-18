package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.util.IconPackWriter
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState.ChooseDestinationDirectoryState
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.invariantSeparatorsPathString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPackViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val inputHandler = NewPackInputHandler(inMemorySettings.current)

    private val _events = Channel<NewPackEvent>()
    val events = _events.receiveAsFlow()

    private val currentState: NewPackModeState
        get() = state.value

    private val _state = MutableStateFlow<NewPackModeState>(ChooseDestinationDirectoryState())
    val state = _state.asStateFlow()

    init {
        inputHandler.state
            .onEach { inputFieldState ->
                _state.updateState {
                    if (this is NewPackModeState.PickedState) {
                        copy(inputFieldState = inputFieldState)
                    } else {
                        this
                    }
                }
            }
            .launchIn(viewModelScope)

        inMemorySettings.settings
            .onEach { settings ->
                inputHandler.invalidate(settings)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NewPackAction) {
        when (action) {
            is NewPackAction.SelectDestinationFolder -> updateDestinationPath(action.path)
            is NewPackAction.SaveDestination -> {
                saveDestination()
                initDefaultPack()
            }
            is NewPackAction.AddNestedPack -> inputHandler.addNestedPack()
            is NewPackAction.RemoveNestedPack -> inputHandler.removeNestedPack(action.nestedPack)
            is NewPackAction.PreviewPackObject -> previewIconPackObject()
            is NewPackAction.SavePack -> saveIconPack()
        }
    }

    fun onValueChange(change: InputChange) = inputHandler.handleInput(change)

    private fun updateDestinationPath(path: Path) {
        _state.updateState {
            ChooseDestinationDirectoryState(
                directoryState = DirectoryState(
                    iconPackDestination = path.absolutePathString(),
                    predictedPackage = PackageExtractor.getFrom(path.invariantSeparatorsPathString).orEmpty(),
                    nextAvailable = true,
                ),
            )
        }
    }

    private fun saveDestination() {
        val destinationDirectoryState = currentState.safeAs<ChooseDestinationDirectoryState>() ?: return

        inMemorySettings.update {
            iconPackDestination = destinationDirectoryState.directoryState.iconPackDestination
            flatPackage = false
        }
    }

    private fun initDefaultPack() {
        _state.updateState {
            NewPackModeState.PickedState(inputFieldState = inputHandler.state.value)
        }
    }

    private fun previewIconPackObject() = viewModelScope.launch {
        val inputFieldState = currentState.safeAs<NewPackModeState.PickedState>()?.inputFieldState ?: return@launch

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
        _events.send(NewPackEvent.PreviewIconPackObject(code = iconPackCode))
    }

    private fun saveIconPack() {
        val pickedState = currentState.safeAs<NewPackModeState.PickedState>() ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                IconPackWriter.savePack(
                    inMemorySettings = inMemorySettings,
                    inputFieldState = pickedState.inputFieldState,
                )
            }
            _events.send(NewPackEvent.OnSettingsUpdated)
        }
    }
}
