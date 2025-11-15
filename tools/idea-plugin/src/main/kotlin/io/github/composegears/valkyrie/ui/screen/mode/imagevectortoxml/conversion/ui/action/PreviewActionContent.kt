package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui.action

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.components.previewer.ImageVectorPreviewPanel

@Composable
fun PreviewActionContent(
    irImageVector: IrImageVector,
    modifier: Modifier = Modifier,
    previewType: PreviewType = PreviewType.Auto,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()
        ImageVectorPreviewPanel(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            irImageVector = irImageVector,
            previewType = previewType,
        )
    }
}
