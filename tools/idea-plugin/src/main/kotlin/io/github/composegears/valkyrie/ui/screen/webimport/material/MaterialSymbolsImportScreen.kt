package io.github.composegears.valkyrie.ui.screen.webimport.material

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontVariation.grade
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
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
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconLoadingPlaceholder
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.LoadingContent
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.SidePanel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.IconModel
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.FontCustomization
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.MaterialTopActions
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
                is MaterialState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ErrorContent(message = current.message)
                    }
                }
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

            IconGrid(state = lazyGridState) {
                items(
                    items = state.gridItems,
                    span = { item ->
                        when (item) {
                            is CategoryHeader -> GridItemSpan(maxLineSpan)
                            is IconItem<*> -> GridItemSpan(1)
                        }
                    },
                    key = { it.id },
                ) { item ->
                    when (item) {
                        is CategoryHeader -> CategoryHeaderComponent(title = item.categoryName)
                        is IconItem<*> -> {
                            val materialIcon = item.icon as IconModel
                            MaterialIconStub(
                                icon = materialIcon,
                                shimmer = shimmer,
                            )
                        }
                    }
                }
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
                    EmptyContent()
                    return@ProvideIconParameters
                } else {
                    IconGrid(state = lazyGridState) {
                        items(
                            items = state.gridItems,
                            span = { item ->
                                when (item) {
                                    is CategoryHeader -> GridItemSpan(maxLineSpan)
                                    is IconItem<*> -> GridItemSpan(1)
                                }
                            },
                            key = { it.id },
                        ) { item ->
                            when (item) {
                                is CategoryHeader -> CategoryHeaderComponent(title = item.categoryName)
                                is IconItem<*> -> {
                                    val icon = item.icon as IconModel

                                    IconCard(
                                        name = icon.name,
                                        selected = icon == selectedIcon,
                                        onClick = {
                                            selectedIcon = icon
                                            onSelectIcon(icon)
                                        },
                                        iconContent = {
                                            FontIcon(
                                                modifier = Modifier.fillMaxSize(),
                                                icon = Char(icon.codepoint),
                                                contentDescription = null,
                                            )
                                        },
                                    )
                                }
                            }
                        }
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
    IconCard(
        name = icon.name,
        selected = false,
        onClick = { /* No-op during loading */ },
        iconContent = {
            IconLoadingPlaceholder(shimmer = shimmer)
        },
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
