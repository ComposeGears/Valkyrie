package io.github.composegears.valkyrie.ui.screen.mode.iconpack.preview

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IconPackPreviewViewModel(
    private val inMemorySettings: InMemorySettings
) : TiamatViewModel() {

    private val _state = MutableStateFlow("")
    val state = _state.asStateFlow()

    init {
        _state.updateState {
            IconPackGenerator(
                config = IconPackGeneratorConfig(
                    packageName = inMemorySettings.current.packageName,
                    iconPackName = inMemorySettings.current.iconPackName,
                    subPacks = inMemorySettings.current.nestedPacks
                )
            ).generate()
        }
    }
}