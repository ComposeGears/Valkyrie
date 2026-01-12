package io.github.composegears.valkyrie.ui.screen.webimport.lucide

import androidx.collection.LruCache
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import com.intellij.openapi.diagnostic.Logger
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.di.LucideModule
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideConfig
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.toGridItems
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
class LucideViewModel(savedState: MutableSavedState) : ViewModel() {

    private val lucideUseCase = inject(LucideModule.lucideUseCase)

    // Wrapper to handle nullable ImageVector values in LruCache (which requires non-null types)
    private data class CachedIcon(val imageVector: ImageVector?)

    private val iconVectorCache = LruCache<String, CachedIcon>(300)
    private val iconLoadMutexes = ConcurrentHashMap<String, Mutex>()

    private val lucideRecord = savedState.recordOf<LucideState>(
        key = "lucide",
        initialValue = LucideState.Loading,
    )
    val lucideState = lucideRecord.asStateFlow()

    private val _events = MutableSharedFlow<LucideEvent>()
    val events = _events.asSharedFlow()

    private var downloadJob: Job? = null
    private val iconLoadJobs = ConcurrentHashMap<String, Job>()
    private val searchQueryFlow = MutableStateFlow("")

    companion object {
        private val LOG = Logger.getInstance(LucideViewModel::class.java)
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    init {
        when (lucideRecord.value) {
            is LucideState.Success -> {
                // Already loaded
            }
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
                lucideRecord.value = LucideState.Success(
                    config = config,
                    gridItems = config.gridItems.toGridItems(),
                )
            }.onFailure { error ->
                LOG.error("Error loading Lucide icons", error)
                lucideRecord.value = LucideState.Error("Error loading Lucide icons: ${error.message}")
            }
        }
    }

