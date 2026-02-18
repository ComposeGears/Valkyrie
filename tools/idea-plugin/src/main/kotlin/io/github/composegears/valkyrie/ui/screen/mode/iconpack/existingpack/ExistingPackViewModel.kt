package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

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
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.util.IconPackWriter
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState.ExistingPackEditState
import io.github.composegears.valkyrie.util.extension.resolveKtFile
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExistingPackViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val inputHandler = ExistingPackInputHandler()

    private val currentState: ExistingPackModeState
        get() = state.value

    private val _state = MutableStateFlow<ExistingPackModeState>(ExistingPackModeState.ChooserState)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<ExistingPackEvent>()
    val events = _events.asSharedFlow()

    init {
        inputHandler.state
            .onEach { inputFieldState ->
                _state.updateState {
                    if (this is ExistingPackEditState) {
                        copy(inputFieldState = inputFieldState)
                    } else {
                        this
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onValueChange(inputChange: InputChange) = inputHandler.handleInput(inputChange)

    fun onAction(existingPackAction: ExistingPackAction) {
        when (existingPackAction) {
            is ExistingPackAction.SelectKotlinFile -> onChooseFile(existingPackAction.path, existingPackAction.project)
            is ExistingPackAction.AddNestedPack -> inputHandler.addNestedPack()
            is ExistingPackAction.RemoveNestedPack -> inputHandler.removeNestedPack(existingPackAction.nestedPack)
            is ExistingPackAction.PreviewPackObject -> previewIconPackObject()
            is ExistingPackAction.SavePack -> saveIconPack()
        }
    }

    private fun onChooseFile(path: Path, project: Project) = viewModelScope.launch {
        val ktFile = path.resolveKtFile(project) ?: return@launch
        val iconPackInfo = IconPackPsiParser.parse(ktFile) ?: return@launch

        val inputFieldState = iconPackInfo.toInputFieldState()
        inputHandler.updateState(inputFieldState)

        _state.updateState {
            ExistingPackEditState(
                inputFieldState = inputFieldState,
                importDirectory = path.parent.absolutePathString(),
            )
        }
    }

    private fun previewIconPackObject() = viewModelScope.launch {
        val inputFieldState = currentState.safeAs<ExistingPackEditState>()?.inputFieldState ?: return@launch

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

        _events.emit(ExistingPackEvent.PreviewIconPackObject(code = iconPackCode))
    }

    private fun saveIconPack() {
        val editState = currentState.safeAs<ExistingPackEditState>() ?: return
        val inputFieldState = editState.inputFieldState

        viewModelScope.launch {
            inMemorySettings.update {
                iconPackDestination = editState.importDirectory
                flatPackage = false
            }
            withContext(Dispatchers.IO) {
                IconPackWriter.savePack(
                    inMemorySettings = inMemorySettings,
                    inputFieldState = inputFieldState,
                )
            }
            _events.emit(ExistingPackEvent.OnSettingsUpdated)
        }
    }

    private fun IconPackInfo.toInputFieldState(): InputFieldState {
        return InputFieldState(
            iconPackName = InputState(text = iconPack.name, enabled = false),
            packageName = InputState(text = packageName, enabled = false),
            nestedPacks = iconPack.nested.mapIndexed { index, pack ->
                NestedPack(
                    id = index.toString(),
                    inputFieldState = InputState(text = pack.name, enabled = false),
                )
            },
        )
    }
}
