package io.github.composegears.valkyrie.ui.screen.webimport.lucide

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.compose.core.animation.rememberShimmer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoryHeader as CategoryHeaderComponent
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.EmptyContent
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.ErrorContent
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconCard
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconGrid
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.LoadingContent
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui.LucideCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui.LucideIconDisplay
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui.LucideTopActions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

val LucideImportScreen by navDestination<Unit> {
    val navController = navController()
    val parentNavController = navController.parent
    val viewModel = saveableViewModel { LucideViewModel(savedState = it) }
    val state by viewModel.lucideState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is LucideEvent.IconDownloaded -> {
                        parentNavController?.navigate(
                            dest = SimpleConversionScreen,
                            navArgs = TextSource(
                                name = it.name,
                                text = it.svgContent,
                            ),
                        )
                    }
                }
            }
            .launchIn(this)
    }

    LucideImportScreenUI(
        state = state,
        onBack = navController::back,
        onSelectIcon = viewModel::downloadIcon,
        onSelectCategory = viewModel::selectCategory,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSettingsChange = viewModel::updateSettings,
        onLoadIconForDisplay = viewModel::loadIconForDisplay,
        getIconCacheKey = viewModel::getIconCacheKey,
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
    onLoadIconForDisplay: (LucideIcon) -> Unit,
    getIconCacheKey: (String, LucideSettings) -> String,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle("Lucide Icons Import")
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
                is LucideState.Loading -> LoadingContent(message = "Loading Lucide icons...")
                is LucideState.Error -> ErrorContent(message = current.message)
                is LucideState.Success -> {
                    IconsContent(
                        state = current,
                        onSelectIcon = onSelectIcon,
                        onSelectCategory = onSelectCategory,
                        onSearchQueryChange = onSearchQueryChange,
                        onSettingsChange = onSettingsChange,
                        onLoadIconForDisplay = onLoadIconForDisplay,
                        getIconCacheKey = getIconCacheKey,
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
    onLoadIconForDisplay: (LucideIcon) -> Unit,
    getIconCacheKey: (String, LucideSettings) -> String,
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

            if (state.gridItems.isEmpty()) {
                EmptyContent()
            } else {
                IconGrid(state = lazyGridState) {
                    items(
                        items = state.gridItems,
                        key = { it.id },
                        span = { item ->
                            when (item) {
                                is CategoryHeader -> GridItemSpan(maxLineSpan)
                                is IconItem<*> -> GridItemSpan(1)
                            }
                        },
                    ) { item ->
                        when (item) {
                            is CategoryHeader -> CategoryHeaderComponent(title = item.categoryName)
                            is IconItem<*> -> {
                                val lucideIcon = item.icon as LucideIcon
                                val iconCacheKey = getIconCacheKey(lucideIcon.name, state.settings)
                                var iconLoadState = state.loadedIcons[iconCacheKey]

                                if (iconLoadState !is IconLoadState.Success) {
                                    iconLoadState = state.loadedIcons.entries
                                        .firstOrNull {
                                            it.key.startsWith("${lucideIcon.name}-") &&
                                                it.value is IconLoadState.Success
                                        }
                                        ?.value
                                }

                                IconCard(
                                    name = lucideIcon.displayName,
                                    selected = lucideIcon == selectedIcon,
                                    onClick = {
                                        selectedIcon = lucideIcon
                                        onSelectIcon(lucideIcon)
                                    },
                                    iconContent = {
                                        LucideIconDisplay(
                                            icon = lucideIcon,
                                            iconCacheKey = iconCacheKey,
                                            iconLoadState = iconLoadState,
                                            onLoadIcon = onLoadIconForDisplay,
                                            shimmer = shimmer,
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    },
                                )
                            }
                        }
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
