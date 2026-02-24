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
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.material.di.MaterialSymbolsModule
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialConfig
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialIconFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MaterialSymbolsViewModel(savedState: MutableSavedState) : ViewModel() {

    private val fontCache = mutableMapOf<MaterialIconFontFamily, FontByteArray>()
    private val materialSymbolsConfigUseCase = inject(MaterialSymbolsModule.materialSymbolsConfigUseCase)
    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val materialRecord = savedState.recordOf<MaterialState>(
        key = "materialSymbols",
        initialValue = MaterialState.Loading,
    )
    val materialState = materialRecord.asStateFlow()

    private val _events = Channel<MaterialEvent>()
    val events = _events.receiveAsFlow()

    private var currentFontFamily: MaterialIconFontFamily = MaterialIconFontFamily.Outlined
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
                val initialSettings = inMemorySettings.readState {
                    MaterialFontSettings(
                        fill = materialFontFill,
                        weight = materialFontWeight,
                        grade = materialFontGrade,
                        opticalSize = materialFontOpticalSize,
                    )
                }
                val config = materialSymbolsConfigUseCase.loadConfig()
                materialRecord.value = MaterialState.Success(
                    config = config,
                    gridItems = config.gridItems.toGridItems(),
                    fontSettings = initialSettings,
                )
                downloadFont(MaterialIconFontFamily.Outlined)
            }.onFailure {
                materialRecord.value = MaterialState.Error("Error loading Material Symbols config: ${it.message}")
            }
        }
    }

    fun downloadFont(iconFontFamily: MaterialIconFontFamily) {
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

    fun updateFontSettings(fontSettings: MaterialFontSettings) {
        viewModelScope.launch {
            inMemorySettings.update {
                materialFontFill = fontSettings.fill
                materialFontWeight = fontSettings.weight
                materialFontGrade = fontSettings.grade
                materialFontOpticalSize = fontSettings.opticalSize
            }
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
        val fontSettings: MaterialFontSettings,
        val iconFontFamily: MaterialIconFontFamily = MaterialIconFontFamily.Outlined,
        val fontByteArray: FontByteArray? = null,
    ) : MaterialState

    data class Error(val message: String) : MaterialState
}
