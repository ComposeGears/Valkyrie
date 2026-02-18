package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.parser.unified.util.PackageExtractor
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.util.IconPackWriter
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction.SaveDestination
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction.SavePack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackEvent.FinishSetup
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState.ChooseDestinationDirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState.PickedState
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

class MaterialPackViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val inputHandler = MaterialInputHandler(inMemorySettings.current)

    private val _events = Channel<MaterialPackEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow<MaterialPackState>(ChooseDestinationDirectoryState())
    val state = _state.asStateFlow()

    private val currentState: MaterialPackState
        get() = state.value

    init {
        inputHandler.state
            .onEach { inputFieldState ->
                _state.updateState {
                    if (this is PickedState) {
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

    fun onAction(action: MaterialPackAction) {
        when (action) {
            is SelectDestinationFolder -> updateDestinationPath(action.path)
            is SaveDestination -> {
                saveDestination()
                initMaterialPack()
            }
            is SavePack -> saveIconPack()
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
        }
    }

    private fun initMaterialPack() {
        _state.updateState {
            PickedState(inputFieldState = inputHandler.state.value)
        }
    }

    private fun saveIconPack() {
        val pickedState = currentState.safeAs<PickedState>() ?: return

        viewModelScope.launch {
            inMemorySettings.update {
                flatPackage = true
            }
            withContext(Dispatchers.IO) {
                IconPackWriter.savePack(
                    writeToFile = false,
                    inMemorySettings = inMemorySettings,
                    inputFieldState = pickedState.inputFieldState,
                )
            }
            _events.send(FinishSetup)
        }
    }
}
