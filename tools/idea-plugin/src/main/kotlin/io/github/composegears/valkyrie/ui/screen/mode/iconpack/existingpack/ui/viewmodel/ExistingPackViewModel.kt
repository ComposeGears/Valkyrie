package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.intellij.psi.iconpack.IconPackInfo
import io.github.composegears.valkyrie.sdk.intellij.psi.iconpack.IconPackPsiParser
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util.IconPackWriter
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.AddNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.RemoveNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.SavePack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.SelectKotlinFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackEvent.OnSettingsUpdated
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackEvent.PreviewIconPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState.ChooserState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState.ExistingPackEditState
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExistingPackViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val inputHandler = ExistingPackInputHandler()

    private val currentState: ExistingPackModeState
        get() = state.value

    private val _state = MutableStateFlow<ExistingPackModeState>(ChooserState)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ExistingPackEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            inputHandler.state.collect { inputFieldState ->
                _state.updateState {
                    if (this is ExistingPackEditState) {
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
    }

    fun onValueChange(inputChange: InputChange) = viewModelScope.launch {
        inputHandler.handleInput(inputChange)
    }

    fun onAction(existingPackAction: ExistingPackAction) {
        when (existingPackAction) {
            is SelectKotlinFile -> onChooseFile(existingPackAction.path, existingPackAction.project)
            is AddNestedPack -> inputHandler.addNestedPack()
            is RemoveNestedPack -> inputHandler.removeNestedPack(existingPackAction.nestedPack)
            is PreviewPackObject -> previewIconPackObject()
            is SavePack -> saveIconPack()
        }
    }

    private fun onChooseFile(path: Path, project: Project) {
        val iconPackInfo = IconPackPsiParser.extractIconPack(path, project) ?: return

        val inputFieldState = iconPackInfo.toInputFieldState()
        inputHandler.updateState(inputFieldState)

        _state.updateState {
            ExistingPackEditState(
                packEditState = PackEditState(inputFieldState = inputFieldState),
                importDirectory = path.parent.absolutePathString(),
            )
        }
    }

    private fun previewIconPackObject() = viewModelScope.launch {
        val editState = currentState.safeAs<ExistingPackEditState>()?.packEditState ?: return@launch
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
        val editState = currentState.safeAs<ExistingPackEditState>() ?: return
        val inputFieldState = editState.packEditState.inputFieldState

        viewModelScope.launch {
            inMemorySettings.update {
                iconPackDestination = editState.importDirectory
            }
            IconPackWriter.savePack(
                inMemorySettings = inMemorySettings,
                inputFieldState = inputFieldState,
            )
            _events.emit(OnSettingsUpdated)
        }
    }

    private fun IconPackInfo.toInputFieldState(): InputFieldState {
        return InputFieldState(
            iconPackName = InputState(text = iconPack, enabled = false),
            packageName = InputState(text = packageName, enabled = false),
            nestedPacks = nestedPacks.mapIndexed { index, name ->
                NestedPack(
                    id = index.toString(),
                    inputFieldState = InputState(text = name, enabled = false),
                )
            },
        )
    }
}
