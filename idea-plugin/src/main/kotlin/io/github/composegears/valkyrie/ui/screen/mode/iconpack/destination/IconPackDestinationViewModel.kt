package io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class IconPackDestinationViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val settings = inMemorySettings.current

    private val _state = MutableStateFlow(
        IconPackDestinationState(
            nextButtonEnabled = settings.iconPackDestination.isNotEmpty(),
            iconPackDestination = settings.iconPackDestination
        )
    )
    val state = _state.asStateFlow()

    fun updateDestination(path: Path) {
        _state.updateState {
            copy(
                iconPackDestination = path.absolutePathString(),
                nextButtonEnabled = true
            )
        }
    }

    fun saveSettings() {
        inMemorySettings.updateIconPackDestination(state.value.iconPackDestination)
    }
}

data class IconPackDestinationState(
    val nextButtonEnabled: Boolean = false,
    val iconPackDestination: String = ""
)