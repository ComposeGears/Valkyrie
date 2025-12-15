package io.github.composegears.valkyrie.ui.screen.webimport.lucide

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
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.util.ThreadSafeLruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LucideViewModel(savedState: MutableSavedState) : ViewModel() {

    private val lucideUseCase = inject(LucideModule.lucideUseCase)

    private val iconVectorCache = ThreadSafeLruCache<String, ImageVector?>(maxSize = 300)
    private val iconLoadMutex = Mutex()

    private val lucideRecord = savedState.recordOf<LucideState>(
        key = "lucide",
        initialValue = LucideState.Loading,
    )
    val lucideState = lucideRecord.asStateFlow()

    private val _events = MutableSharedFlow<LucideEvent>()
    val events = _events.asSharedFlow()

    private var iconLoadJob: Job? = null
    private val iconLoadJobs = mutableMapOf<String, Job>()

    companion object {
        private val LOG = Logger.getInstance(LucideViewModel::class.java)
    }

    init {
        when (lucideRecord.value) {
            is LucideState.Success -> {
                // Already loaded
            }
            else -> loadConfig()
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
        iconLoadJob?.cancel()
        iconLoadJob = viewModelScope.launch {
            val state = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch

            val rawSvg = lucideUseCase.getRawSvg(icon.name)
            val svgContent = lucideUseCase.applyCustomizations(rawSvg, state.settings)

            _events.emit(
                LucideEvent.IconDownloaded(
                    svgContent = svgContent,
                    name = IconNameFormatter.format(icon.displayName),
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

    fun updateSettings(settings: LucideSettings) {
        viewModelScope.launch(Dispatchers.Default) {
            iconLoadJobs.values.forEach { it.cancel() }
            iconLoadJobs.clear()

            val currentState = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch

            val loadedIconNames = currentState.loadedIcons.keys
                .map { cacheKey -> cacheKey.substringBefore("-") }
                .toSet()

            updateSuccess { state ->
                state.copy(settings = settings)
            }

            loadedIconNames.forEach { iconName ->
                currentState.config.iconsByName[iconName]?.let { icon ->
                    reParseIconFromRepository(icon, settings)
                }
            }
        }
    }

    private fun reParseIconFromRepository(icon: LucideIcon, settings: LucideSettings) {
        iconLoadJobs[icon.name]?.cancel()

        val job = viewModelScope.launch {
            val cacheKey = buildIconCacheKey(icon.name, settings)

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

        iconLoadJobs[icon.name] = job
        job.invokeOnCompletion { iconLoadJobs.remove(icon.name) }
    }

    /**
     * Load an icon for display purposes (preview in grid).
     *
     * Uses mutex to prevent race conditions when multiple coroutines try to load the same icon.
     * Tracks jobs per cache key to allow cancellation when settings change.
     */
    fun loadIconForDisplay(icon: LucideIcon) {
        val job = viewModelScope.launch {
            val state = lucideRecord.value.safeAs<LucideState.Success>() ?: return@launch
            val cacheKey = buildIconCacheKey(icon.name, state.settings)

            iconLoadMutex.withLock {
                // Skip if already loaded successfully or currently loading
                val currentState = state.loadedIcons[cacheKey]
                if (currentState is IconLoadState.Success || currentState is IconLoadState.Loading) {
                    return@launch
                }

                iconVectorCache[cacheKey]?.let { cachedVector ->
                    updateSuccess {
                        it.copy(
                            loadedIcons = it.loadedIcons + (cacheKey to IconLoadState.Success(cachedVector)),
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

        iconLoadJobs[icon.name] = job
        job.invokeOnCompletion { iconLoadJobs.remove(icon.name) }
    }

    private suspend fun parseAndCacheIcon(
        iconName: String,
        customizedSvg: String,
        cacheKey: String,
    ) {
        val parserOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            value = customizedSvg,
            iconName = iconName,
        )

        val imageVector = parserOutput.irImageVector.toComposeImageVector()

        iconLoadMutex.withLock {
            iconVectorCache[cacheKey] = imageVector
        }

        updateSuccess {
            it.copy(loadedIcons = it.loadedIcons + (cacheKey to IconLoadState.Success(imageVector)))
        }
    }

    private suspend fun handleIconParseError(iconName: String, cacheKey: String, error: Throwable) {
        LOG.error("Failed to parse icon '$iconName'", error)
        iconLoadMutex.withLock {
            iconVectorCache[cacheKey] = null
        }
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
        return "$iconName-${settings.strokeWidth}-${settings.size}-${settings.absoluteStrokeWidth}-$colorKey"
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
    ) : LucideState

    data class Error(val message: String) : LucideState
}
