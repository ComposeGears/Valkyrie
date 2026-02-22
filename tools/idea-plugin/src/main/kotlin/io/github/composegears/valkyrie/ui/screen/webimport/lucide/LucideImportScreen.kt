package io.github.composegears.valkyrie.ui.screen.webimport.lucide

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
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
import io.github.composegears.valkyrie.sdk.compose.foundation.ObserveEvent
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconCard
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconGrid
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconLoadingPlaceholder
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui.LucideCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui.LucideTopActions
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.LocalContentColor

val LucideImportScreen by navDestination<Unit> {
    val navController = navController()
    val parentNavController = navController.parent
    val viewModel = saveableViewModel { LucideViewModel(savedState = it) }
    val state by viewModel.lucideState.collectAsState()

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is LucideEvent.IconDownloaded -> {
                parentNavController?.navigate(
                    dest = SimpleConversionScreen,
                    navArgs = TextSource(
                        name = event.name,
                        text = event.svgContent,
                    ),
                )
            }
        }
    }

    LucideImportScreenUI(
        state = state,
        onBack = navController::back,
        onSelectIcon = viewModel::downloadIcon,
        onSelectCategory = viewModel::selectCategory,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSettingsChange = viewModel::updateSettings,
    )
}

@Composable
private fun LucideImportScreenUI(
    state: LucideState,
    onBack: () -> Unit,
    onSelectIcon: (LucideIcon) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (LucideSettings) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("web.import.title.lucide"))
        }
        AnimatedContent(
            targetState = state,
            contentKey = {
                when (it) {
                    is LucideState.Loading -> "loading"
                    is LucideState.Error -> "error"
                    is LucideState.Success -> "success"
                }
            },
        ) { current ->
            when (current) {
                is LucideState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        LoadingPlaceholder(text = stringResource("web.import.placeholder.loading"))
                    }
                }
                is LucideState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ErrorPlaceholder(message = current.message)
                    }
                }
                is LucideState.Success -> {
                    IconsContent(
                        state = current,
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
    state: LucideState.Success,
    onSelectIcon: (LucideIcon) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (LucideSettings) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var selectedIcon by rememberMutableState<LucideIcon?> { null }
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
            LucideTopActions(
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
                    LucideIconGrid(
                        gridItems = state.gridItems,
                        lazyGridState = lazyGridState,
                        iconContent = { lucideIcon ->
                            LucideIconStub(
                                icon = lucideIcon,
                                shimmer = shimmer,
                            )
                        },
                    )
                }
                else -> {
                    val iconFont = rememberLucideFont(
                        font = state.fontByteArray.bytes,
                    )
                    val iconSizeDp = state.settings.size.dp

                    ProvideIconParameters(
                        iconFont = iconFont,
                        tint = LocalContentColor.current,
                        weight = FontWeight.W400,
                    ) {
                        LucideIconGrid(
                            gridItems = state.gridItems,
                            lazyGridState = lazyGridState,
                            iconContent = { lucideIcon ->
                                IconCard(
                                    name = lucideIcon.displayName,
                                    selected = lucideIcon == selectedIcon,
                                    onClick = {
                                        selectedIcon = lucideIcon
                                        onSelectIcon(lucideIcon)
                                    },
                                    iconContent = {
                                        FontIcon(
                                            modifier = Modifier.size(iconSizeDp),
                                            icon = Char(lucideIcon.codepoint),
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
                LucideCustomization(
                    settings = state.settings,
                    onClose = { isSidePanelOpen = false },
                    onSettingsChange = onSettingsChange,
                )
            },
        )
    }
}

@Composable
private fun LucideIconGrid(
    gridItems: List<GridItem>,
    lazyGridState: LazyGridState,
    iconContent: @Composable (LucideIcon) -> Unit,
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
                is IconItem<*> -> iconContent(item.icon as LucideIcon)
            }
        }
    }
}

@Composable
private fun LucideIconStub(
    icon: LucideIcon,
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
private fun rememberLucideFont(
    font: ByteArray,
): IconFont = rememberVariableIconFont(
    alias = "lucide",
    data = font,
    weights = arrayOf(FontWeight.W400),
)
