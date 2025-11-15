package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.compose.codeviewer.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.conversion.GenericConversionScreen
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
    GenericConversionScreen(
        title = "Simple conversion",
        iconName = state.iconContent.name,
        codeContent = state.iconContent.code,
        irImageVector = state.iconContent.irImageVector,
        onBack = onBack,
        onIconNameChange = { onAction(OnIconNameChange(it)) },
        onCopyCode = { onAction(OnCopyInClipboard(it)) },
        onOpenSettings = openSettings,
        editPanel = { name, onNameChange ->
            EditActionContent(
                iconName = name,
                onNameChange = onNameChange,
            )
        },
        previewPanel = { irImageVector ->
            PreviewActionContent(
                irImageVector = irImageVector,
                previewType = previewType,
            )
        },
        codeViewer = { text, onChange ->
            KotlinCodeViewer(
                modifier = Modifier.fillMaxSize(),
                text = text,
                onChange = onChange,
            )
        },
    )
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
