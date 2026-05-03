package io.github.composegears.valkyrie.ui.screen.webimport.common

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontVariation
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
import io.github.composegears.valkyrie.sdk.compose.foundation.ObserveEvent
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.StandardIconProvider
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.FontByteArray
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font.VariableFontConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoryHeaderItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconCard
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconSizeCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.StandardTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.ZOOM_DEFAULT_SCALE
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.ZoomFloatingBar
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch
import org.jetbrains.jewel.foundation.theme.LocalContentColor
import org.jetbrains.jewel.ui.component.VerticalScrollbar

@Composable
internal fun StandardImportScreen(
    title: String,
    provider: StandardIconProvider,
    onIconDownload: (event: StandardIconEvent.IconDownloaded) -> Unit,
    onBack: () -> Unit,
    customizationContent: (@Composable (onClose: () -> Unit) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val viewModel = saveableViewModel {
        StandardIconViewModel(savedState = it, provider = provider)
    }
    val state by viewModel.state.collectAsState()
    val variableFontConfig by provider.variableFontConfig.collectAsState()
    val currentOnIconDownloaded by rememberUpdatedState(onIconDownload)

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is StandardIconEvent.IconDownloaded -> {
                currentOnIconDownloaded(event)
            }
        }
    }

    StandardImportScreenUI(
        state = state,
        title = title,
        fontAlias = provider.fontAlias,
        resolveFontWeight = provider::resolveFontWeight,
        variableFontConfig = variableFontConfig,
        customizationContent = customizationContent,
        onBack = onBack,
        onSelectIcon = viewModel::downloadIcon,
        onSelectCategory = viewModel::selectCategory,
        onSelectStyle = viewModel::selectStyle,
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
    resolveFontWeight: (IconStyle?) -> FontWeight,
    variableFontConfig: VariableFontConfig?,
    customizationContent: (@Composable (onClose: () -> Unit) -> Unit)?,
    onBack: () -> Unit,
    onSelectIcon: (StandardIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSelectStyle: (IconStyle) -> Unit,
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
                        resolveFontWeight = resolveFontWeight,
                        variableFontConfig = variableFontConfig,
                        customizationContent = customizationContent,
                        onSelectIcon = onSelectIcon,
                        onSelectCategory = onSelectCategory,
                        onSelectStyle = onSelectStyle,
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
    resolveFontWeight: (IconStyle?) -> FontWeight,
    variableFontConfig: VariableFontConfig?,
    customizationContent: (@Composable (onClose: () -> Unit) -> Unit)?,
    onSelectIcon: (StandardIcon) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSelectStyle: (IconStyle) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSettingsChange: (SizeSettings) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var selectedIcon by rememberMutableState<StandardIcon?> { null }
    var isSidePanelOpen by rememberMutableState { false }
    var scaleFactor by rememberMutableState { ZOOM_DEFAULT_SCALE }
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
                styles = state.config.styles,
                selectedCategory = state.selectedCategory,
                selectedStyle = state.selectedStyle,
                onToggleCustomization = { isSidePanelOpen = !isSidePanelOpen },
                onSelectStyle = { style ->
                    scope.launch {
                        lazyGridState.scrollToItem(0)
                    }
                    onSelectStyle(style)
                },
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
                    val styleAwareAlias = state.selectedStyle
                        ?.id
                        ?.let { "$fontAlias-$it" }
                        ?: fontAlias
                    val styleAwareWeight = variableFontConfig?.weight ?: resolveFontWeight(state.selectedStyle)
                    val iconFont = rememberStandardFont(
                        font = state.fontByteArray,
                        alias = styleAwareAlias,
                        weight = styleAwareWeight,
                        weights = variableFontConfig?.weights,
                        fontVariationSettings = variableFontConfig?.variation,
                    )
                    val iconSizeDp =
                        ((variableFontConfig?.opticalSize ?: state.settings.size.toFloat()) * scaleFactor).dp

                    ProvideIconParameters(
                        iconFont = iconFont,
                        tint = LocalContentColor.current,
                        weight = styleAwareWeight,
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
                                            iconName = icon.codepoint.toGlyphString(),
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
        if (state.fontByteArray != null) {
            ZoomFloatingBar(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp),
                scaleFactor = scaleFactor,
                onScaleChange = { scaleFactor = it },
            )
        }
        SidePanel(
            isOpen = isSidePanelOpen,
            onClose = { isSidePanelOpen = false },
            content = {
                if (customizationContent != null) {
                    customizationContent { isSidePanelOpen = false }
                } else {
                    IconSizeCustomization(
                        settings = state.settings,
                        onSettingsChange = onSettingsChange,
                        onClose = { isSidePanelOpen = false },
                        sizeLabel = stringResource("web.import.font.customize.size"),
                    )
                }
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
                is CategoryHeader -> CategoryHeaderItem(title = item.categoryName)
                is IconItem<*> -> (item.icon as? StandardIcon)?.let { iconContent(it) }
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
    font: FontByteArray,
    alias: String,
    weight: FontWeight,
    weights: List<FontWeight>? = null,
    fontVariationSettings: FontVariation.Settings? = null,
): IconFont = when (fontVariationSettings) {
    null -> rememberVariableIconFont(
        alias = alias,
        data = font.bytes,
        weights = weights?.toTypedArray() ?: arrayOf(weight),
    )
    else -> rememberVariableIconFont(
        alias = alias,
        data = font.bytes,
        weights = weights?.toTypedArray() ?: arrayOf(weight),
        fontVariationSettings = fontVariationSettings,
    )
}

/**
 * Shared icon grid for web import screens.
 * Provides a consistent grid layout with scrollbar.
 *
 * @param state The LazyGridState for the grid
 * @param modifier Modifier to be applied to the container
 * @param content The content of the grid
 */
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

/**
 * Shimmer placeholder for loading icon state.
 *
 * @param shimmer The shimmer animation to use
 * @param modifier Modifier to be applied to the shimmer placeholder
 */
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
