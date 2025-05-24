package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.TextWithIcon
import io.github.composegears.valkyrie.ui.foundation.compositionlocal.LocalProject
import io.github.composegears.valkyrie.ui.foundation.icons.KotlinLogo
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.picker.rememberKtFilePicker
import io.github.composegears.valkyrie.ui.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction
import kotlin.io.path.isRegularFile
import kotlinx.coroutines.launch

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
                    project = project.current,
                ),
            )
        },
    )
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    val scope = rememberCoroutineScope()
    val ktFilePicker = rememberKtFilePicker()

    Column(modifier = modifier) {
        DragAndDropBox(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            isDragging = isDragging,
            onChoose = {
                scope.launch {
                    val path = ktFilePicker.launch()

                    if (path != null) {
                        onAction(
                            ExistingPackAction.SelectKotlinFile(
                                path = path,
                                project = project.current,
                            ),
                        )
                    }
                }
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TextWithIcon(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    imageVector = ValkyrieIcons.KotlinLogo,
                    textAlign = TextAlign.Center,
                    text = "Drag & drop existing  [icon]  icon pack\nor browse",
                    style = MaterialTheme.typography.titleSmall,
                    iconSize = 12.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChooseExistingPackFilePreview() = PreviewTheme {
    ChooseExistingPackFile(
        modifier = Modifier.fillMaxWidth(0.8f),
        onAction = {},
    )
}
