package io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IconPackDestinationViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val settings = inMemorySettings.current

    private val _state = MutableStateFlow(
        IconPackDestinationState(
            nextButtonEnabled = settings.iconPackDestination.isNotEmpty(),
            destination = settings.iconPackDestination
        )
    )
    val state = _state.asStateFlow()

    fun updateDestination(destination: String) {
        _state.updateState {
            copy(
                destination = destination,
                nextButtonEnabled = true
            )
        }
    }

    fun saveSettings() {
        inMemorySettings.updateIconPackDestination(state.value.destination)
    }
}

data class IconPackDestinationState(
    val nextButtonEnabled: Boolean = false,
    val destination: String = ""
)