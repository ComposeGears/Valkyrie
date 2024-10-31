package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.FocusableTextField
import io.github.composegears.valkyrie.ui.foundation.FocusableTextFieldDefaults
import io.github.composegears.valkyrie.ui.foundation.HorizontalSpacer
import io.github.composegears.valkyrie.ui.foundation.SizeSpacer
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.highlights.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconContent
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnIconNaneChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.util.PainterConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SimpleConversionPreviewStateUi(
    state: ConversionState,
    onAction: (SimpleConversionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var codePreview by rememberMutableState(state.iconContent.code) { state.iconContent.code }

    Column(modifier = modifier.fillMaxSize()) {
        VerticalSpacer(4.dp)
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = Dp.Hairline,
        )
        VerticalSpacer(8.dp)
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FocusableTextField(
                value = state.iconContent.name,
                onValueChange = { onAction(OnIconNaneChange(it)) },
                colors = FocusableTextFieldDefaults.colors().copy(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
            HorizontalSpacer(8.dp)
            CopyAction(onCopy = { onAction(OnCopyInClipboard(codePreview)) })
            WeightSpacer()
            IconPreview(state = state)
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

@Composable
private fun IconPreview(state: ConversionState) {
    var bgType by rememberMutableState { BgType.PixelGrid }

    val iconPainter by produceState<Painter?>(initialValue = null) {
        value = withContext(Dispatchers.Default) {
            when (val source = state.iconSource) {
                is IconSource.FileBasedIcon -> PainterConverter.from(
                    path = source.path,
                )
                is IconSource.StringBasedIcon -> PainterConverter.from(
                    text = source.text,
                    iconType = state.iconContent.iconType,
                )
            }
        }
    }

    when (val painter = iconPainter) {
        null -> {
            if (LocalInspectionMode.current) {
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(Color.LightGray),
                )
            } else {
                SizeSpacer(32.dp)
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                bgType = bgType.next()
                            },
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                PreviewBackground(
                    bgType = bgType,
                    gridSize = (32f / 11f).dp,
                    modifier = Modifier.matchParentSize(),
                )
                Image(
                    modifier = Modifier.matchParentSize(),
                    painter = painter,
                    contentDescription = null,
                )
            }
        }
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
                iconType = SVG,
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
        onAction = {},
    )
}
