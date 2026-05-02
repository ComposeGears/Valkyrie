package io.github.composegears.valkyrie.ui.screen.webimport.svg.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.ui.placeholder.EmptyPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.sdk.compose.foundation.ObserveEvent
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoryHeaderItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconCard
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconSizeCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.StandardTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data.SvgPreviewColorNormalizer
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data.SvgPreviewCache
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.domain.SvgIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model.SvgIcon
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.jewel.foundation.theme.LocalContentColor
import org.jetbrains.jewel.ui.component.VerticalScrollbar

@Composable
internal fun SvgImportScreen(
    title: String,
    provider: SvgIconProvider,
    onIconDownload: (event: SvgIconEvent.IconDownloaded) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = saveableViewModel {
        SvgIconViewModel(savedState = it, provider = provider)
    }
    val state by viewModel.state.collectAsState()
    val currentOnIconDownloaded by rememberUpdatedState(onIconDownload)

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is SvgIconEvent.IconDownloaded -> currentOnIconDownloaded(event)
        }
    }

    SvgImportScreenUI(
        state = state,
        title = title,
        onBack = onBack,
        onSelectIcon = viewModel::downloadIcon,
        onSelectCategory = viewModel::selectCategory,
        onSelectStyle = viewModel::selectStyle,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSettingsChange = viewModel::updateSettings,
        loadPreviewSvg = viewModel::loadPreviewSvg,
        modifier = modifier,
    )
}

@Composable
private fun SvgImportScreenUI(
    state: SvgState,
    title: String,
    onBack: () -> Unit,
    onSelectIcon: (SvgIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSelectStyle: (IconStyle) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (SizeSettings) -> Unit,
    loadPreviewSvg: suspend (SvgIcon) -> String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = title)
        }
        AnimatedContent(
            targetState = state,
            contentKey = {
                when (it) {
                    is SvgState.Loading -> "loading"
                    is SvgState.Error -> "error"
                    is SvgState.Success -> "success"
                }
            },
        ) { current ->
            when (current) {
                is SvgState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingPlaceholder(text = stringResource("web.import.placeholder.loading"))
                }
                is SvgState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ErrorPlaceholder(message = current.message)
                }
                is SvgState.Success -> SvgIconsContent(
                    state = current,
                    onSelectIcon = onSelectIcon,
                    onSelectCategory = onSelectCategory,
                    onSelectStyle = onSelectStyle,
                    onSearchQueryChange = onSearchQueryChange,
                    onSettingsChange = onSettingsChange,
                    loadPreviewSvg = loadPreviewSvg,
                )
            }
        }
    }
}

