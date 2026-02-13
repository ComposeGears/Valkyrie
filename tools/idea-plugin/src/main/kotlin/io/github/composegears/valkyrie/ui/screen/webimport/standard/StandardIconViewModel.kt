package io.github.composegears.valkyrie.ui.screen.webimport.standard

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import com.intellij.openapi.diagnostic.Logger
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.standard.util.filterByCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StandardIconViewModel(
    savedState: MutableSavedState,
    private val provider: StandardIconProvider,
) : ViewModel() {

    private var fontCache: FontByteArray? = null

    private val stateRecord = savedState.recordOf<StandardState>(
        key = provider.stateKey,
        initialValue = StandardState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<StandardIconEvent>()
    val events = _events.receiveAsFlow()

    private var downloadJob: Job? = null
    private var fontLoadJob: Job? = null

    private val log = Logger.getInstance("StandardIconViewModel-${provider.providerName}")

    init {
        when (val initialState = stateRecord.value) {
            is StandardState.Success if initialState.fontByteArray == null -> downloadFont()
            else -> loadConfig()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            stateRecord.value = StandardState.Loading

            runCatching {
                val config = provider.loadConfig()

                if (config.iconsByName.isEmpty()) {
                    stateRecord.value = StandardState.Error(
                        "No ${provider.providerName} icons found. Check network connection.",
                    )
                    return@launch
                }

                stateRecord.value = StandardState.Success(
                    config = config,
                    gridItems = config.gridItems.toGridItems(),
                )
                downloadFont()
            }.onFailure { error ->
                log.error("Error loading ${provider.providerName} icons", error)
                stateRecord.value = StandardState.Error(
                    "Error loading ${provider.providerName} icons: ${error.message}",
                )
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
                    val bytes = provider.loadFontBytes()
                    fontCache = bytes
                    updateSuccess { it.copy(fontByteArray = bytes) }
                }.onFailure { error ->
                    log.error("Error loading ${provider.providerName} font", error)
                }
            } else {
                updateSuccess { it.copy(fontByteArray = cachedFont) }
            }
        }
    }

    fun downloadIcon(icon: StandardIcon) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val currentState = stateRecord.value.safeAs<StandardState.Success>() ?: return@launch

            runCatching {
                val svgContent = provider.downloadSvg(icon.name, currentState.settings)

                _events.send(
                    StandardIconEvent.IconDownloaded(
                        svgContent = svgContent,
                        name = IconNameFormatter.format(icon.displayName),
                    ),
                )
            }.onFailure { error ->
                log.error("Failed to download icon '${icon.name}'", error)
            }
        }
    }

    fun selectCategory(category: InferredCategory) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    selectedCategory = category,
                    gridItems = state.config.filterByCategory(category),
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    gridItems = state.config.filterByCategory(
                        category = state.selectedCategory,
                        searchQuery = query,
                    ),
                )
            }
        }
    }

    fun updateSettings(settings: SizeSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(settings = settings)
            }
        }
    }

    private inline fun updateSuccess(crossinline transform: (StandardState.Success) -> StandardState.Success) {
        val current = stateRecord.value
        if (current is StandardState.Success) {
            stateRecord.value = transform(current)
        }
    }
}

sealed interface StandardIconEvent {
    data class IconDownloaded(
        val svgContent: String,
        val name: String,
    ) : StandardIconEvent
}

@Stable
sealed interface StandardState {
    data object Loading : StandardState

    @Stable
    data class Success(
        val config: StandardIconConfig,
        val gridItems: List<GridItem> = emptyList(),
        val selectedCategory: InferredCategory = InferredCategory.All,
        val settings: SizeSettings = SizeSettings(),
        val fontByteArray: FontByteArray? = null,
    ) : StandardState

    data class Error(val message: String) : StandardState
}
