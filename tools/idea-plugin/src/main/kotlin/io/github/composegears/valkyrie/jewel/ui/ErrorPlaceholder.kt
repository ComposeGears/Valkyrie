package io.github.composegears.valkyrie.jewel.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ErrorPlaceholder(
    message: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = message,
            maxLines = 1,
        )
        if (description != null) {
            Spacer(16.dp)

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
                    modifier = Modifier.padding(4.dp),
                    text = description,
                    maxLines = 5,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ErrorPlaceholderPreview() = PreviewTheme(alignment = Alignment.Center) {
    ErrorPlaceholder(
        message = "Message",
        description = "Description Description Description",
    )
}
