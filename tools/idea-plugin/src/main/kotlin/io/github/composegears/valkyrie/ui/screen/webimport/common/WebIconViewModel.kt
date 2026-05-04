package io.github.composegears.valkyrie.ui.screen.webimport.common

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.WebIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StyledWebIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterByCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WebIconViewModel<Icon : StyledWebIcon, Config : WebIconConfig<Icon>>(
    savedState: MutableSavedState,
    private val provider: WebIconProvider<Icon, Config>,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<WebIconState<Icon>>(
        key = provider.stateKey,
        initialValue = WebIconState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<WebIconEvent>()
    val events = _events.receiveAsFlow()

    private var downloadJob: Job? = null

    init {
        when (val initialState = stateRecord.value) {
            is WebIconState.Success -> {
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
            stateRecord.value = WebIconState.Loading
            runCatching {
                val config = provider.loadConfig()
                val selectedStyle = config.styles.firstOrNull()
                if (config.gridItems.isEmpty()) {
                    stateRecord.value = WebIconState.Error(
                        "No ${provider.providerName} icons found. Check network connection.",
                    )
                    return@launch
                }
                stateRecord.value = WebIconState.Success(
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
                stateRecord.value = WebIconState.Error(
                    "Error loading ${provider.providerName} icons: ${error.message}",
                )
            }
        }
    }

    fun downloadIcon(icon: Icon) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val currentState = stateRecord.value.safeAs<WebIconState.Success<Icon>>() ?: return@launch
            runCatching {
                val svgContent = provider.downloadSvg(icon, currentState.settings, currentState.selectedStyle)
                _events.send(
                    WebIconEvent.IconDownloaded(
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

    private inline fun updateSuccess(crossinline transform: (WebIconState.Success<Icon>) -> WebIconState.Success<Icon>) {
        val current = stateRecord.value
        if (current is WebIconState.Success) {
            stateRecord.value = transform(current)
        }
    }
}

sealed interface WebIconEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : WebIconEvent
}

@Stable
sealed interface WebIconState<out Icon : StyledWebIcon> {
    data object Loading : WebIconState<Nothing>

    data class Error(val message: String) : WebIconState<Nothing>

    @Stable
    data class Success<Icon : StyledWebIcon>(
        val config: WebIconConfig<Icon>,
        val gridItems: List<GridItem> = emptyList(),
        val settings: SizeSettings = SizeSettings(),
        val searchQuery: String = "",
        val selectedCategory: InferredCategory = InferredCategory.All,
        val selectedStyle: IconStyle? = null,
    ) : WebIconState<Icon>
}
