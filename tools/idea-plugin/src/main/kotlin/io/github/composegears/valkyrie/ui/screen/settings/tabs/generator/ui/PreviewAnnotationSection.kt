package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.AndroidX
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType.Jetbrains
import io.github.composegears.valkyrie.jewel.highlight.KtCodeViewer
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.RadioButtonGroup
import io.github.composegears.valkyrie.jewel.settings.RadioButtonTooltipRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.compose.highlights.core.rememberCodeHighlight
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme

@Composable
fun PreviewAnnotationSection(
    generatePreview: Boolean,
    previewAnnotationType: PreviewAnnotationType,
    onGeneratePreviewChange: (Boolean) -> Unit,
    onAnnotationTypeChange: (PreviewAnnotationType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CheckboxSettingsRow(
            text = stringResource("settings.generator.preview.block"),
            infoText = stringResource("settings.generator.preview.block.description"),
            checked = generatePreview,
            onCheckedChange = onGeneratePreviewChange,
        )
        AnimatedVisibility(
            modifier = Modifier.padding(start = 24.dp),
            visible = generatePreview,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            RadioButtonGroup(
                text = stringResource("settings.generator.preview.type"),
                paddingValues = PaddingValues(start = 4.dp, top = 16.dp),
            ) {
                RadioButtonTooltipRow(
                    text = stringResource("settings.generator.preview.type.androidx"),
                    selected = previewAnnotationType == AndroidX,
                    onClick = { onAnnotationTypeChange(AndroidX) },
                    tooltipContent = {
                        KtCodeViewer(
                            highlights = rememberCodeHighlight(
                                codeBlock = AndroidXPreviewHint,
                                isDark = JewelTheme.isDark,
                            ),
                        )
                    },
                )
                RadioButtonTooltipRow(
                    text = stringResource("settings.generator.preview.type.jetBrains"),
                    selected = previewAnnotationType == Jetbrains,
                    onClick = { onAnnotationTypeChange(Jetbrains) },
                    tooltipContent = {
                        KtCodeViewer(
                            highlights = rememberCodeHighlight(
                                codeBlock = JetbrainsPreviewHint,
                                isDark = JewelTheme.isDark,
                            ),
                        )
                    },
                )
            }
        }
    }
}

private val AndroidXPreviewHint = """
    import androidx.compose.ui.tooling.preview.Preview

    @Preview
    @Composable
    fun MyIconPreview() {
        Icon(imageVector = Icons.Filled.MyIcon)
    }
""".trimIndent()

private val JetbrainsPreviewHint = """
    import androidx.compose.desktop.ui.tooling.preview.Preview

    @Preview
    @Composable
    fun MyIconPreview() {
        Icon(imageVector = Icons.Filled.MyIcon)
    }
""".trimIndent()

@Preview
@Composable
private fun PreviewAnnotationSectionPreview() = PreviewTheme {
    var generatePreview by rememberMutableState { true }
    var previewAnnotationType by rememberMutableState { AndroidX }

    PreviewAnnotationSection(
        modifier = Modifier.padding(horizontal = 16.dp),
        generatePreview = generatePreview,
        previewAnnotationType = previewAnnotationType,
        onGeneratePreviewChange = { generatePreview = it },
        onAnnotationTypeChange = { previewAnnotationType = it },
    )
}
