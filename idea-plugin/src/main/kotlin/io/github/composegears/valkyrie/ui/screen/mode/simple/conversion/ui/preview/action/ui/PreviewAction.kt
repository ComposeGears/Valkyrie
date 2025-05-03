package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.action.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.components.previewer.ImageVectorPreviewPanel
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.IR_STUB

@Composable
fun PreviewAction(
    irImageVector: IrImageVector,
    previewType: PreviewType,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()
        ImageVectorPreviewPanel(
            modifier = Modifier.height(250.dp),
            previewType = previewType,
            irImageVector = irImageVector,
        )
    }
}

@Preview
@Composable
private fun PreviewActionPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    PreviewAction(
        irImageVector = IR_STUB,
        previewType = PreviewType.Auto,
    )
}
