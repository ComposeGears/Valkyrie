package io.github.composegears.valkyrie.sdk.ir.core

sealed interface IrStroke {
    data class Color(val irColor: IrColor) : IrStroke
}
