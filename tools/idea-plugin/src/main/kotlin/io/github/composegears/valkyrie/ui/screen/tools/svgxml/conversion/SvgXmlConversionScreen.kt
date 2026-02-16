package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import io.github.composegears.valkyrie.jewel.CopyAction
import io.github.composegears.valkyrie.jewel.EditToggleAction
import io.github.composegears.valkyrie.jewel.ExportAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.ErrorBanner
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.SuccessBanner
import io.github.composegears.valkyrie.jewel.banner.rememberBannerManager
import io.github.composegears.valkyrie.jewel.editor.IntellijEditorTextField
import io.github.composegears.valkyrie.jewel.editor.SyntaxLanguage
import io.github.composegears.valkyrie.jewel.platform.copyInClipboard
import io.github.composegears.valkyrie.jewel.platform.picker.rememberFileSaver
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.ExpandedAnimatedContent
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.FocusableColumn
import io.github.composegears.valkyrie.ui.foundation.conversion.ConversionExpandedAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.EditActionContent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction.OnCodeChange
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction.OnExportFile
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction.OnNameChange
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlEvent
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlState
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview

val SvgXmlConversionScreen by navDestination<SvgXmlParams> {
    val navController = navController()
    val params = navArgs()
    val bannerManager = rememberBannerManager()

    val fileSaver = rememberFileSaver(
        title = stringResource("svg.to.xml.export.dialog.title"),
        description = stringResource("svg.to.xml.export.dialog.description"),
    )
    val viewModel = saveableViewModel { SvgXmlViewModel(savedState = it, params = params) }

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is SvgXmlEvent.ExportXmlFile -> {
                        val saved = fileSaver.save(it.fileName, it.content)
                        if (saved) {
                            bannerManager.show(message = SuccessBanner(text = message("svg.to.xml.export.success")))
                        } else {
                            bannerManager.show(message = ErrorBanner(text = message("svg.to.xml.export.error")))
                        }
                    }
                    is SvgXmlEvent.CopyInClipboard -> {
                        copyInClipboard(it.text)
                        bannerManager.show(message = SuccessBanner(text = message("general.action.text.copy.clipboard")))
                    }
                }
            }
            .launchIn(this)
    }

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is SvgXmlState.Content -> {
            SvgXmlContent(
                state = currentState,
                onBack = navController::back,
                openSettings = {
                    navController.navigate(dest = SettingsScreen)
                },
                onAction = viewModel::onAction,
            )
        }
        is SvgXmlState.Error -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Toolbar {
                    BackAction(onBack = navController::back)
                    Title(text = stringResource("svg.to.xml.picker.title"))
                    WeightSpacer()
                    SettingsAction(
                        openSettings = {
                            navController.navigate(dest = SettingsScreen)
                        },
                    )
                }
                WeightSpacer(weight = 0.3f)
                ErrorPlaceholder(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    message = currentState.message,
                    description = currentState.stacktrace,
                )
                WeightSpacer(weight = 0.7f)
            }
        }
        is SvgXmlState.Loading -> {
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
private fun SvgXmlContent(
    state: SvgXmlState.Content,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onAction: (SvgXmlAction) -> Unit,
) {
    var expandedAction by rememberMutableState { ConversionExpandedAction.None }

    val code by rememberMutableState { state.xmlCode.value }

    FocusableColumn(modifier = Modifier.fillMaxSize()) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("svg.to.xml.picker.title"))
            WeightSpacer()
            EditToggleAction(
                onEdit = { selected ->
                    expandedAction = if (selected) ConversionExpandedAction.Edit else ConversionExpandedAction.None
                },
                selected = expandedAction == ConversionExpandedAction.Edit,
            )
            ExportAction { onAction(OnExportFile) }
            CopyAction(onCopy = { onAction(OnCopyInClipboard) })
            SettingsAction(openSettings = openSettings)
        }
        ExpandedAnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = expandedAction,
        ) { action ->
            when (action) {
                ConversionExpandedAction.Edit -> {
                    EditActionContent(
                        iconName = state.fileName,
                        onNameChange = { onAction(OnNameChange(name = it)) },
                    )
                }
                else -> {
                    Spacer(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        HorizontalDivider()
        IntellijEditorTextField(
            modifier = Modifier.fillMaxSize(),
            language = SyntaxLanguage.XML,
            text = code,
            onValueChange = { onAction(OnCodeChange(newCode = it)) },
        )
    }
}

@Preview
@Composable
private fun SvgXmlContentPreview() = ProjectPreviewTheme {
    TiamatPreview(
        destination = SvgXmlConversionScreen,
        navArgs = SvgXmlParams.TextSource(
            """
            <svg width="24" height="24" viewBox="0 0 24 24">
                <path d="M10,10h4v4h-4z"/>
            </svg>
            """.trimIndent(),
        ),
    )
}
