package io.github.composegears.valkyrie.jewel.textarea

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.debugBounds
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextArea

@Composable
fun TextArea(
    text: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val updatedOnValueChange by rememberUpdatedState(onValueChange)

    val state = rememberTextFieldState(text)

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .collectLatest { updatedOnValueChange(it) }
    }

    TextArea(
        modifier = modifier,
        state = state,
        placeholder = {
            Text(text = placeholder)
        },
        enabled = enabled,
    )
}

@Preview
@Composable
private fun TextAreaPreview() = PreviewTheme(alignment = Alignment.Center) {
    var updatedText by rememberMutableState { "" }

    TextArea(
        modifier = Modifier
            .width(300.dp)
            .height(100.dp),
        text = """
             /*
             * This program is free software: you can redistribute it and/or modify
             * it under the terms of the GNU General Public License as published by
             * the Free Software Foundation, either version 3 of the License, or
             * (at your option) any later version.
             *
             * This program is distributed in the hope that it will be useful,
             * but WITHOUT ANY WARRANTY; without even the implied warranty of
             * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
             * GNU General Public License for more details.
             *
             * You should have received a copy of the GNU General Public License
             * along with this program.  If not, see <https://www.gnu.org/licenses/>.
             */
        """.trimIndent(),
        onValueChange = { updatedText = it },
        placeholder = "placeholder text",
    )

    Text(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .debugBounds(),
        text = updatedText,
    )
}
