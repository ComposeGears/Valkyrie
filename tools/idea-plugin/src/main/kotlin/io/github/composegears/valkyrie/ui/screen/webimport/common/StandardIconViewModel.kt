package io.github.composegears.valkyrie.ui.screen.webimport.common

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterByCategory
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

class StandardIconViewModel(
    savedState: MutableSavedState,
    private val provider: StandardIconProvider,
) : ViewModel() {

    private val fontCache = mutableMapOf<String?, FontByteArray>()

    private val stateRecord = savedState.recordOf<StandardState>(
        key = provider.stateKey,
        initialValue = StandardState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<StandardIconEvent>()
    val events = _events.receiveAsFlow()

    private var downloadJob: Job? = null
    private var fontLoadJob: Job? = null
    private var prefetchJob: Job? = null

    init {
        when (val initialState = stateRecord.value) {
            is StandardState.Success -> {
                val restoredStyleId = initialState.selectedStyle?.id
                val normalizedSelectedStyle = initialState.selectedStyle
                    ?.takeIf { selected -> initialState.config.styles.any { it.id == selected.id } }
                    ?: initialState.config.styles.defaultStyle()
                val restoredFont = initialState.fontByteArray
                    ?.takeIf { restoredStyleId == normalizedSelectedStyle?.id }

                stateRecord.value = initialState.copy(
                    selectedStyle = normalizedSelectedStyle,
                    fontByteArray = restoredFont,
                    gridItems = initialState.config.filterByCategory(
                        category = initialState.selectedCategory,
                        style = normalizedSelectedStyle,
                        searchQuery = initialState.searchQuery,
                    ),
                )

                val styleKey = normalizedSelectedStyle?.id
                restoredFont?.let { fontCache[styleKey] = it }

                provider.onStyleChanged(normalizedSelectedStyle)

                if (restoredFont == null) {
                    downloadFont(normalizedSelectedStyle)
                }

                prefetchStyleFonts(
                    styles = initialState.config.styles,
                    selectedStyleId = normalizedSelectedStyle?.id,
                )
            }
            else -> loadConfig()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            stateRecord.value = StandardState.Loading

            runCatching {
                val config = provider.loadConfig()
                val selectedStyle = config.styles.defaultStyle()

                if (config.gridItems.isEmpty()) {
                    stateRecord.value = StandardState.Error(
                        "No ${provider.providerName} icons found. Check network connection.",
                    )
                    return@launch
                }

                stateRecord.value = StandardState.Success(
                    config = config,
                    gridItems = config.filterByCategory(
                        category = InferredCategory.All,
                        style = selectedStyle,
                        searchQuery = "",
                    ),
                    settings = SizeSettings(size = provider.persistentSize),
                    selectedStyle = selectedStyle,
                )
                provider.onStyleChanged(selectedStyle)
                downloadFont(selectedStyle)
                prefetchStyleFonts(
                    styles = config.styles,
                    selectedStyleId = selectedStyle?.id,
                )
            }.onFailure { error ->
                stateRecord.value = StandardState.Error(
                    "Error loading ${provider.providerName} icons: ${error.message}",
                )
            }
        }
    }

    fun downloadFont(style: IconStyle? = null) {
        fontLoadJob?.cancel()
        fontLoadJob = viewModelScope.launch {
            val resolvedStyle = style
                ?: (stateRecord.value as? StandardState.Success)?.selectedStyle
            val styleKey = resolvedStyle?.id
            val cachedFont = fontCache[styleKey]

            if (cachedFont == null) {
                updateSuccess { it.copy(fontByteArray = null) }
                // Yield so Compose renders the skeleton before font bytes arrive.
                // Without this, providers with in-memory caches (suspendLazy) return
                // instantly — both state updates are batched into one frame and the
                // skeleton is never visible.
                yield()
                runCatching {
                    val bytes = provider.loadFontBytes(resolvedStyle)
                    fontCache[styleKey] = bytes
                    updateSuccess { it.copy(fontByteArray = bytes) }
                }.onFailure { error ->
                    if (error is CancellationException) throw error
                    updateSuccess { it.copy(fontByteArray = null) }
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
                val svgContent = provider.downloadSvg(icon, currentState.settings)

                _events.send(
                    StandardIconEvent.IconDownloaded(
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
                    gridItems = state.config.filterByCategory(
                        category = category,
                        style = state.selectedStyle,
                        searchQuery = state.searchQuery,
                    ),
                )
            }
        }
    }

    fun selectStyle(style: IconStyle) {
        viewModelScope.launch(Dispatchers.Default) {
            val cachedFont = withContext(Dispatchers.Main.immediate) {
                fontCache[style.id]
            }
            provider.onStyleChanged(style)
            updateSuccess { state ->
                state.copy(
                    selectedStyle = style,
                    fontByteArray = cachedFont,
                    gridItems = state.config.filterByCategory(
                        category = state.selectedCategory,
                        style = style,
                        searchQuery = state.searchQuery,
                    ),
                )
            }

            if (cachedFont == null) {
                downloadFont(style)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updateSuccess { state ->
                state.copy(
                    searchQuery = query,
                    gridItems = state.config.filterByCategory(
                        category = state.selectedCategory,
                        style = state.selectedStyle,
                        searchQuery = query,
                    ),
                )
            }
        }
    }

    fun updateSettings(settings: SizeSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            provider.updatePersistentSize(settings.size)
            updateSuccess { state ->
                state.copy(settings = settings)
            }
        }
    }

    private fun prefetchStyleFonts(styles: List<IconStyle>, selectedStyleId: String?) {
        prefetchJob?.cancel()
        prefetchJob = viewModelScope.launch {
            styles
                .asSequence()
                .filter { it.id != selectedStyleId }
                .forEach { style ->
                    if (fontCache[style.id] == null) {
                        runCatching {
                            provider.loadFontBytes(style)
                        }.onSuccess { bytes ->
                            fontCache[style.id] = bytes
                        }
                    }
                }
        }
    }

    private fun List<IconStyle>.defaultStyle(): IconStyle? {
        return firstOrNull { it.id == "regular" } ?: firstOrNull()
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
        val selectedStyle: IconStyle? = null,
        val searchQuery: String = "",
        val settings: SizeSettings = SizeSettings(),
        val fontByteArray: FontByteArray? = null,
    ) : StandardState

    data class Error(val message: String) : StandardState
}
