package io.github.composegears.valkyrie.ui.screen.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.core.FocusContainer
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.editor.edit.EditScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

val EditorSelectScreen by navDestination {
    val navController = navController()

    EditorScreenUi(
        onBack = navController::back,
        openEdit = { type ->
            navController.navigate(
                dest = EditScreen,
                navArgs = type,
            )
        },
    )
}

@Composable
private fun EditorScreenUi(
    onBack: () -> Unit,
    openEdit: (EditorType) -> Unit,
) {
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("editor.select.header"))
        }
        Spacer(8.dp)

        val options = remember {
            listOf(
                OptionElement(
                    type = EditorType.MaterialIconToRegularImageVector,
                    title = "editor.select.option.material",
                    description = "editor.select.option.material.description",
                ),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            options.forEach {
                EditorOption(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    title = stringResource(it.title),
                    description = stringResource(it.description),
                    onClick = { openEdit(it.type) },
                )
            }
        }
    }
}

@Composable
private fun EditorOption(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FocusContainer(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier
                .width(420.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = JewelTheme.typography.h4TextStyle,
            )
            InfoText(text = description)
        }
    }
}

private data class OptionElement(
    val type: EditorType,
    val title: String,
    val description: String,
)

@Preview
@Composable
private fun EditorScreenPreview() = PreviewTheme {
    EditorScreenUi(
        onBack = {},
        openEdit = {},
    )
}
