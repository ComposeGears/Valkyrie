package io.github.composegears.valkyrie.ui.screen.webimport.svg.common

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterByCategory
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain.SvgIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIconConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SvgIconViewModel(
    savedState: MutableSavedState,
    private val provider: SvgIconProvider,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<SvgState>(
        key = provider.stateKey,
        initialValue = SvgState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<SvgIconEvent>()
    val events = _events.receiveAsFlow()

    private var downloadJob: Job? = null

    init {
        when (val initialState = stateRecord.value) {
            is SvgState.Success -> {
                val selectedStyle = initialState.selectedStyle
                    ?.takeIf { selected -> initialState.config.styles.any { it.id == selected.id } }
                    ?: initialState.config.styles.firstOrNull()

                stateRecord.value = initialState.copy(
                    selectedStyle = selectedStyle,
                    gridItems = initialState.config.filterByCategory(
                        category = initialState.selectedCategory,
                        style = selectedStyle,
                        searchQuery = initialState.searchQuery,
                    ),
                )
            }
            else -> loadConfig()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            stateRecord.value = SvgState.Loading
            runCatching {
                val config = provider.loadConfig()
                val selectedStyle = config.styles.firstOrNull()
                if (config.gridItems.isEmpty()) {
                    stateRecord.value = SvgState.Error("No ${provider.providerName} icons found. Check network connection.")
                    return@launch
                }
                stateRecord.value = SvgState.Success(
                    config = config,
                    gridItems = config.filterByCategory(
                        category = InferredCategory.All,
                        style = selectedStyle,
                        searchQuery = "",
                    ),
                    settings = SizeSettings(size = provider.persistentSize),
                    selectedStyle = selectedStyle,
                )
            }.onFailure { error ->
                stateRecord.value = SvgState.Error("Error loading ${provider.providerName} icons: ${error.message}")
            }
        }
    }

    fun downloadIcon(icon: SvgIcon) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val currentState = stateRecord.value.safeAs<SvgState.Success>() ?: return@launch
            runCatching {
                val svgContent = provider.downloadSvg(icon, currentState.settings)
                _events.send(
                    SvgIconEvent.IconDownloaded(
                        svgContent = svgContent,
                        name = IconNameFormatter.format(icon.exportName),
                    ),
                )
            }
        }
    }

    fun selectCategory(category: InferredCategory) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    selectedCategory = category,
                    gridItems = state.config.filterByCategory(category, state.selectedStyle, state.searchQuery),
                )
            }
        }
    }

    fun selectStyle(style: IconStyle) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    selectedStyle = style,
                    gridItems = state.config.filterByCategory(state.selectedCategory, style, state.searchQuery),
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    searchQuery = query,
                    gridItems = state.config.filterByCategory(state.selectedCategory, state.selectedStyle, query),
                )
            }
        }
    }

    fun updateSettings(settings: SizeSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            provider.updatePersistentSize(settings.size)
            updateSuccess { it.copy(settings = settings) }
        }
    }

    suspend fun loadPreviewSvg(icon: SvgIcon): String = provider.loadPreviewSvg(icon)

    private inline fun updateSuccess(crossinline transform: (SvgState.Success) -> SvgState.Success) {
        val current = stateRecord.value
        if (current is SvgState.Success) {
            stateRecord.value = transform(current)
        }
    }
}

sealed interface SvgIconEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : SvgIconEvent
}

@Stable
sealed interface SvgState {
    data object Loading : SvgState

    data class Error(val message: String) : SvgState

    data class Success(
        val config: SvgIconConfig,
        val gridItems: List<GridItem>,
        val settings: SizeSettings,
        val searchQuery: String = "",
        val selectedCategory: InferredCategory = InferredCategory.All,
        val selectedStyle: IconStyle? = null,
    ) : SvgState
}
