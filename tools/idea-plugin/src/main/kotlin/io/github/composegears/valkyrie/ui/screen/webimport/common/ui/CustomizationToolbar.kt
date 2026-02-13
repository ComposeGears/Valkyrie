package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.CloseAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.ui.component.Link

/**
 * Standard customization toolbar with close, title, and reset actions.
 *
 * @param onClose Callback when close is clicked
 * @param onReset Callback when reset is clicked
 * @param isModified Used to determine whether Reset is enabled
 */
@Composable
fun CustomizationToolbar(
    onClose: () -> Unit,
    onReset: () -> Unit,
    isModified: Boolean,
    modifier: Modifier = Modifier,
) {
    Toolbar(modifier = modifier) {
        CloseAction(onClose = onClose)
        Title(text = stringResource("web.import.font.customize.header"))
        WeightSpacer()
        Link(
            text = stringResource("web.import.font.customize.reset"),
            onClick = onReset,
            enabled = isModified,
        )
        Spacer(4.dp)
    }
}
