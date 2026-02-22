package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.ErrorBanner
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.SuccessBanner
import io.github.composegears.valkyrie.jewel.banner.rememberBannerManager
import io.github.composegears.valkyrie.jewel.editor.SyntaxLanguage
import io.github.composegears.valkyrie.jewel.platform.copyInClipboard
import io.github.composegears.valkyrie.jewel.platform.picker.SaveResult
import io.github.composegears.valkyrie.jewel.platform.picker.rememberFileSaver
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.sdk.compose.foundation.ObserveEvent
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.conversion.GenericConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction.OnExport
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionAction.OnIconNameChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionEvent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.EditActionContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.PreviewActionContent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import java.nio.file.Path
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed interface SimpleConversionParamsSource {
    data class PathSource(val path: Path) : SimpleConversionParamsSource
    data class TextSource(val name: String, val text: String) : SimpleConversionParamsSource
}

val SimpleConversionScreen by navDestination<SimpleConversionParamsSource> {
    val navController = navController()
    val params = navArgs()
    val bannerManager = rememberBannerManager()

    val viewModel = saveableViewModel {
        SimpleConversionViewModel(
            savedState = it,
            params = params,
        )
    }

    val fileSaver = rememberFileSaver(
        title = stringResource("simple.export.dialog.title"),
        description = stringResource("simple.export.dialog.description"),
    )

    val state by viewModel.state.collectAsState()
    val settings by viewModel.inMemorySettings.settings.collectAsState()

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is SimpleConversionEvent.ExportKtFile -> {
                when (val result = fileSaver.save(event.fileName, event.content)) {
                    is SaveResult.Success -> bannerManager.show(
                        message = SuccessBanner(text = message("general.export.success")),
                    )
                    is SaveResult.Error -> bannerManager.show(
                        message = ErrorBanner(text = message("general.export.error", result.message)),
                    )
                    is SaveResult.Cancelled -> Unit
                }
            }
            is SimpleConversionEvent.CopyInClipboard -> {
                copyInClipboard(event.text)
                bannerManager.show(message = SuccessBanner(text = message("general.action.text.copy.clipboard")))
            }
        }
    }

    when (val state = state) {
        is ConversionState -> {
            SimpleConversionContent(
                state = state,
                previewType = settings.previewType,
                onBack = navController::back,
                openSettings = {
                    navController.navigate(SettingsScreen)
                },
                onAction = viewModel::onAction,
            )
        }
        is SimpleConversionState.Error -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Toolbar {
                    BackAction(onBack = navController::back)
                    Title(text = stringResource("simple.picker.title"))
                    WeightSpacer()
                    SettingsAction(
                        openSettings = {
                            navController.navigate(SettingsScreen)
                        },
                    )
                }
                WeightSpacer(weight = 0.3f)
                ErrorPlaceholder(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    message = state.message,
                    description = state.stacktrace,
                )
                WeightSpacer(weight = 0.7f)
            }
        }
        is SimpleConversionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LoadingPlaceholder()
            }
        }
    }
}

@Composable
private fun SimpleConversionContent(
    state: ConversionState,
    previewType: PreviewType,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onAction: (SimpleConversionAction) -> Unit,
) {
    GenericConversionScreen(
        title = stringResource("simple.picker.title"),
        iconName = state.iconContent.name,
        codeContent = state.iconContent.code,
        irImageVector = state.iconContent.irImageVector,
        language = SyntaxLanguage.KOTLIN,
        onBack = onBack,
        onIconNameChange = { onAction(OnIconNameChange(it)) },
        onExport = { onAction(OnExport(it)) },
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
    )
}

@Preview
@Composable
private fun SimpleConversionPreviewUiPreview() = ProjectPreviewTheme {
    TiamatPreview(
        destination = SimpleConversionScreen,
        navArgs = SimpleConversionParamsSource.TextSource(
            name = "IconName",
            text = """
                <vector xmlns:android="http://schemas.android.com/apk/res/android"
                    android:width="24dp"
                    android:height="24dp"
                    android:viewportWidth="18"
                    android:viewportHeight="18">
                </vector>
            """.trimIndent(),
        ),
    )
}