@Composable
private fun SvgIconsContent(
    state: SvgState.Success,
    onSelectIcon: (SvgIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSelectStyle: (IconStyle) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (SizeSettings) -> Unit,
    loadPreviewSvg: suspend (SvgIcon) -> String,
) {
    val scope = rememberCoroutineScope()
    var selectedIcon by rememberMutableState<SvgIcon?> { null }
    var isSidePanelOpen by rememberMutableState { false }
    val lazyGridState = rememberLazyGridState()
    val previewCache = remember { SvgPreviewCache<ImageVector>() }
    val shimmer = rememberShimmer()
    val focusManager = LocalFocusManager.current
    val currentLoadPreviewSvg by rememberUpdatedState(loadPreviewSvg)

    LaunchedEffect(state.gridItems, lazyGridState) {
        snapshotFlow {
            lazyGridState.layoutInfo.visibleItemsInfo.maxOfOrNull { it.index } ?: 0
        }.distinctUntilChanged().collectLatest { lastVisibleIndex ->
            prefetchPreviewIcons(
                gridItems = state.gridItems,
                startIndex = lastVisibleIndex + 1,
                previewCache = previewCache,
                loadPreviewSvg = currentLoadPreviewSvg,
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        ) {
            StandardTopActions(
                categories = state.config.categories,
                styles = state.config.styles,
                selectedCategory = state.selectedCategory,
                selectedStyle = state.selectedStyle,
                onToggleCustomization = { isSidePanelOpen = !isSidePanelOpen },
                onSelectStyle = { style ->
                    scope.launch { lazyGridState.scrollToItem(0) }
                    onSelectStyle(style)
                },
                onSelectCategory = { category ->
                    scope.launch { lazyGridState.scrollToItem(0) }
                    onSelectCategory(category)
                },
                onSearchQueryChange = onSearchQueryChange,
            )
            HorizontalDivider()

            if (state.gridItems.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyPlaceholder(message = stringResource("web.import.placeholder.empty"))
                }
            } else {
                SvgIconGrid(
                    gridItems = state.gridItems,
                    lazyGridState = lazyGridState,
                    iconContent = { icon ->
                        IconCard(
                            name = icon.displayName,
                            selected = icon == selectedIcon,
                            onClick = {
                                selectedIcon = icon
                                onSelectIcon(icon)
                            },
                            iconContent = {
                                SvgIconPreview(
                                    icon = icon,
                                    size = state.settings.size,
                                    previewCache = previewCache,
                                    shimmer = shimmer,
                                    loadPreviewSvg = loadPreviewSvg,
                                )
                            },
                        )
                    },
                )
            }
        }

        SidePanel(
            isOpen = isSidePanelOpen,
            onClose = { isSidePanelOpen = false },
            content = {
                IconSizeCustomization(
                    settings = state.settings,
                    onSettingsChange = onSettingsChange,
                    onClose = { isSidePanelOpen = false },
                    sizeLabel = stringResource("web.import.font.customize.size"),
                )
            },
        )
    }
}

@Composable
private fun SvgIconPreview(
    icon: SvgIcon,
    size: Int,
    previewCache: SvgPreviewCache<ImageVector>,
    shimmer: Shimmer,
    loadPreviewSvg: suspend (SvgIcon) -> String,
) {
    val currentLoadPreviewSvg by rememberUpdatedState(loadPreviewSvg)
    val imageVector by produceState<ImageVector?>(initialValue = null, icon.path) {
        value = runCatching {
            loadPreviewImageVector(
                icon = icon,
                previewCache = previewCache,
                loadPreviewSvg = currentLoadPreviewSvg,
            )
        }.getOrNull()
    }

    val loadedImageVector = imageVector
    if (loadedImageVector == null) {
        IconLoadingPlaceholder(shimmer = shimmer)
    } else {
        Image(
            modifier = Modifier.size(size.dp),
            imageVector = loadedImageVector,
            contentDescription = null,
            colorFilter = ColorFilter.tint(LocalContentColor.current),
        )
    }
}

private suspend fun loadPreviewImageVector(
    icon: SvgIcon,
    previewCache: SvgPreviewCache<ImageVector>,
    loadPreviewSvg: suspend (SvgIcon) -> String,
): ImageVector = withContext(Dispatchers.Default) {
    previewCache.getOrLoad(icon.path) {
        val svg = SvgPreviewColorNormalizer.normalize(loadPreviewSvg(icon))
        SvgXmlParser.toIrImageVector(
            parser = ParserType.Kmp,
            value = svg,
            iconName = icon.exportName,
        ).irImageVector.toComposeImageVector()
    }
}

private suspend fun prefetchPreviewIcons(
    gridItems: List<GridItem>,
    startIndex: Int,
    previewCache: SvgPreviewCache<ImageVector>,
    loadPreviewSvg: suspend (SvgIcon) -> String,
) {
    val endIndex = (startIndex + PREVIEW_PREFETCH_GRID_ITEMS).coerceAtMost(gridItems.size)
    for (index in startIndex until endIndex) {
        if (!currentCoroutineContext().isActive) return
        val icon = (gridItems[index] as? IconItem<*>)?.icon as? SvgIcon ?: continue
        runCatching {
            loadPreviewImageVector(
                icon = icon,
                previewCache = previewCache,
                loadPreviewSvg = loadPreviewSvg,
            )
        }
    }
}

private const val PREVIEW_PREFETCH_GRID_ITEMS = 80

@Composable
private fun SvgIconGrid(
    gridItems: List<GridItem>,
    lazyGridState: LazyGridState,
    iconContent: @Composable (SvgIcon) -> Unit,
) {
    IconGrid(state = lazyGridState) {
        items(
            items = gridItems,
            key = { it.id },
            span = { item: GridItem ->
                when (item) {
                    is CategoryHeader -> GridItemSpan(maxLineSpan)
                    is IconItem<*> -> GridItemSpan(1)
                }
            },
        ) { item: GridItem ->
            when (item) {
                is CategoryHeader -> CategoryHeaderItem(title = item.categoryName)
                is IconItem<*> -> iconContent(item.icon as SvgIcon)
            }
        }
    }
}

@Composable
private fun IconGrid(
    state: LazyGridState,
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit,
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            state = state,
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(100.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
        VerticalScrollbar(
            scrollState = state,
            modifier = Modifier.fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, top = 8.dp, bottom = 4.dp),
        )
    }
}

@Composable
private fun IconLoadingPlaceholder(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .shimmer(shimmer = shimmer, cornerRadius = 12.dp),
    )
}
