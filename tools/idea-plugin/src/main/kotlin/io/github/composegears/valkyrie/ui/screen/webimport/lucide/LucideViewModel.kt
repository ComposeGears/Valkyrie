package io.github.composegears.valkyrie.ui.screen.webimport.lucide

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import com.intellij.openapi.diagnostic.Logger
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.di.LucideModule
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideConfig
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.toGridItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class LucideViewModel(savedState: MutableSavedState) : ViewModel() {

    private val lucideUseCase = inject(LucideModule.lucideUseCase)

    private var fontCache: FontByteArray? = null

    private val lucideRecord = savedState.recordOf<LucideState>(
        key = "lucide",
        initialValue = LucideState.Loading,
    )
    val lucideState = lucideRecord.asStateFlow()

    private val _events = MutableSharedFlow<LucideEvent>()
    val events = _events.asSharedFlow()

    private var downloadJob: Job? = null
    private var fontLoadJob: Job? = null
    private val searchQueryFlow = MutableStateFlow("")

    companion object {
        private val LOG = Logger.getInstance(LucideViewModel::class.java)
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    init {
        when (val initialState = lucideRecord.value) {
            is LucideState.Success if initialState.fontByteArray == null -> downloadFont()
            else -> loadConfig()
        }

        viewModelScope.launch(Dispatchers.Default) {
            searchQueryFlow
                .debounce(SEARCH_DEBOUNCE_MS)
                .collect { query ->
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
    }

    private fun loadConfig() {
        viewModelScope.launch {
            lucideRecord.value = LucideState.Loading

            runCatching {
                val config = lucideUseCase.loadConfig()

                if (config.iconsByName.isEmpty()) {
                    lucideRecord.value = LucideState.Error("No Lucide icons found. Check network connection.")
                    return@launch
                }

                lucideRecord.value = LucideState.Success(
                    config = config,
                    gridItems = config.gridItems.toGridItems(),
                )
                downloadFont()
            }.onFailure { error ->
                LOG.error("Error loading Lucide icons", error)
                lucideRecord.value = LucideState.Error("Error loading Lucide icons: ${error.message}")
            }
        }
    }

    fun downloadFont() {
        fontLoadJob?.cancel()
        fontLoadJob = viewModelScope.launch {
            val cachedFont = fontCache
            if (cachedFont == null) {
                updateSuccess { it.copy(fontByteArray = null) }
                runCatching {
                    val bytes = lucideUseCase.loadFontBytes()
                    fontCache = bytes
                    updateSuccess { it.copy(fontByteArray = bytes) }
                }.onFailure { error ->
                    LOG.error("Error loading Lucide font", error)
                }
            } else {
                updateSuccess { it.copy(fontByteArray = cachedFont) }
            }
        }
    }

    fun downloadIcon(icon: LucideIcon) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val state = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch

            runCatching {
                val svgContent = lucideUseCase.downloadSvg(icon, state.settings)

                _events.emit(
                    LucideEvent.IconDownloaded(
                        svgContent = svgContent,
                        name = IconNameFormatter.format(icon.displayName),
                    ),
                )
            }.onFailure { error ->
                LOG.error("Failed to download icon '${icon.name}'", error)
            }
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
        searchQueryFlow.value = query
    }

    fun updateSettings(settings: LucideSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(settings = settings)
            }
        }
    }

    private fun filterGridItems(
        config: LucideConfig,
        category: Category,
        searchQuery: String = "",
    ): List<GridItem> {
        val categoryFiltered = when (category) {
            Category.All -> config.gridItems
            else -> config.gridItems.filterKeys { it.id == category.id }
        }

        return if (searchQuery.isBlank()) {
            categoryFiltered.toGridItems()
        } else {
            categoryFiltered
                .mapValues { (_, icons) ->
                    icons.filter { icon ->
                        icon.name.contains(searchQuery, ignoreCase = true) ||
                            icon.displayName.contains(searchQuery, ignoreCase = true) ||
                            icon.tags.any { it.contains(searchQuery, ignoreCase = true) }
                    }
                }
                .filterValues { it.isNotEmpty() }
                .toGridItems()
        }
    }

    private inline fun updateSuccess(crossinline transform: (LucideState.Success) -> LucideState.Success) {
        val current = lucideRecord.value
        if (current is LucideState.Success) {
            lucideRecord.value = transform(current)
        }
    }
}

sealed interface LucideEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : LucideEvent
}

@Stable
sealed interface LucideState {
    data object Loading : LucideState

    @Stable
    data class Success(
        val config: LucideConfig,
        val gridItems: List<GridItem> = emptyList(),
        val selectedCategory: Category = Category.All,
        val settings: LucideSettings = LucideSettings(),
        val fontByteArray: FontByteArray? = null,
    ) : LucideState

    data class Error(val message: String) : LucideState
}
