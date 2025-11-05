package io.github.composegears.valkyrie.ui.screen.webimport.material

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontVariation.grade
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
import io.github.composegears.valkyrie.compose.core.animatedBorder
import io.github.composegears.valkyrie.compose.core.animation.Shimmer
import io.github.composegears.valkyrie.compose.core.animation.rememberShimmer
import io.github.composegears.valkyrie.compose.core.animation.shimmer
import io.github.composegears.valkyrie.compose.core.applyIf
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.ui.foundation.VerticalScrollbar
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialGridItem.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.MaterialGridItem.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.FontCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.MaterialTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.SidePanel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
val MaterialSymbolsImportScreen by navDestination {
    val navController = navController()
    val parentNavController = navController.parent

    val viewModel = saveableViewModel { MaterialSymbolsViewModel(savedState = it) }
    val state by viewModel.materialState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is MaterialEvent.IconDownloaded -> {
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

    MaterialSymbolsImportUI(
        state = state,
        onBack = navController::back,
        onSelectIcon = viewModel::downloadIcon,
        onSelectFontFamily = viewModel::downloadFont,
        onSelectCategory = viewModel::selectCategory,
        onSettingsChange = viewModel::updateFontSettings,
        onSearchQueryChange = viewModel::updateSearchQuery,
    )
}

@Composable
private fun MaterialSymbolsImportUI(
    state: MaterialState,
    onBack: () -> Unit,
    onSelectIcon: (IconModel) -> Unit,
    onSelectFontFamily: (IconFontFamily) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onSettingsChange: (FontSettings) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    var isSidePanelOpen by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle("Material Symbols import")
        }
        AnimatedContent(
            targetState = state,
            contentKey = {
                when (it) {
                    is MaterialState.Loading -> "loading"
                    is MaterialState.Error -> "error"
                    is MaterialState.Success -> "success"
                }
            },
        ) { current ->
            when (current) {
                is MaterialState.Loading -> LoadingContent()
                is MaterialState.Error -> ErrorContent(message = current.message)
                is MaterialState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        IconsContent(
                            state = current,
                            onSelectIcon = onSelectIcon,
                            onSelectFontFamily = onSelectFontFamily,
                            onSelectCategory = onSelectCategory,
                            onToggleSidePanel = { isSidePanelOpen = !isSidePanelOpen },
                            onSearchQueryChange = onSearchQueryChange,
                        )
                        SidePanel(
                            isOpen = isSidePanelOpen,
                            onClose = { isSidePanelOpen = false },
                            content = {
                                FontCustomization(
                                    fontSettings = current.fontSettings,
                                    onClose = { isSidePanelOpen = false },
                                    onSettingsChange = onSettingsChange,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun IconsContent(
    state: MaterialState.Success,
    onSelectIcon: (IconModel) -> Unit,
    onSelectFontFamily: (IconFontFamily) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onToggleSidePanel: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var selectedIcon by rememberMutableState<IconModel?> { null }
    val lazyGridState = rememberLazyGridState()
    val fontSettings = state.fontSettings

    val fontByteArray = state.fontByteArray
    val iconFontFamily = state.iconFontFamily

    val focusManager = LocalFocusManager.current

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
        MaterialTopActions(
            categories = state.config.categories,
            selectedCategory = state.selectedCategory,
            iconFontFamily = iconFontFamily,
            onToggleSidePanel = onToggleSidePanel,
            onSelectFontFamily = onSelectFontFamily,
            onSelectCategory = { category ->
                scope.launch {
                    lazyGridState.scrollToItem(0)
                }
                onSelectCategory(category)
            },
            onSearchQueryChange = onSearchQueryChange,
        )
        if (fontByteArray == null) {
            val shimmer = rememberShimmer()

            Box {
                LazyVerticalGrid(
                    state = lazyGridState,
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(100.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.gridItems,
                        span = { item ->
                            when (item) {
                                is CategoryHeader -> GridItemSpan(maxLineSpan)
                                is IconItem -> GridItemSpan(1)
                            }
                        },
                        key = { it.id },
                    ) { item ->
                        when (item) {
                            is CategoryHeader -> MaterialCategoryHeader(item.category)
                            is IconItem -> {
                                MaterialIconStub(
                                    icon = item.icon,
                                    shimmer = shimmer,
                                )
                            }
                        }
                    }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(lazyGridState))
            }
        } else {
            val iconFont = rememberMaterialSymbolsFont(
                name = iconFontFamily.fontFamily,
                font = fontByteArray.bytes,
                fill = fontSettings.fill,
                grade = fontSettings.grade,
                opticalSize = fontSettings.opticalSize,
            )

            ProvideIconParameters(
                iconFont = iconFont,
                tint = LocalContentColor.current,
                weight = FontWeight(fontSettings.weight),
            ) {
                if (state.gridItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "No icons found",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    return@ProvideIconParameters
                } else {
                    Box {
                        LazyVerticalGrid(
                            state = lazyGridState,
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(
                                items = state.gridItems,
                                span = { item ->
                                    when (item) {
                                        is CategoryHeader -> GridItemSpan(maxLineSpan)
                                        is IconItem -> GridItemSpan(1)
                                    }
                                },
                                key = { it.id },
                            ) { item ->
                                when (item) {
                                    is CategoryHeader -> MaterialCategoryHeader(item.category)
                                    is IconItem -> {
                                        val icon = item.icon

                                        MaterialIconFont(
                                            icon = icon,
                                            onSelect = {
                                                selectedIcon = item.icon
                                                onSelectIcon(item.icon)
                                            },
                                            selected = item.icon == selectedIcon,
                                        )
                                    }
                                }
                            }
                        }
                        VerticalScrollbar(adapter = rememberScrollbarAdapter(lazyGridState))
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialIconStub(
    icon: IconModel,
    shimmer: Shimmer,
) {
    Box(contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.width(100.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .shimmer(shimmer = shimmer, cornerRadius = 12.dp),
                )
                VerticalSpacer(8.dp)
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = icon.name,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun MaterialIconFont(
    icon: IconModel,
    onSelect: () -> Unit,
    selected: Boolean,
) {
    Box(contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .width(100.dp)
                .applyIf(selected) {
                    animatedBorder(
                        borderColors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary,
                        ),
                        shape = CardDefaults.shape,
                    )
                },
            onClick = onSelect,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                FontIcon(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(16.dp),
                    icon = Char(icon.codepoint),
                    contentDescription = null,
                )
                VerticalSpacer(8.dp)
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = icon.name,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun MaterialCategoryHeader(category: Category) {
    Text(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 4.dp),
        text = category.name,
        style = MaterialTheme.typography.titleMedium,
    )
}

@OptIn(ExperimentalFontIconsApi::class)
@Composable
private fun rememberMaterialSymbolsFont(
    name: String,
    font: ByteArray,
    fill: Boolean,
    grade: Int,
    opticalSize: Float,
): IconFont = rememberVariableIconFont(
    alias = name,
    data = font,
    weights = arrayOf(
        FontWeight.W100,
        FontWeight.W200,
        FontWeight.W300,
        FontWeight.W400,
        FontWeight.W500,
        FontWeight.W600,
        FontWeight.W700,
    ),
    fontVariationSettings = FontVariation.Settings(
        FontVariation.Setting("FILL", if (fill) 1f else 0f),
        grade(grade),
        FontVariation.opticalSizing(TextUnit(opticalSize, TextUnitType.Sp)),
    ),
)
