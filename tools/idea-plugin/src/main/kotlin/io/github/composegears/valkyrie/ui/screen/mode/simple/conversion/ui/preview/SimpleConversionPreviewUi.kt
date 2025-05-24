package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.CloseAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.EditAction
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.PreviewAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.highlights.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.action.ExpandedActions
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.action.ui.EditAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.action.ui.PreviewAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.ClosePreview
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnIconNaneChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OpenSettings
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.util.IR_STUB

@Composable
fun SimpleConversionPreviewStateUi(
    state: ConversionState,
    previewType: PreviewType,
    onAction: (SimpleConversionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var codePreview by rememberMutableState(state.iconContent.code) { state.iconContent.code }
    var expandedAction by rememberMutableState { ExpandedActions.None }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar {
            CloseAction(onClose = { onAction(ClosePreview) })
            AppBarTitle(title = "Simple conversion")
            WeightSpacer()
            EditAction(
                onEdit = {
                    expandedAction = when (expandedAction) {
                        ExpandedActions.Edit -> ExpandedActions.None
                        else -> ExpandedActions.Edit
                    }
                },
                selected = expandedAction == ExpandedActions.Edit,
            )
            PreviewAction(
                onPreview = {
                    expandedAction = when (expandedAction) {
                        ExpandedActions.Preview -> ExpandedActions.None
                        else -> ExpandedActions.Preview
                    }
                },
                selected = expandedAction == ExpandedActions.Preview,
            )
            CopyAction(onCopy = { onAction(OnCopyInClipboard(codePreview)) })
            SettingsAction(openSettings = { onAction(OpenSettings) })
        }
        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = expandedAction,
            transitionSpec = {
                if (initialState != targetState) {
                    (slideInVertically { height -> -height } + fadeIn()) togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = true),
                )
            },
        ) { actions ->
            when (actions) {
                ExpandedActions.Edit -> {
                    EditAction(
                        iconName = state.iconContent.name,
                        onNameChange = { onAction(OnIconNaneChange(it)) },
                    )
                }
                ExpandedActions.Preview -> PreviewAction(
                    irImageVector = state.iconContent.irImageVector,
                    previewType = previewType,
                )
                ExpandedActions.None -> Spacer(modifier = Modifier.fillMaxWidth())
            }
        }
        HorizontalDivider()
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
                irImageVector = IR_STUB,
                code = """
                    package io.github.composegears.valkyrie.icons

                    import androidx.compose.ui.graphics.vector.ImageVector
                    import androidx.compose.ui.unit.dp

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
        previewType = PreviewType.Auto,
        onAction = {},
    )
}
