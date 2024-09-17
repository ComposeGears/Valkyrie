package io.github.composegears.valkyrie.action.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.ui.DialogWrapper
import io.github.composegears.valkyrie.PluginWindow
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme

class RequiredIconPackModeDialog(
    private val onOk: () -> Unit,
) : DialogWrapper(true) {
    init {
        title = "Valkyrie Warning"
        init()
    }

    override fun createCenterPanel() = PluginWindow {
        ValkyrieTheme {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                text = "Please setup IconPack mode before using this feature.",
            )
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        onOk()
    }
}
