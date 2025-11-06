package io.github.composegears.valkyrie.sdk.ir.core

data class IrImageVector(
    val name: String = "",
    val autoMirror: Boolean = false,
    val defaultWidth: Float,
    val defaultHeight: Float,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val nodes: List<IrVectorNode>,
)
