package io.github.composegears.valkyrie.sdk.ir.core

sealed interface IrVectorNode {
    data class IrGroup(
        val name: String = "",
        val rotate: Float = 0f,
        val pivotX: Float = 0f,
        val pivotY: Float = 0f,
        val scaleX: Float = 1f,
        val scaleY: Float = 1f,
        val translationX: Float = 0f,
        val translationY: Float = 0f,
        val clipPathData: MutableList<IrPathNode>,
        val nodes: MutableList<IrVectorNode>,
    ) : IrVectorNode

    data class IrPath(
        val name: String = "",
        val fill: IrFill? = null,
        val fillAlpha: Float = 1f,
        val stroke: IrStroke? = null,
        val strokeAlpha: Float = 1f,
        val strokeLineWidth: Float = 0f,
        val strokeLineCap: IrStrokeLineCap = IrStrokeLineCap.Butt,
        val strokeLineJoin: IrStrokeLineJoin = IrStrokeLineJoin.Miter,
        val strokeLineMiter: Float = 4f,
        val pathFillType: IrPathFillType = IrPathFillType.NonZero,
        val paths: List<IrPathNode>,
    ) : IrVectorNode
}
