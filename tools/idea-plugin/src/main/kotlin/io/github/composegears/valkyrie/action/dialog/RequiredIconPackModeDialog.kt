package io.github.composegears.valkyrie.action.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.ui.DialogWrapper
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Warning
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme
import io.github.composegears.valkyrie.ui.platform.buildComposePanel
import java.awt.event.ActionEvent
import javax.swing.Action

class RequiredIconPackModeDialog(
    private val message: String,
    private val onContinue: () -> Unit,
) : DialogWrapper(true) {

    init {
        title = "Valkyrie Warning"
        isResizable = false
        setSize(450, 200)
        init()
    }

    override fun createCenterPanel() = buildComposePanel {
        ValkyrieTheme {
            DialogContentUi(
                modifier = Modifier.fillMaxSize(),
                message = message,
            )
        }
    }

    override fun createActions(): Array<Action> {
        val okAction = object : DialogWrapperAction("Setup") {

            init {
                putValue("DefaultAction", true)
            }

            override fun doAction(e: ActionEvent?) = doOKAction()
        }
        return arrayOf(okAction, myCancelAction)
    }

    override fun doOKAction() {
        super.doOKAction()
        onContinue()
    }
}

@Composable
private fun DialogContentUi(
    message: String,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally),
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            imageVector = ValkyrieIcons.Warning,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            text = message,
        )
    }
}

@Preview
@Composable
private fun RequiredIconPackModeDialogPreview() = PreviewTheme {
    DialogContentUi(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .border(0.1.dp, MaterialTheme.colorScheme.onSurface),
        message = "This action requires an Icon pack mode to be setup",
    )
}
