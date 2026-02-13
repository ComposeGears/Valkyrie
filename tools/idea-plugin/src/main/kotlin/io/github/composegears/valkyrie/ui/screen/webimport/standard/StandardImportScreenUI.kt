package io.github.composegears.valkyrie.ui.screen.webimport.standard

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.saveableViewModel
import dev.tclement.fonticons.ExperimentalFontIconsApi
import dev.tclement.fonticons.FontIcon
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.fonticons.rememberVariableIconFont
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.ui.placeholder.EmptyPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconCard
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconGrid
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconLoadingPlaceholder
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconSizeCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.standard.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ui.StandardTopActions
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.LocalContentColor

@Composable
internal fun StandardImportScreen(
    title: String,
    provider: StandardIconProvider,
    onIconDownload: (event: StandardIconEvent.IconDownloaded) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = saveableViewModel {
        StandardIconViewModel(savedState = it, provider = provider)
    }
    val state by viewModel.state.collectAsState()
    val currentOnIconDownloaded by rememberUpdatedState(onIconDownload)

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is StandardIconEvent.IconDownloaded -> {
                        currentOnIconDownloaded(it)
                    }
                }
            }
            .launchIn(this)
    }

    StandardImportScreenUI(
        state = state,
        title = title,
        fontAlias = provider.fontAlias,
        onBack = onBack,
        onSelectIcon = viewModel::downloadIcon,
        onSelectCategory = viewModel::selectCategory,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSettingsChange = viewModel::updateSettings,
        modifier = modifier,
    )
}

@Composable
private fun StandardImportScreenUI(
    state: StandardState,
    title: String,
    fontAlias: String,
    onBack: () -> Unit,
    onSelectIcon: (StandardIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (SizeSettings) -> Unit,
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
                    is StandardState.Loading -> "loading"
                    is StandardState.Error -> "error"
                    is StandardState.Success -> "success"
                }
            },
        ) { current ->
            when (current) {
                is StandardState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LoadingPlaceholder(text = stringResource("web.import.placeholder.loading"))
                    }
                }
                is StandardState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ErrorPlaceholder(message = current.message)
                    }
                }
                is StandardState.Success -> {
                    IconsContent(
                        state = current,
                        fontAlias = fontAlias,
                        onSelectIcon = onSelectIcon,
                        onSelectCategory = onSelectCategory,
                        onSearchQueryChange = onSearchQueryChange,
                        onSettingsChange = onSettingsChange,
                    )
                }
            }
        }
    }
}

@Composable
private fun IconsContent(
    state: StandardState.Success,
    fontAlias: String,
    onSelectIcon: (StandardIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (SizeSettings) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var selectedIcon by rememberMutableState<StandardIcon?> { null }
    var isSidePanelOpen by rememberMutableState { false }
    val lazyGridState = rememberLazyGridState()
    val shimmer = rememberShimmer()

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        },
                    )
                },
        ) {
            StandardTopActions(
                categories = state.config.categories,
                selectedCategory = state.selectedCategory,
                onToggleCustomization = { isSidePanelOpen = !isSidePanelOpen },
                onSelectCategory = { category ->
                    scope.launch {
                        lazyGridState.scrollToItem(0)
                    }
                    onSelectCategory(category)
                },
                onSearchQueryChange = onSearchQueryChange,
            )
            HorizontalDivider()

            when {
                state.gridItems.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        EmptyPlaceholder(message = stringResource("web.import.placeholder.empty"))
                    }
                }
                state.fontByteArray == null -> {
                    StandardIconGrid(
                        gridItems = state.gridItems,
                        lazyGridState = lazyGridState,
                        iconContent = { icon ->
                            StandardIconStub(
                                icon = icon,
                                shimmer = shimmer,
                            )
                        },
                    )
                }
                else -> {
                    val iconFont = rememberStandardFont(
                        font = state.fontByteArray.bytes,
                        alias = fontAlias,
                    )
                    val iconSizeDp = state.settings.size.dp

                    ProvideIconParameters(
                        iconFont = iconFont,
                        tint = LocalContentColor.current,
                        weight = FontWeight.W400,
                    ) {
                        StandardIconGrid(
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
                                        FontIcon(
                                            modifier = Modifier.size(iconSizeDp),
                                            icon = Char(icon.codepoint),
                                            contentDescription = null,
                                        )
                                    },
                                )
                            },
                        )
                    }
                }
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
                    sizeLabel = "web.import.font.customize.size",
                )
            },
        )
    }
}

@Composable
private fun StandardIconGrid(
    gridItems: List<GridItem>,
    lazyGridState: LazyGridState,
    iconContent: @Composable (StandardIcon) -> Unit,
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
                is CategoryHeader -> CategoryHeader(title = item.categoryName)
                is IconItem<*> -> iconContent(item.icon as StandardIcon)
            }
        }
    }
}

@Composable
private fun StandardIconStub(
    icon: StandardIcon,
    shimmer: Shimmer,
) {
    IconCard(
        name = icon.displayName,
        selected = false,
        onClick = { /* No-op during loading */ },
        iconContent = {
            IconLoadingPlaceholder(shimmer = shimmer)
        },
    )
}

@OptIn(ExperimentalFontIconsApi::class)
@Composable
private fun rememberStandardFont(
    font: ByteArray,
    alias: String,
): IconFont = rememberVariableIconFont(
    alias = alias,
    data = font,
    weights = arrayOf(FontWeight.W400),
)
