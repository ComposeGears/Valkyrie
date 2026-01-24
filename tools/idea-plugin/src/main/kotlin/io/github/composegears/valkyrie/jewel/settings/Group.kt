package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

@Composable
fun Group(
    @Nls text: String,
    modifier: Modifier = Modifier,
    startComponent: (@Composable () -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    itemSpacing: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.padding(paddingValues)) {
        GroupHeader(
            text = text,
            startComponent = startComponent,
        )

        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            Spacer(8.dp)
            content()
        }
    }
}

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun ColumnScope.GroupSpacing() = Spacer(32.dp)

@Preview
@Composable
private fun GroupPreview() = PreviewTheme {
    var checked by rememberMutableState { true }

    Group(
        text = "Group header",
        startComponent = {
            Icon(
                key = AllIconsKeys.General.Warning,
                contentDescription = null,
            )
        },
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
