package io.github.composegears.valkyrie.jewel.editor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import io.github.composegears.valkyrie.jewel.VerticalDivider
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableIntState
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.compose.highlights.core.buildAnnotatedString
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.HorizontalScrollbar
import org.jetbrains.jewel.ui.component.VerticalScrollbar
import org.jetbrains.jewel.ui.theme.textFieldStyle
import org.jetbrains.jewel.ui.typography

@Composable
fun CodeEditor(
    text: String,
    syntaxLanguage: SyntaxLanguage,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = JewelTheme.typography.editorTextStyle,
    translateTabToSpaces: Boolean = true,
    readOnly: Boolean = false,
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val linesTextScroll = rememberScrollState()

    val isDark = JewelTheme.isDark

    val linesCount by rememberMutableIntState(text) { text.count { it == '\n' } + 1 }

    LaunchedEffect(linesTextScroll.value) {
        verticalScrollState.scrollTo(linesTextScroll.value)
    }
    LaunchedEffect(verticalScrollState.value) {
        linesTextScroll.scrollTo(verticalScrollState.value)
    }

    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(32.dp)
                    .verticalScroll(linesTextScroll)
                    .padding(top = 4.dp),
                value = IntRange(1, linesCount).joinToString(separator = "\n"),
                readOnly = true,
                textStyle = textStyle.copy(
                    color = JewelTheme.globalColors.text.disabled,
                    textAlign = TextAlign.End,
                ),
                onValueChange = {},
            )
            Spacer(8.dp)
            VerticalDivider()
            Box(modifier = Modifier.fillMaxSize()) {
                BasicTextField(
                    modifier = Modifier
                        .matchParentSize()
                        .horizontalScroll(horizontalScrollState)
                        .verticalScroll(verticalScrollState)
                        .padding(top = 4.dp),
                    value = text,
                    onValueChange = { onValueChange(it.updateTabs(translateTabToSpaces)) },
                    visualTransformation = { text ->
                        val code = Highlights.Builder()
                            .code(text.text)
                            .language(syntaxLanguage)
                            .theme(SyntaxThemes.darcula(darkMode = isDark))
                            .build()
                            .buildAnnotatedString()

                        TransformedText(
                            text = code,
                            offsetMapping = OffsetMapping.Identity,
                        )
                    },
                    cursorBrush = SolidColor(JewelTheme.textFieldStyle.colors.caret),
                    textStyle = textStyle,
                    readOnly = readOnly,
                )

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 8.dp, bottom = 4.dp),
                    scrollState = verticalScrollState,
                )
                HorizontalScrollbar(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 4.dp, end = 8.dp),
                    scrollState = horizontalScrollState,
                )
            }
        }
    }
}

private const val TAB_LENGTH = 4
private const val TAB_CHAR = "\t"

private fun String.updateTabs(translateTabToSpaces: Boolean): String {
    return if (translateTabToSpaces && contains(TAB_CHAR)) {
        replace(TAB_CHAR, " ".repeat(TAB_LENGTH))
    } else {
        this
    }
}

@Preview
@Composable
private fun CodeEditorPreview() = PreviewTheme {
    var text by rememberMutableState {
        """
        package io.github.composegears.valkyrie.compose.icons.outlined

        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.graphics.SolidColor
        import androidx.compose.ui.graphics.vector.ImageVector
        import androidx.compose.ui.graphics.vector.path
        import androidx.compose.ui.unit.dp
        import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

        val ValkyrieIcons.Outlined.Tune: ImageVector
            get() {
                if (_Tune != null) {
                    return _Tune!!
                }
                _Tune = ImageVector.Builder(
                    name = "Outlined.Tune",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 960f,
                    viewportHeight = 960f,
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(440f, 840f)
                        verticalLineToRelative(-240f)
                        horizontalLineToRelative(80f)
                        verticalLineToRelative(80f)
                        horizontalLineToRelative(320f)
                        verticalLineToRelative(80f)
                        lineTo(520f, 760f)
                        verticalLineToRelative(80f)
                        horizontalLineToRelative(-80f)
                        close()
                        moveTo(120f, 760f)
                        verticalLineToRelative(-80f)
                        horizontalLineToRelative(240f)
                        verticalLineToRelative(80f)
                        lineTo(120f, 760f)
                        close()
                        moveTo(280f, 600f)
                        verticalLineToRelative(-80f)
                        lineTo(120f, 520f)
                        verticalLineToRelative(-80f)
                        horizontalLineToRelative(160f)
                        verticalLineToRelative(-80f)
                        horizontalLineToRelative(80f)
                        verticalLineToRelative(240f)
                        horizontalLineToRelative(-80f)
                        close()
                        moveTo(440f, 520f)
                        verticalLineToRelative(-80f)
                        horizontalLineToRelative(400f)
                        verticalLineToRelative(80f)
                        lineTo(440f, 520f)
                        close()
                        moveTo(600f, 360f)
                        verticalLineToRelative(-240f)
                        horizontalLineToRelative(80f)
                        verticalLineToRelative(80f)
                        horizontalLineToRelative(160f)
                        verticalLineToRelative(80f)
                        lineTo(680f, 280f)
                        verticalLineToRelative(80f)
                        horizontalLineToRelative(-80f)
                        close()
                        moveTo(120f, 280f)
                        verticalLineToRelative(-80f)
                        horizontalLineToRelative(400f)
                        verticalLineToRelative(80f)
                        lineTo(120f, 280f)
                        close()
                    }
                }.build()

                return _Tune!!
            }

        @Suppress("ObjectPropertyName")
        private var _Tune: ImageVector? = null

        """.trimIndent()
    }
    CodeEditor(
        syntaxLanguage = SyntaxLanguage.KOTLIN,
        text = text,
        onValueChange = { text = it },
    )
}
