package io.github.composegears.valkyrie.flow.simple.screen.conversion

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes
import io.github.composegears.valkyrie.compose.codeviewer.core.CodeViewer
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.ui.foundation.AnimatedVerticalScrollbar
import io.github.composegears.valkyrie.compose.util.isLight
import io.github.composegears.valkyrie.ui.AnimatedVisibilityScope
import io.github.composegears.valkyrie.ui.AppBarTitle
import io.github.composegears.valkyrie.ui.CloseAction
import io.github.composegears.valkyrie.ui.SharedTransitionScope
import io.github.composegears.valkyrie.ui.TopAppBar
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import org.jetbrains.compose.resources.stringResource
import valkyrie.tools.compose_app.generated.resources.Res
import valkyrie.tools.compose_app.generated.resources.simple_conversion_screen_title

val SimpleConversionScreen by navDestination<List<PlatformFile>> {
    val navArgs = navArgs()
    val navController = navController()

    SimpleConversionUi(
        files = navArgs,
        onClose = { navController.parent?.back() },
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SimpleConversionUi(
    files: List<PlatformFile>,
    onClose: () -> Unit,
) {
    SharedTransitionScope {
        AnimatedVisibilityScope {
            Surface {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar {
                        CloseAction(onClose = onClose)
                        AppBarTitle(
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("top-appbar"),
                                    animatedVisibilityScope = this@AnimatedVisibilityScope,
                                )
                                .renderInSharedTransitionScopeOverlay(),
                            title = stringResource(Res.string.simple_conversion_screen_title),
                        )
                    }
                    Row(modifier = Modifier.fillMaxSize()) {
                        var selectedIcon by rememberMutableState { files.first() }
                        val text by produceState("", key1 = selectedIcon) {
                            value = selectedIcon.readString()
                        }
                        val isLight = MaterialTheme.colorScheme.isLight
                        var highlights by rememberMutableState(isLight, text) {
                            Highlights.Builder()
                                .code(text)
                                .theme(SyntaxThemes.darcula(darkMode = !isLight))
                                .build()
                        }
                        IconListSection(
                            modifier = Modifier.width(360.dp),
                            files = files,
                            currentFile = selectedIcon,
                            onFileSelect = { selectedIcon = it },
                        )
                        CodeViewer(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            highlights = highlights,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconListSection(
    files: List<PlatformFile>,
    currentFile: PlatformFile,
    modifier: Modifier = Modifier,
    onFileSelect: (PlatformFile) -> Unit,
) {
    Column(modifier = modifier) {
        val lazyListState = rememberLazyListState()

        Box {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(files) { file ->
                    IconItem(
                        name = file.name,
                        selected = currentFile == file,
                        onClick = { onFileSelect(file) },
                    )
                }
            }
            AnimatedVerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(vertical = 8.dp),
                adapter = rememberScrollbarAdapter(lazyListState),
                isScrollInProgress = lazyListState.isScrollInProgress,
            )
        }
    }
}

@Composable
private fun IconItem(
    selected: Boolean,
    name: String,
    onClick: () -> Unit,
) {
    val border = when {
        selected -> BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary)
        else -> null
    }
    CenterVerticalRow(
        modifier = Modifier.fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(if (border != null) Modifier.border(border, MaterialTheme.shapes.small) else Modifier)
            .clickable(onClick = onClick),
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
