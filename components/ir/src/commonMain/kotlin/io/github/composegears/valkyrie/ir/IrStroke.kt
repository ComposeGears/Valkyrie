package io.github.composegears.valkyrie.ir

sealed interface IrStroke {
    data class Color(val irColor: IrColor) : IrStroke
}
