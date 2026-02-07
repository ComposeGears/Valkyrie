package io.github.composegears.valkyrie.jewel.ui.placeholder

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.debugBounds
import io.github.composegears.valkyrie.jewel.tooling.lorem
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.util.ValkyrieBundle
import java.awt.datatransfer.StringSelection
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.ContextMenuItemOption
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ErrorPlaceholder(
    message: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = message,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        if (description != null) {
            Spacer(16.dp)

            val clipboard = LocalClipboard.current
            val scope = rememberCoroutineScope()

            ContextMenuArea(
                items = {
                    listOf(
                        ContextMenuItemOption(
                            label = ValkyrieBundle.message("ui.placeholder.error.context.menu.copy"),
                            action = {
                                scope.launch {
                                    clipboard.setClipEntry(ClipEntry(StringSelection(description)))
                                }
                            },
                            icon = AllIconsKeys.Actions.Copy,
                        ),
                    )
                },
                content = {
                    val primaryColor = JewelTheme.primaryColor
                    Box(
                        modifier = Modifier
                            .background(
                                color = JewelTheme.globalColors.borders.disabled,
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 4.dp,
                                    bottomEnd = 4.dp,
                                    bottomStart = 0.dp,
                                ),
                            )
                            .drawBehind {
                                val inset = 2.dp.toPx()
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(0f, inset / 2f),
                                    end = Offset(0f, size.height - inset / 2f),
                                    strokeWidth = inset,
                                    cap = StrokeCap.Round,
                                )
                            },
                    ) {
                        InfoText(
                            modifier = Modifier.padding(8.dp),
                            text = description,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPlaceholderPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column {
        ErrorPlaceholder(
            modifier = Modifier.debugBounds(),
            message = lorem(3),
        )
        ErrorPlaceholder(
            modifier = Modifier.debugBounds(),
            message = lorem(3),
            description = lorem(3),
        )
        ErrorPlaceholder(
            modifier = Modifier.debugBounds(),
            message = lorem(3),
            description = lorem(30),
        )
    }
}
