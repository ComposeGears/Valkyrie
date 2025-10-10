package io.github.composegears.valkyrie.ir

data class IrImageVector(
    val name: String = "",
    val autoMirror: Boolean = false,
    val defaultWidth: Float,
    val defaultHeight: Float,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val nodes: List<IrVectorNode>,
)

val IrImageVector.aspectRatio: Float
    get() = if (viewportHeight != 0f && viewportWidth != 0f) {
        viewportWidth / viewportHeight
    } else {
        1f
    }
