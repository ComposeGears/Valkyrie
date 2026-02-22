package io.github.composegears.valkyrie.ui.screen.webimport.material

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.material.di.MaterialSymbolsModule
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialConfig
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MaterialSymbolsViewModel(savedState: MutableSavedState) : ViewModel() {

    private val fontCache = mutableMapOf<IconFontFamily, FontByteArray>()
    private val materialSymbolsConfigUseCase = inject(MaterialSymbolsModule.materialSymbolsConfigUseCase)

    private val materialRecord = savedState.recordOf<MaterialState>(
        key = "materialSymbols",
        initialValue = MaterialState.Loading,
    )
    val materialState = materialRecord.asStateFlow()

    private val _events = Channel<MaterialEvent>()
    val events = _events.receiveAsFlow()

    private var currentFontFamily: IconFontFamily = IconFontFamily.OUTLINED
    private var iconLoadJob: Job? = null

    init {
        when (val current = materialRecord.value) {
            is MaterialState.Success -> if (current.fontByteArray == null) {
                downloadFont(current.iconFontFamily)
            }
            else -> loadConfig()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            materialRecord.value = MaterialState.Loading

            runCatching {
                val config = materialSymbolsConfigUseCase.loadConfig()
                materialRecord.value = MaterialState.Success(
                    config = config,
                    gridItems = config.gridItems.toGridItems(),
                )
                downloadFont(IconFontFamily.OUTLINED)
            }.onFailure {
                materialRecord.value = MaterialState.Error("Error loading Material Symbols config: ${it.message}")
            }
        }
    }

    fun downloadFont(iconFontFamily: IconFontFamily) {
        currentFontFamily = iconFontFamily
        viewModelScope.launch {
            val cachedFont = fontCache[iconFontFamily]

            if (cachedFont == null) {
                if (currentFontFamily == iconFontFamily) {
                    updateSuccess { it.copy(fontByteArray = null, iconFontFamily = iconFontFamily) }
                }

                runCatching {
                    val font = materialSymbolsConfigUseCase.loadFont(iconFontFamily)
                    // Always cache the downloaded font
                    fontCache[iconFontFamily] = font

                    if (currentFontFamily == iconFontFamily) {
                        updateSuccess {
                            it.copy(
                                fontByteArray = font,
                                iconFontFamily = iconFontFamily,
                            )
                        }
                    }
                }.onFailure { e ->
                    if (currentFontFamily == iconFontFamily) {
                        materialRecord.value = MaterialState.Error("Error loading Material Symbols font: ${e.message}")
                    }
                }
            } else {
                updateSuccess {
                    it.copy(
                        fontByteArray = cachedFont,
                        iconFontFamily = iconFontFamily,
                    )
                }
            }
        }
    }

    fun downloadIcon(icon: IconModel) {
        iconLoadJob?.cancel()
        iconLoadJob = viewModelScope.launch {
            val state = materialRecord.value.safeAs<MaterialState.Success>() ?: return@launch

            val svgContent = materialSymbolsConfigUseCase.loadIcon(
                name = icon.originalName,
                fontFamily = state.iconFontFamily.fontFamily,
                fontSettings = state.fontSettings,
            )

            _events.send(
                MaterialEvent.IconDownloaded(
                    svgContent = svgContent,
                    name = IconNameFormatter.format(icon.name),
                ),
            )
        }
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    selectedCategory = category,
                    gridItems = filterGridItems(
                        config = state.config,
                        category = category,
                    ),
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    gridItems = filterGridItems(
                        config = state.config,
                        category = state.selectedCategory,
                        searchQuery = query,
                    ),
                )
            }
        }
    }

    private fun filterGridItems(
        config: MaterialConfig,
        category: Category,
        searchQuery: String = "",
    ): List<GridItem> = config
        .gridItems
        .filterGridItems(
            category = category.takeUnless { it == Category.All },
            searchQuery = searchQuery,
        )

    fun updateFontSettings(fontSettings: FontSettings) {
        viewModelScope.launch {
            updateSuccess { state ->
                state.copy(fontSettings = fontSettings)
            }
        }
    }

    private inline fun updateSuccess(crossinline transform: (MaterialState.Success) -> MaterialState.Success) {
        val current = materialRecord.value
        if (current is MaterialState.Success) {
            materialRecord.value = transform(current)
        }
    }
}

sealed interface MaterialEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : MaterialEvent
}

@Stable
sealed interface MaterialState {
    data object Loading : MaterialState

    @Stable
    data class Success(
        val config: MaterialConfig,
        val gridItems: List<GridItem> = emptyList(),
        val selectedCategory: Category = Category.All,
        val fontSettings: FontSettings = FontSettings(),
        val iconFontFamily: IconFontFamily = IconFontFamily.OUTLINED,
        val fontByteArray: FontByteArray? = null,
    ) : MaterialState

    data class Error(val message: String) : MaterialState
}
