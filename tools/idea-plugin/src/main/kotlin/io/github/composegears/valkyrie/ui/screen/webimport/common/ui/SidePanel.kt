package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.material.ui.MaterialFontCustomization
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun SidePanel(
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isOpen,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(onClose) {
                        detectTapGestures(onTap = { onClose() })
                    },
            )
            Box(
                modifier = Modifier
                    .widthIn(max = 250.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(JewelTheme.globalColors.borders.normal),
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun SidePanelPreview() = PreviewTheme {
    var isOpen by rememberMutableState { true }

    Box(modifier = Modifier.fillMaxSize()) {
        DefaultButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = { isOpen = true },
        ) {
            Text(text = "Open Side Panel")
        }

        SidePanel(
            isOpen = isOpen,
            onClose = { isOpen = false },
            content = {
                MaterialFontCustomization(
                    modifier = Modifier.fillMaxHeight(),
                    fontSettings = MaterialFontSettings(fill = true),
                    onClose = { isOpen = false },
                    onSettingsChange = { },
                )
            },
        )
    }
}
