package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import java.awt.datatransfer.StringSelection
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.InternalJewelApi
import org.jetbrains.jewel.foundation.util.JewelLogger
import org.jetbrains.jewel.ui.component.ContextMenuItemOption
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(InternalJewelApi::class)
@Composable
fun LinkIcon(
    key: IconKey,
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val uriHandler = LocalUriHandler.current

    val scope = rememberCoroutineScope()

    ContextMenuArea(
        items = {
            listOf(
                ContextMenuItemOption(
                    label = message("general.action.text.open.link.in.browser"),
                    action = {
                        openUri(uriHandler, url)
                    },
                    icon = AllIconsKeys.Nodes.PpWeb,
                ),
                ContextMenuItemOption(
                    label = message("general.action.text.copy.link.address"),
                    action = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(StringSelection(url)))
                        }
                    },
                    icon = AllIconsKeys.Actions.Copy,
                ),
            )
        },
        content = {
            Icon(
                modifier = modifier.clickable {
                    openUri(uriHandler, url)
                },
                key = key,
                contentDescription = contentDescription,
            )
        },
    )
}

fun openUri(uriHandler: UriHandler, link: String) {
    runCatching {
        uriHandler.openUri(link)
    }.onFailure {
        JewelLogger.getInstance("LinkIcon").error("Unable to open link ($link). Error: $it")
    }
}

@Preview
@Composable
private fun LinkIconPreview() = PreviewTheme(alignment = Alignment.Center) {
    LinkIcon(
        key = AllIconsKeys.General.ContextHelp,
        contentDescription = "Help",
        url = "https://test.com",
    )
}
