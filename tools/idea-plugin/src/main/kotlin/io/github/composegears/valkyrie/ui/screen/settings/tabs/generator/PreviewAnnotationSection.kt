package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.util.isLight
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.AndroidX
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.Jetbrains
import io.github.composegears.valkyrie.sdk.compose.codeviewer.rememberCodeHighlight
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui.SelectableCard
import io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui.SwitchOption
import io.github.composegears.valkyrie.util.stringResource

@Composable
fun PreviewAnnotationSection(
    generatePreview: Boolean,
    previewAnnotationType: PreviewAnnotationType,
    onGeneratePreviewChange: (Boolean) -> Unit,
    onAnnotationTypeChange: (PreviewAnnotationType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SwitchOption(
            title = stringResource("settings.generator.preview.block"),
            description = stringResource("settings.generator.preview.block.description"),
            checked = generatePreview,
            onCheckedChange = onGeneratePreviewChange,
        )
        AnimatedVisibility(
            visible = generatePreview,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column {
                Spacer(8.dp)
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SelectableCard(
                        modifier = Modifier.weight(1f),
                        text = "AndroidX Preview",
                        highlights = rememberCodeHighlight(
                            """
                               import androidx.compose.ui.tooling.preview.Preview

                               @Preview
                               @Composable
                               fun MyIconPreview() {
                                   Icon(imageVector = Icons.Filled.MyIcon)
                               }
                            """.trimIndent(),
                            isLight = MaterialTheme.colorScheme.isLight,
                        ),
                        isSelected = previewAnnotationType == AndroidX,
                        onSelect = { onAnnotationTypeChange(AndroidX) },
                    )

                    SelectableCard(
                        modifier = Modifier.weight(1f),
                        text = "JetBrains Preview",
                        highlights = rememberCodeHighlight(
                            """
                               import androidx.compose.desktop.ui.tooling.preview.Preview

                               @Preview
                               @Composable
                               fun MyIconPreview() {
                                   Icon(imageVector = Icons.Filled.MyIcon)
                               }
                            """.trimIndent(),
                            isLight = MaterialTheme.colorScheme.isLight,
                        ),
                        isSelected = previewAnnotationType == Jetbrains,
                        onSelect = { onAnnotationTypeChange(Jetbrains) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewAnnotationSectionPreview() = PreviewTheme {
    PreviewAnnotationSection(
        generatePreview = true,
        previewAnnotationType = AndroidX,
        onGeneratePreviewChange = {},
        onAnnotationTypeChange = {},
    )
}
