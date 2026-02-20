package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.SuccessBanner
import io.github.composegears.valkyrie.jewel.banner.rememberBannerManager
import io.github.composegears.valkyrie.jewel.editor.SyntaxLanguage
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.platform.copyInClipboard
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.jewel.ui.placeholder.ErrorPlaceholder
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.conversion.GenericConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.EditActionContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action.PreviewActionContent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorSource
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlAction
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlState
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.util.IR_STUB
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val ImageVectorXmlScreen by navDestination<ImageVectorXmlParams> {
    val navController = navController()
    val params = navArgs()
    val bannerManager = rememberBannerManager()

    val project = LocalProject.current

    val viewModel = saveableViewModel {
        ImageVectorXmlViewModel(
            project = project,
            savedState = it,
            params = params,
        )
    }
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is ImageVectorXmlState.Content -> {
            ImageVectorToXmlContent(
                state = currentState,
                onBack = navController::back,
                openSettings = {
                    navController.navigate(dest = SettingsScreen)
                },
                onAction = { action ->
                    when (action) {
                        is ImageVectorXmlAction.OnCopyInClipboard -> {
                            copyInClipboard(action.text)
                            bannerManager.show(message = SuccessBanner(text = message("general.action.text.copy.clipboard")))
                        }
                        is ImageVectorXmlAction.OnIconNameChange -> {
                            viewModel.changeIconName(action.name)
                        }
                    }
                },
            )
        }
        is ImageVectorXmlState.Error -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Toolbar {
                    BackAction(onBack = navController::back)
                    Title(text = stringResource("imagevectortoxml.conversion.title"))
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
        is ImageVectorXmlState.Loading -> {
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
private fun ImageVectorToXmlContent(
    state: ImageVectorXmlState.Content,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onAction: (ImageVectorXmlAction) -> Unit,
) {
    GenericConversionScreen(
        title = stringResource("imagevectortoxml.conversion.title"),
        iconName = state.xmlContent.name,
        codeContent = state.xmlContent.xmlCode,
        irImageVector = state.xmlContent.irImageVector,
        language = SyntaxLanguage.XML,
        onBack = onBack,
        onIconNameChange = { onAction(ImageVectorXmlAction.OnIconNameChange(it)) },
        onCopyCode = { onAction(ImageVectorXmlAction.OnCopyInClipboard(it)) },
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
                previewType = PreviewType.Pixel,
            )
        },
    )
}

@Preview
@Composable
private fun ImageVectorToXmlContentPreview() = ProjectPreviewTheme {
    ImageVectorToXmlContent(
        state = ImageVectorXmlState.Content(
            iconSource = ImageVectorSource.TextBasedIcon(""),
            xmlContent = XmlContent(
                name = "IconName",
                irImageVector = IR_STUB,
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
