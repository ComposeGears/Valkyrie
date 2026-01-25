package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.ui.component.GroupHeader
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.skiko.Cursor

@Composable
fun ExpandedGroup(
    @Nls text: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    itemSpacing: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    var expanded by rememberMutableState { false }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier.padding(paddingValues)) {
        GroupHeader(
            modifier =
            Modifier
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { expanded = !expanded },
                )
                .hoverable(interactionSource)
                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))),
            text = text,
            startComponent = {
                Icon(
                    key = when {
                        expanded -> AllIconsKeys.General.ChevronDown
                        else -> AllIconsKeys.General.ChevronRight
                    },
                    contentDescription = null,
                )
            },
        )
        if (expanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
            ) {
                Spacer(8.dp)
                content()
            }
        }
    }
}

@Preview
@Composable
private fun ExpandedGroupPreview() = PreviewTheme {
    var checked by rememberMutableState { true }

    ExpandedGroup(
        text = "Expanded group header",
        content = {
            CheckboxSettingsRow(
                text = "Option",
                infoText = "Option description",
                checked = checked,
                onCheckedChange = { checked = it },
            )
        },
    )
}
