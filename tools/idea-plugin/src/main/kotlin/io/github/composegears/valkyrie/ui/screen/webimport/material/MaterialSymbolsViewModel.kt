package io.github.composegears.valkyrie.ui.screen.webimport.material

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.webimport.material.di.MaterialSymbolsModule
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialConfig
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialGridItem
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.toGridItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaterialSymbolsViewModel : ViewModel() {

    private val materialSymbolsConfigUseCase = inject(MaterialSymbolsModule.materialSymbolsConfigUseCase)

    private val fontCache = mutableMapOf<IconFontFamily, FontByteArray>()

    private val _materialState = MutableStateFlow<MaterialState>(MaterialState.Loading)
    val materialState = _materialState.asStateFlow()

    private val _events = MutableSharedFlow<MaterialEvent>()
    val events = _events.asSharedFlow()

    private var currentFontFamily: IconFontFamily = IconFontFamily.OUTLINED
    private var iconLoadJob: Job? = null

    init {
        loadConfig()
    }

    private fun loadConfig() {
        viewModelScope.launch {
            _materialState.updateState { MaterialState.Loading }

            runCatching {
                val config = materialSymbolsConfigUseCase.loadConfig()
                _materialState.updateState {
                    MaterialState.Success(
                        config = config,
                        gridItems = config.gridItems.toGridItems(),
                    )
                }
                downloadFont(IconFontFamily.OUTLINED)
            }.onFailure {
                _materialState.updateState { MaterialState.Error("Error loading Material Symbols config: ${it.message}") }
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
                        _materialState.updateState {
                            MaterialState.Error("Error loading Material Symbols font: ${e.message}")
                        }
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
            val state = _materialState.value.safeAs<MaterialState.Success>() ?: return@launch

            val svgContent = materialSymbolsConfigUseCase.loadIcon(
                name = icon.originalName,
                fontFamily = state.iconFontFamily.fontFamily,
                fontSettings = state.fontSettings,
            )

            _events.emit(
                MaterialEvent.IconDownloaded(
                    svgContent = svgContent,
                    name = icon.name,
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
    ): List<MaterialGridItem> {
        val categoryFiltered = when (category) {
            Category.All -> config.gridItems
            else -> config.gridItems.filterKeys { it == category }
        }

        return if (searchQuery.isBlank()) {
            categoryFiltered.toGridItems()
        } else {
            val filtered = categoryFiltered.mapValues { (_, icons) ->
                icons.filter { icon ->
                    icon.name.contains(searchQuery, ignoreCase = true)
                }
            }.filterValues { it.isNotEmpty() }
            filtered.toGridItems()
        }
    }

    fun updateFontSettings(fontSettings: FontSettings) {
        viewModelScope.launch {
            updateSuccess { state ->
                state.copy(fontSettings = fontSettings)
            }
        }
    }

    private inline fun updateSuccess(crossinline transform: (MaterialState.Success) -> MaterialState.Success) {
        val current = _materialState.value
        if (current is MaterialState.Success) {
            _materialState.updateState { transform(current) }
        }
    }
}

sealed interface MaterialEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : MaterialEvent
}

sealed interface MaterialState {
    object Loading : MaterialState
    data class Success(
        val config: MaterialConfig,
        val gridItems: List<MaterialGridItem> = emptyList(),
        val selectedCategory: Category = Category.All,
        val fontSettings: FontSettings = FontSettings(),
        val iconFontFamily: IconFontFamily = IconFontFamily.OUTLINED,
        val fontByteArray: FontByteArray? = null,
    ) : MaterialState

    data class Error(val message: String) : MaterialState
}
