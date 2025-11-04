package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.compose.codeviewer.KotlinCodeViewer
import io.github.composegears.valkyrie.compose.core.animation.ExpandedAnimatedContent
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.EditAction
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.PreviewAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.copyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction.OnIconNameChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.EditActionContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.ExpandedActions
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.PreviewActionContent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.IR_STUB
import java.nio.file.Path
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed interface SimpleConversionParamsSource {
    data class PathSource(val path: Path) : SimpleConversionParamsSource
    data class TextSource(val name: String, val text: String) : SimpleConversionParamsSource
}

val SimpleConversionScreen by navDestination<SimpleConversionParamsSource> {
    val navController = navController()
    val params = navArgs()
    val snackbar = rememberSnackbar()

    val viewModel = saveableViewModel {
        SimpleConversionViewModel(
            savedState = it,
            params = params,
        )
    }
    val state by viewModel.state.collectAsState()
    val settings by viewModel.inMemorySettings.settings.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach(snackbar::show)
            .launchIn(this)
    }

    state?.let { state ->
        SimpleConversionContent(
            state = state,
            previewType = settings.previewType,
            onBack = navController::back,
            openSettings = {
                navController.navigate(dest = SettingsScreen)
            },
            onAction = {
                when (it) {
                    is OnCopyInClipboard -> {
                        copyInClipboard(it.text)
                        snackbar.show(message = "Copied in clipboard")
                    }
                    is OnIconNameChange -> viewModel.changeIconName(it.name)
                }
            },
        )
    }
}

@Composable
private fun SimpleConversionContent(
    state: SimpleConversionState,
    previewType: PreviewType,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onAction: (SimpleConversionAction) -> Unit,
) {
    var codePreview by rememberMutableState(state.iconContent.code) { state.iconContent.code }
    var expandedAction by rememberMutableState { ExpandedActions.None }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Simple conversion")
            WeightSpacer()
            EditAction(
                onEdit = {
                    expandedAction = when (expandedAction) {
                        ExpandedActions.Edit -> ExpandedActions.None
                        else -> ExpandedActions.Edit
                    }
                },
                selected = expandedAction == ExpandedActions.Edit,
            )
            PreviewAction(
                onPreview = {
                    expandedAction = when (expandedAction) {
                        ExpandedActions.Preview -> ExpandedActions.None
                        else -> ExpandedActions.Preview
                    }
                },
                selected = expandedAction == ExpandedActions.Preview,
            )
            CopyAction(onCopy = { onAction(OnCopyInClipboard(codePreview)) })
            SettingsAction(openSettings = openSettings)
        }
        ExpandedAnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = expandedAction,
        ) { actions ->
            when (actions) {
                ExpandedActions.Edit -> {
                    EditActionContent(
                        iconName = state.iconContent.name,
                        onNameChange = { onAction(OnIconNameChange(it)) },
                    )
                }
                ExpandedActions.Preview -> PreviewActionContent(
                    irImageVector = state.iconContent.irImageVector,
                    previewType = previewType,
                )
                ExpandedActions.None -> Spacer(modifier = Modifier.fillMaxWidth())
            }
        }
        HorizontalDivider()
        KotlinCodeViewer(
            modifier = Modifier.fillMaxSize(),
            text = codePreview,
            onChange = {
                codePreview = it
            },
        )
    }
}

@Preview
@Composable
private fun SimpleConversionPreviewUiPreview() = PreviewTheme {
    SimpleConversionContent(
        state = SimpleConversionState(
            iconSource = IconSource.StringBasedIcon(""),
            iconContent = IconContent(
                name = "IconName",
                irImageVector = IR_STUB,
                code = """
                    import androidx.compose.ui.graphics.vector.ImageVector
                    import androidx.compose.ui.unit.dp

                    val WithoutPath: ImageVector
                        get() {
                            if (_WithoutPath != null) {
                                return _WithoutPath!!
                            }
                            _WithoutPath = ImageVector.Builder(
                                name = "WithoutPath",
                                defaultWidth = 24.dp,
                                defaultHeight = 24.dp,
                                viewportWidth = 18f,
                                viewportHeight = 18f
                            ).build()

                            return _WithoutPath!!
                        }

                    @Suppress("ObjectPropertyName")
                    private var _WithoutPath: ImageVector? = null

                """.trimIndent(),
            ),
        ),
        previewType = PreviewType.Auto,
        onBack = {},
        openSettings = {},
        onAction = {},
    )
}
