package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.ui.foundation.compositionlocal.LocalProject
import io.github.composegears.valkyrie.ui.foundation.conversion.GenericConversionScreen
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.copyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorSource
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlAction
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlParams
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlState
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui.XmlCodeViewer
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui.action.EditActionContent
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui.action.PreviewActionContent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.IR_STUB
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val ImageVectorToXmlScreen by navDestination<ImageVectorToXmlParams> {
    val navController = navController()
    val params = navArgs()
    val snackbar = rememberSnackbar()
    val project = LocalProject.current

    val viewModel = saveableViewModel {
        ImageVectorToXmlViewModel(
            project = project.current,
            savedState = it,
            params = params,
        )
    }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach(snackbar::show)
            .launchIn(this)
    }

    state?.let { state ->
        ImageVectorToXmlContent(
            state = state,
            onBack = navController::back,
            openSettings = {
                navController.navigate(dest = SettingsScreen)
            },
            onAction = { action ->
                when (action) {
                    is ImageVectorToXmlAction.OnCopyInClipboard -> {
                        copyInClipboard(action.text)
                        snackbar.show(message = "Copied to clipboard")
                    }
                    is ImageVectorToXmlAction.OnIconNameChange -> {
                        viewModel.changeIconName(action.name)
                    }
                }
            },
        )
    }
}

@Composable
private fun ImageVectorToXmlContent(
    state: ImageVectorToXmlState,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onAction: (ImageVectorToXmlAction) -> Unit,
) {
    GenericConversionScreen(
        title = stringResource("imagevectortoxml.conversion.title"),
        iconName = state.xmlContent.name,
        codeContent = state.xmlContent.xmlCode,
        irImageVector = state.xmlContent.irImageVector,
        onBack = onBack,
        onIconNameChange = { onAction(ImageVectorToXmlAction.OnIconNameChange(it)) },
        onCopyCode = { onAction(ImageVectorToXmlAction.OnCopyInClipboard(it)) },
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
            )
        },
        codeViewer = { text, onChange ->
            XmlCodeViewer(
                modifier = Modifier.fillMaxSize(),
                text = text,
                onChange = onChange,
            )
        },
    )
}

@Preview
@Composable
private fun ImageVectorToXmlContentPreview() = PreviewTheme {
    ImageVectorToXmlContent(
        state = ImageVectorToXmlState(
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
