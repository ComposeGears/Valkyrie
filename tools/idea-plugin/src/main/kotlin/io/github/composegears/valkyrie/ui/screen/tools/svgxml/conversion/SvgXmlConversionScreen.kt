package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.CopyAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.SuccessBanner
import io.github.composegears.valkyrie.jewel.banner.rememberBannerManager
import io.github.composegears.valkyrie.jewel.editor.IntellijEditorTextField
import io.github.composegears.valkyrie.jewel.editor.SyntaxLanguage
import io.github.composegears.valkyrie.jewel.platform.copyInClipboard
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgSource
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlState
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val SvgXmlConversionScreen by navDestination<SvgXmlParams> {
    val navController = navController()
    val params = navArgs()
    val bannerManager = rememberBannerManager()

    val viewModel = saveableViewModel {
        SvgXmlViewModel(
            savedState = it,
            params = params,
        )
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
                onAction = { action ->
                    when (action) {
                        is SvgXmlAction.OnCopyInClipboard -> {
                            copyInClipboard(action.text)
                            bannerManager.show(message = SuccessBanner(text = message("general.action.text.copy.clipboard")))
                        }
                    }
                },
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
    var latestCode by rememberMutableState { state.xmlContent.xmlCode }

    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("svg.to.xml.picker.title"))
            WeightSpacer()
            CopyAction(onCopy = { onAction(SvgXmlAction.OnCopyInClipboard(latestCode)) })
            SettingsAction(openSettings = openSettings)
        }
        HorizontalDivider()
        IntellijEditorTextField(
            modifier = Modifier.fillMaxSize(),
            language = SyntaxLanguage.XML,
            text = state.xmlContent.xmlCode,
            onValueChange = { latestCode = it },
        )
    }
}

@Preview
@Composable
private fun SvgXmlContentPreview() = ProjectPreviewTheme {
    SvgXmlContent(
        state = SvgXmlState.Content(
            svgSource = SvgSource.TextBasedIcon(""),
            xmlContent = XmlContent(
                xmlCode = """
                    <vector xmlns:android="http://schemas.android.com/apk/res/android"
                        android:width="24dp"
                        android:height="24dp"
                        android:viewportWidth="24"
                        android:viewportHeight="24">
                        <path
                            android:fillColor="#FF000000"
                            android:pathData="M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
                    </vector>
                """.trimIndent(),
            ),
        ),
        onBack = {},
        openSettings = {},
        onAction = {},
    )
}
