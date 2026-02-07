package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.InlineIconText
import io.github.composegears.valkyrie.jewel.icons.IntelliJIcons
import io.github.composegears.valkyrie.jewel.icons.KotlinLogo
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.platform.picker.rememberKtPathPicker
import io.github.composegears.valkyrie.jewel.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.ui.DragAndDropBox
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction
import io.github.composegears.valkyrie.util.stringResource
import kotlin.io.path.isRegularFile
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChooseExistingPackFile(
    modifier: Modifier = Modifier,
    onAction: (ExistingPackAction) -> Unit,
) {
    val project = LocalProject.current

    val dragAndDropHandler = rememberFileDragAndDropHandler(
        onDrop = { path ->
            if (!path.isRegularFile()) return@rememberFileDragAndDropHandler

            onAction(
                ExistingPackAction.SelectKotlinFile(
                    path = path,
                    project = project,
                ),
            )
        },
    )
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    val scope = rememberCoroutineScope()
    val ktPathPicker = rememberKtPathPicker()

    Column(
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 32.dp),
    ) {
        DragAndDropBox(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            isDragging = isDragging,
            onChoose = {
                scope.launch {
                    val path = ktPathPicker.launch()

                    if (path != null) {
                        onAction(
                            ExistingPackAction.SelectKotlinFile(
                                path = path,
                                project = project,
                            ),
                        )
                    }
                }
            },
        ) {
            InlineIconText(
                modifier = Modifier.padding(horizontal = 16.dp),
                imageVector = IntelliJIcons.KotlinLogo,
                textAlign = TextAlign.Center,
                text = stringResource("iconpack.existingpack.choose.file.dnd"),
            )
        }
    }
}

@Preview
@Composable
private fun ChooseExistingPackFilePreview() = PreviewTheme(alignment = Alignment.Center) {
    ChooseExistingPackFile(onAction = {})
}
