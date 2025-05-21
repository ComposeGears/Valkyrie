package io.github.composegears.valkyrie.parser.unified.model

import io.github.composegears.valkyrie.ir.IrImageVector

data class IconParserOutput(
    val iconType: IconType,
    val irImageVector: IrImageVector,
    val iconName: String,
)
