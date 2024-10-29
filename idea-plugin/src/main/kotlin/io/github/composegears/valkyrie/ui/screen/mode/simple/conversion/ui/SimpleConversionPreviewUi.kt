package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.FocusableTextField
import io.github.composegears.valkyrie.ui.foundation.FocusableTextFieldDefaults
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.highlights.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnIconNaneChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState

@Composable
fun SimpleConversionPreviewStateUi(
    state: ConversionState,
    onAction: (SimpleConversionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var codePreview by rememberMutableState(state.iconContent.code) { state.iconContent.code }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FocusableTextField(
                value = state.iconContent.name,
                onValueChange = { onAction(OnIconNaneChange(it)) },
                colors = FocusableTextFieldDefaults.colors().copy(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
            CopyAction(onCopy = { onAction(OnCopyInClipboard(codePreview)) })
        }
        VerticalSpacer(8.dp)
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = Dp.Hairline,
        )
        KotlinCodeViewer(
            modifier = Modifier.fillMaxSize(),
            text = codePreview,
            onChange = {
                codePreview = it
            },
        )
    }
}

@Preview
@Composable
private fun SimpleConversionPreviewUiPreview() = PreviewTheme {
    SimpleConversionPreviewStateUi(
        state = ConversionState(
            iconSource = IconSource.StringBasedIcon(""),
            iconContent = IconContent(
                name = "IconName",
                code = """
                    package io.github.composegears.valkyrie.icons

                    import androidx.compose.ui.graphics.vector.ImageVector
                    import androidx.compose.ui.unit.dp
                    import kotlin.Suppress

                    val WithoutPath: ImageVector
                        get() {
                            if (_WithoutPath != null) {
                                return _WithoutPath!!
                            }
                            _WithoutPath = ImageVector.Builder(
                                name = "WithoutPath",
                                defaultWidth = 24.dp,
                                defaultHeight = 24.dp,
                                viewportWidth = 18f,
                                viewportHeight = 18f
                            ).build()

                            return _WithoutPath!!
                        }

                    @Suppress("ObjectPropertyName")
                    private var _WithoutPath: ImageVector? = null

                """.trimIndent(),
            ),
        ),
        onAction = {},
    )
}
