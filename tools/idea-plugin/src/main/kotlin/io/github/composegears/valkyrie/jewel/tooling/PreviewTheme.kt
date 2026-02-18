package io.github.composegears.valkyrie.jewel.tooling

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.jewel.banner.LocalGlobalBannerState
import io.github.composegears.valkyrie.jewel.banner.rememberBannerState
import io.github.composegears.valkyrie.jewel.colors.errorFocused
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.platform.rememberProjectAccessor
import io.github.composegears.valkyrie.sdk.compose.foundation.dim
import io.github.composegears.valkyrie.ui.di.DI
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.Stroke
import org.jetbrains.jewel.foundation.modifier.border
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ProjectPreviewTheme(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.(Project) -> Unit,
) {
    val projectAccessor = rememberProjectAccessor()
    val project = projectAccessor.project ?: error("No project available in preview")

    LaunchedEffect(Unit) {
        GlobalPreviewState.isPreview = true
        DI.initWith(project)
    }

    CompositionLocalProvider(
        LocalProject provides project,
        LocalGlobalBannerState provides rememberBannerState(),
    ) {
        PreviewTheme(
            modifier = modifier,
            alignment = alignment,
            content = { content(project) },
        )
    }
}

@Composable
fun PreviewTheme(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        content = content,
        contentAlignment = alignment,
    )
}

@Composable
fun Modifier.debugBounds(): Modifier {
    return this
        .border(
            alignment = Stroke.Alignment.Outside,
            width = Dp.Hairline,
            shape = RectangleShape,
            color = JewelTheme.errorFocused.dim(),
            expand = 2.dp,
        )
}

@Preview
@Composable
private fun PreviewThemePreview() = PreviewTheme(alignment = Alignment.Center) {
    Text(
        modifier = Modifier.debugBounds(),
        text = "Content",
    )
}