    fun downloadIcon(icon: LucideIcon) {
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch {
            val state = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch

            runCatching {
                val rawSvg = lucideUseCase.getRawSvg(icon.name)
                val svgContent = lucideUseCase.applyCustomizations(rawSvg, state.settings)

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
            iconLoadJobs.values.forEach { it.cancel() }
            iconLoadJobs.clear()

            val iconsToReload = mutableListOf<Pair<LucideIcon, String>>()
            val oldCacheKeys = mutableSetOf<String>()

            updateSuccess { state ->
                state.loadedIcons.keys.forEach { cacheKey ->
                    extractIconNameFromCacheKey(cacheKey)?.let { iconName ->
                        state.config.iconsByName[iconName]?.let { icon ->
                            iconsToReload.add(icon to cacheKey)
                            oldCacheKeys.add(cacheKey)
                        }
                    }
                }

                state.copy(settings = settings)
            }

            oldCacheKeys.forEach { oldKey ->
                iconVectorCache.remove(oldKey)
            }

            iconsToReload.forEach { (icon, _) ->
                reParseIconFromRepository(icon, settings)
            }
        }
    }

    private fun reParseIconFromRepository(icon: LucideIcon, settings: LucideSettings) {
        val cacheKey = buildIconCacheKey(icon.name, settings)
        iconLoadJobs[cacheKey]?.cancel()

        val job = viewModelScope.launch {

            val currentState = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch
            if (currentState.loadedIcons[cacheKey] is IconLoadState.Success) {
                return@launch
            }
            runCatching {
                val rawSvg = lucideUseCase.getRawSvg(icon.name)
                val customizedSvg = lucideUseCase.applyCustomizations(rawSvg, settings)

                parseAndCacheIcon(icon.name, customizedSvg, cacheKey)
            }.onFailure { error ->
                handleIconParseError(icon.name, cacheKey, error)
            }
        }

        iconLoadJobs[cacheKey] = job
        job.invokeOnCompletion { iconLoadJobs.remove(cacheKey) }
    }

    /**
     * Load an icon for display purposes (preview in grid).
     *
     * Uses per-icon mutex to prevent race conditions while allowing parallel loading of different icons.
     * Tracks jobs per cache key to allow cancellation when settings change.
     *
     * @return Job that can be cancelled when the icon is no longer visible
     */
    fun loadIconForDisplay(icon: LucideIcon): Job {
        val job = viewModelScope.launch {
            val state = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch
            val cacheKey = buildIconCacheKey(icon.name, state.settings)

            iconLoadJobs[cacheKey]?.cancel()

            val mutex = iconLoadMutexes.computeIfAbsent(cacheKey) { Mutex() }

            mutex.withLock {
                val currentState = state.loadedIcons[cacheKey]
                if (currentState is IconLoadState.Success || currentState is IconLoadState.Loading) {
                    return@launch
                }

                iconVectorCache.get(cacheKey)?.imageVector?.let { cachedVector ->
                    val successState = IconLoadState.Success(cachedVector)
                    updateSuccess {
                        it.copy(
                            loadedIcons = it.loadedIcons + (cacheKey to successState),
                            iconNameIndex = it.iconNameIndex + (icon.name to successState),
                        )
                    }
                    return@launch
                }

                updateSuccess {
                    it.copy(loadedIcons = it.loadedIcons + (cacheKey to IconLoadState.Loading))
                }
            }

            runCatching {
                val rawSvg = lucideUseCase.getRawSvg(icon.name)
                val customizedSvg = lucideUseCase.applyCustomizations(rawSvg, state.settings)

                parseAndCacheIcon(icon.name, customizedSvg, cacheKey)
            }.onFailure { error ->
                handleIconParseError(icon.name, cacheKey, error)
            }
        }

        iconLoadJobs[buildIconCacheKey(icon.name, lucideRecord.value.safeAs<LucideState.Success>()?.settings ?: LucideSettings())] = job
        job.invokeOnCompletion {
            iconLoadJobs.remove(buildIconCacheKey(icon.name, lucideRecord.value.safeAs<LucideState.Success>()?.settings ?: LucideSettings()))
        }

        return job
    }

    private suspend fun parseAndCacheIcon(
        iconName: String,
        customizedSvg: String,
        cacheKey: String,
    ) {
        val imageVector = withContext(Dispatchers.Default) {
            val parserOutput = SvgXmlParser.toIrImageVector(
                parser = ParserType.Jvm,
                value = customizedSvg,
                iconName = iconName,
            )
            parserOutput.irImageVector.toComposeImageVector()
        }

        iconVectorCache.put(cacheKey, CachedIcon(imageVector))

        val successState = IconLoadState.Success(imageVector)
        updateSuccess {
            it.copy(
                loadedIcons = it.loadedIcons + (cacheKey to successState),
                iconNameIndex = it.iconNameIndex + (iconName to successState),
            )
        }
    }

    private suspend fun handleIconParseError(iconName: String, cacheKey: String, error: Throwable) {
        LOG.error("Failed to parse icon '$iconName'", error)
        iconVectorCache.put(cacheKey, CachedIcon(null))
        updateSuccess {
            it.copy(loadedIcons = it.loadedIcons + (cacheKey to IconLoadState.Error))
        }
    }

    fun getIconCacheKey(iconName: String, settings: LucideSettings): String {
        return buildIconCacheKey(iconName, settings)
    }

    private fun buildIconCacheKey(iconName: String, settings: LucideSettings): String {
        val colorKey = if (settings.color != Color.Unspecified) {
            settings.color.value.toString()
        } else {
            "unspecified"
        }
        // Use || delimiter to separate icon name from settings (can't appear in icon names)
        return "$iconName||${settings.strokeWidth}-${settings.size}-${settings.absoluteStrokeWidth}-$colorKey"
    }

    private fun extractIconNameFromCacheKey(cacheKey: String): String? {
        return cacheKey.substringBefore("||", "").takeIf { it.isNotEmpty() }
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

sealed interface IconLoadState {
    data object Loading : IconLoadState
    data class Success(val imageVector: ImageVector) : IconLoadState
    data object Error : IconLoadState
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
        val loadedIcons: Map<String, IconLoadState> = emptyMap(),
        internal val iconNameIndex: Map<String, IconLoadState> = emptyMap(),
    ) : LucideState {
        fun getLatestSuccessfulState(iconName: String): IconLoadState? {
            return iconNameIndex[iconName]
        }
    }

    data class Error(val message: String) : LucideState
}
