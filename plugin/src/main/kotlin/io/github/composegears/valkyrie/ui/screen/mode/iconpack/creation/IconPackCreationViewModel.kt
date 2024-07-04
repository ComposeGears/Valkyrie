package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation

import com.composegears.tiamat.TiamatViewModel
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.composegears.valkyrie.processing.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.processing.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.processing.writter.FileWriter
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.domain.validation.IconPackValidationUseCase
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.PackageValidationUseCase
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.InputChange.PackageName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IconPackCreationViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val inputHandler = InputHandler(inMemorySettings)

    private val _state = MutableStateFlow(IconPackModeState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<IconPackCreationEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            inputHandler.state.collect { inputFieldState ->
                _state.updateState {
                    copy(
                        inputFieldState = inputFieldState,
                        nestedPacks = inputFieldState.nestedPacks,
                        nextAvailable = inputFieldState.noErrors(),
                        packPreview = if (inputFieldState.noErrors()) {
                            IconPackGenerator(
                                config = IconPackGeneratorConfig(
                                    packageName = inputFieldState.packageName.text,
                                    iconPackName = inputFieldState.iconPackName.text,
                                    subPacks = inputFieldState.nestedPacks.map { it.inputFieldState.text }
                                )
                            ).generate().content
                        } else {
                            ""
                        }
                    )
                }
            }
        }
    }

    fun onValueChange(change: InputChange) = viewModelScope.launch {
        inputHandler.handleInput(change)
    }

    fun addNestedPack() = inputHandler.addPack()
    fun removeNestedPack(nestedPack: NestedPack) = inputHandler.removeNestedPack(nestedPack)

    fun saveSettings() {
        viewModelScope.launch {
            val fieldState = state.value.inputFieldState
            inMemorySettings.updatePackageName(fieldState.packageName.text)
            inMemorySettings.updateIconPackName(fieldState.iconPackName.text)
            inMemorySettings.updateNestedPack(fieldState.nestedPacks.map { it.inputFieldState.text })
            inMemorySettings.updateMode(Mode.IconPack)

            val iconPack = IconPackGenerator(
                config = IconPackGeneratorConfig(
                    packageName = inMemorySettings.current.packageName,
                    iconPackName = inMemorySettings.current.iconPackName,
                    subPacks = inMemorySettings.current.nestedPacks
                )
            ).generate()

            FileWriter.writeToFile(
                content = iconPack.content,
                outDirectory = inMemorySettings.current.iconPackDestination,
                fileName = iconPack.name
            )

            writeAction {
                VirtualFileManager.getInstance().syncRefresh()
            }
            _events.emit(IconPackCreationEvent.NavigateToNextScreen)
        }
    }
}

private class InputHandler(private val inMemorySettings: InMemorySettings) {

    private val packageValidationUseCase = PackageValidationUseCase()
    private val iconPackValidationUseCase = IconPackValidationUseCase()

    private val valkyriesSettings: ValkyriesSettings
        get() = inMemorySettings.current

    private val _state = MutableStateFlow(
        InputFieldState(
            iconPackName = InputState(text = valkyriesSettings.iconPackName),
            packageName = InputState(text = valkyriesSettings.packageName),
            nestedPacks = valkyriesSettings.nestedPacks.mapIndexed { index, nestedPack ->
                NestedPack(
                    id = index.toString(),
                    inputFieldState = InputState(text = nestedPack)
                )
            }
        )
    )
    val state = _state.asStateFlow()

    fun addPack() {
        _state.updateState {
            copy(
                nestedPacks = nestedPacks + NestedPack(
                    id = nestedPacks.size.toString(),
                    inputFieldState = InputState(
                        text = "",
                        validationResult = ValidationResult.None
                    )
                )
            )
        }
    }

    fun removeNestedPack(nestedPack: NestedPack) {
        _state.updateState {
            copy(nestedPacks = nestedPacks.filterNot { it.id == nestedPack.id })
        }
    }

    suspend fun handleInput(change: InputChange) {
        when (change) {
            is IconPackName -> {
                _state.updateState {
                    copy(
                        iconPackName = iconPackName.copy(
                            text = change.text,
                            validationResult = ValidationResult.Success
                        )
                    )
                }
            }
            is PackageName -> {
                _state.updateState {
                    copy(
                        packageName = packageName.copy(
                            text = change.text,
                            validationResult = ValidationResult.Success
                        )
                    )
                }
            }
            is InputChange.NestedPackName -> {
                _state.updateState {
                    copy(
                        nestedPacks = nestedPacks.map {
                            if (it.id == change.id) {
                                it.copy(inputFieldState = it.inputFieldState.copy(text = change.text))
                            } else {
                                it
                            }
                        }
                    )
                }
            }
        }
        validate()
    }

    private suspend fun validate() {
        val inputFieldState = _state.value
        val packageResult = packageValidationUseCase(inputFieldState.packageName.text)
        val iconPackResult = iconPackValidationUseCase(inputFieldState.iconPackName.text)
        val nestedPackResults = inputFieldState.nestedPacks.map {
            iconPackValidationUseCase(it.inputFieldState.text)
        }

        _state.updateState {
            copy(
                iconPackName = iconPackName.copy(validationResult = iconPackResult),
                packageName = packageName.copy(validationResult = packageResult),
                nestedPacks = nestedPacks.mapIndexed { index, nestedPack ->
                    nestedPack.copy(
                        inputFieldState = nestedPack.inputFieldState.copy(validationResult = nestedPackResults[index])
                    )
                }
            )
        }
    }
}

sealed interface IconPackCreationEvent {
    data object NavigateToNextScreen : IconPackCreationEvent
}

sealed interface InputChange {
    data class PackageName(val text: String) : InputChange
    data class IconPackName(val text: String) : InputChange
    data class NestedPackName(val id: String, val text: String) : InputChange
}

data class InputFieldState(
    val iconPackName: InputState,
    val packageName: InputState,
    val nestedPacks: List<NestedPack>
) {
    fun noErrors() = iconPackName.validationResult !is ValidationResult.Error &&
            packageName.validationResult !is ValidationResult.Error &&
            nestedPacks.all { it.inputFieldState.validationResult !is ValidationResult.Error && it.inputFieldState.text.isNotEmpty() }
}

data class IconPackModeState(
    val nextAvailable: Boolean = true,
    val inputFieldState: InputFieldState = InputFieldState(
        iconPackName = InputState(),
        packageName = InputState(),
        nestedPacks = emptyList()
    ),
    val nestedPacks: List<NestedPack> = emptyList(),
    val packPreview: String = ""
)

data class NestedPack(
    val id: String,
    val inputFieldState: InputState
)
